package com.ssims.service;

import com.ssims.domain.ApplicationStatus;
import com.ssims.domain.Internship;
import com.ssims.domain.InternshipApplication;
import com.ssims.domain.StudentProfile;
import com.ssims.domain.User;
import com.ssims.dto.ApplicationResponse;
import com.ssims.dto.ApplyRequest;
import com.ssims.dto.StatusUpdateRequest;
import com.ssims.repo.InternshipApplicationRepository;
import com.ssims.repo.StudentProfileRepository;
import com.ssims.repo.UserRepository;
import com.ssims.web.ApiException;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class ApplicationService {

	private final InternshipApplicationRepository applicationRepository;
	private final InternshipService internshipService;
	private final UserRepository userRepository;
	private final StudentProfileRepository profileRepository;

	public ApplicationService(InternshipApplicationRepository applicationRepository,
			InternshipService internshipService, UserRepository userRepository,
			StudentProfileRepository profileRepository) {
		this.applicationRepository = applicationRepository;
		this.internshipService = internshipService;
		this.userRepository = userRepository;
		this.profileRepository = profileRepository;
	}

	@Transactional
	public ApplicationResponse apply(Long studentId, ApplyRequest request) {
		if (applicationRepository.existsByStudentIdAndInternshipId(studentId, request.internshipId())) {
			throw new ApiException(HttpStatus.CONFLICT, "You have already applied for this internship");
		}
		Internship internship = internshipService.require(request.internshipId());
		if (!internship.isActive()) {
			throw new ApiException(HttpStatus.BAD_REQUEST, "This internship is no longer open");
		}
		User student = userRepository.findById(studentId)
				.orElseThrow(() -> new ApiException(HttpStatus.NOT_FOUND, "Student not found"));
		InternshipApplication application = new InternshipApplication();
		application.setStudent(student);
		application.setInternship(internship);
		application.setStatus(ApplicationStatus.PENDING);
		application.setCoverNote(request.coverNote() == null ? null : request.coverNote().trim());
		return toResponse(applicationRepository.save(application));
	}

	@Transactional(readOnly = true)
	public List<ApplicationResponse> mine(Long studentId) {
		return applicationRepository.findByStudentIdOrderByAppliedAtDesc(studentId).stream()
				.map(this::toResponse)
				.toList();
	}

	@Transactional(readOnly = true)
	public List<ApplicationResponse> all() {
		return applicationRepository.findAllByOrderByAppliedAtDesc().stream()
				.map(this::toResponse)
				.toList();
	}

	@Transactional
	public ApplicationResponse updateStatus(Long applicationId, StatusUpdateRequest request) {
		InternshipApplication application = applicationRepository.findById(applicationId)
				.orElseThrow(() -> new ApiException(HttpStatus.NOT_FOUND, "Application not found"));
		application.setStatus(request.status());
		return toResponse(applicationRepository.save(application));
	}

	private ApplicationResponse toResponse(InternshipApplication application) {
		User student = application.getStudent();
		String name = profileRepository.findByUserId(student.getId())
				.map(StudentProfile::getFullName)
				.orElse(null);
		Internship internship = application.getInternship();
		return new ApplicationResponse(
				application.getId(),
				student.getId(),
				student.getEmail(),
				name,
				internship.getId(),
				internship.getTitle(),
				internship.getCompany(),
				application.getStatus(),
				application.getAppliedAt(),
				application.getCoverNote()
		);
	}
}
