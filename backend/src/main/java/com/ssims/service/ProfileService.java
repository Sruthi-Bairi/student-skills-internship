package com.ssims.service;

import com.ssims.domain.StudentProfile;
import com.ssims.domain.User;
import com.ssims.dto.ProfileRequest;
import com.ssims.dto.ProfileResponse;
import com.ssims.repo.StudentProfileRepository;
import com.ssims.repo.UserRepository;
import com.ssims.web.ApiException;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ProfileService {

	private final StudentProfileRepository profileRepository;
	private final UserRepository userRepository;

	public ProfileService(StudentProfileRepository profileRepository, UserRepository userRepository) {
		this.profileRepository = profileRepository;
		this.userRepository = userRepository;
	}

	@Transactional(readOnly = true)
	public ProfileResponse getMine(Long userId) {
		return toResponse(requireProfile(userId));
	}

	@Transactional
	public ProfileResponse updateMine(Long userId, ProfileRequest request) {
		StudentProfile profile = requireProfile(userId);
		profile.setFullName(request.fullName().trim());
		profile.setCollege(blankToNull(request.college()));
		profile.setCourse(blankToNull(request.course()));
		profile.setYearOfStudy(request.yearOfStudy());
		profile.setPhone(blankToNull(request.phone()));
		profile.setBio(blankToNull(request.bio()));
		return toResponse(profileRepository.save(profile));
	}

	public StudentProfile requireProfile(Long userId) {
		return profileRepository.findByUserId(userId)
				.orElseThrow(() -> new ApiException(HttpStatus.NOT_FOUND, "Create your student profile first"));
	}

	private ProfileResponse toResponse(StudentProfile profile) {
		User user = userRepository.findById(profile.getUser().getId()).orElse(profile.getUser());
		return new ProfileResponse(
				profile.getId(),
				user.getId(),
				user.getEmail(),
				profile.getFullName(),
				profile.getCollege(),
				profile.getCourse(),
				profile.getYearOfStudy(),
				profile.getPhone(),
				profile.getBio()
		);
	}

	private String blankToNull(String value) {
		if (value == null || value.isBlank()) {
			return null;
		}
		return value.trim();
	}
}
