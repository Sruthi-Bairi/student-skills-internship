package com.ssims.web;

import com.ssims.dto.ProfileRequest;
import com.ssims.dto.ProfileResponse;
import com.ssims.security.SecurityUtils;
import com.ssims.service.ProfileService;
import jakarta.validation.Valid;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/profile")
@PreAuthorize("hasRole('STUDENT')")
public class ProfileController {

	private final ProfileService profileService;

	public ProfileController(ProfileService profileService) {
		this.profileService = profileService;
	}

	@GetMapping
	public ProfileResponse get() {
		return profileService.getMine(SecurityUtils.currentUserId());
	}

	@PutMapping
	public ProfileResponse update(@Valid @RequestBody ProfileRequest request) {
		return profileService.updateMine(SecurityUtils.currentUserId(), request);
	}
}
