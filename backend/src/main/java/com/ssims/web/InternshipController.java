package com.ssims.web;

import com.ssims.dto.InternshipResponse;
import com.ssims.service.InternshipService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/internships")
public class InternshipController {

	private final InternshipService internshipService;

	public InternshipController(InternshipService internshipService) {
		this.internshipService = internshipService;
	}

	@GetMapping
	public List<InternshipResponse> search(
			@RequestParam(required = false) String q,
			@RequestParam(required = false) String location,
			@RequestParam(required = false, defaultValue = "true") boolean activeOnly
	) {
		return internshipService.search(q, location, activeOnly);
	}

	@GetMapping("/{id}")
	public InternshipResponse get(@PathVariable Long id) {
		return internshipService.get(id);
	}
}
