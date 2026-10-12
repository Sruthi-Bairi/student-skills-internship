package com.ssims.service;

import com.ssims.domain.ApplicationStatus;
import com.ssims.domain.Role;
import com.ssims.domain.StudentProfile;
import com.ssims.dto.DashboardStatsResponse;
import com.ssims.dto.StudentSummaryResponse;
import com.ssims.repo.InternshipApplicationRepository;
import com.ssims.repo.InternshipRepository;
import com.ssims.repo.StudentProfileRepository;
import com.ssims.repo.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class AdminService {

	private final UserRepository userRepository;
	private final InternshipRepository internshipRepository;
	private final InternshipApplicationRepository applicationRepository;
	private final StudentProfileRepository profileRepository;

	public AdminService(UserRepository userRepository, InternshipRepository internshipRepository,
			InternshipApplicationRepository applicationRepository, StudentProfileRepository profileRepository) {
		this.userRepository = userRepository;
		this.internshipRepository = internshipRepository;
		this.applicationRepository = applicationRepository;
		this.profileRepository = profileRepository;
	}

	@Transactional(readOnly = true)
	public DashboardStatsResponse stats() {
		return new DashboardStatsResponse(
				userRepository.findByRoleOrderByCreatedAtDesc(Role.STUDENT).size(),
				internshipRepository.count(),
				applicationRepository.count(),
				applicationRepository.countByStatus(ApplicationStatus.PENDING),
				applicationRepository.countByStatus(ApplicationStatus.ACCEPTED),
				applicationRepository.countByStatus(ApplicationStatus.REJECTED)
		);
	}

	@Transactional(readOnly = true)
	public List<StudentSummaryResponse> students() {
		return userRepository.findByRoleOrderByCreatedAtDesc(Role.STUDENT).stream()
				.map(user -> {
					StudentProfile profile = profileRepository.findByUserId(user.getId()).orElse(null);
					return new StudentSummaryResponse(
							user.getId(),
							user.getEmail(),
							profile == null ? null : profile.getFullName(),
							profile == null ? null : profile.getCollege(),
							profile == null ? null : profile.getCourse(),
							user.getCreatedAt()
					);
				})
				.toList();
	}
}
