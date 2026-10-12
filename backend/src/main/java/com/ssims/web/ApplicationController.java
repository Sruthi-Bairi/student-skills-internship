package com.ssims.web;

import com.ssims.dto.ApplicationResponse;
import com.ssims.dto.ApplyRequest;
import com.ssims.security.SecurityUtils;
import com.ssims.service.ApplicationService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/applications")
@PreAuthorize("hasRole('STUDENT')")
public class ApplicationController {

	private final ApplicationService applicationService;

	public ApplicationController(ApplicationService applicationService) {
		this.applicationService = applicationService;
	}

	@GetMapping("/me")
	public List<ApplicationResponse> mine() {
		return applicationService.mine(SecurityUtils.currentUserId());
	}

	@PostMapping
	@ResponseStatus(HttpStatus.CREATED)
	public ApplicationResponse apply(@Valid @RequestBody ApplyRequest request) {
		return applicationService.apply(SecurityUtils.currentUserId(), request);
	}
}
