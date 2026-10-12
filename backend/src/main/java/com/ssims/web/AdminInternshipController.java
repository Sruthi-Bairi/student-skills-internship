package com.ssims.web;

import com.ssims.dto.InternshipRequest;
import com.ssims.dto.InternshipResponse;
import com.ssims.service.InternshipService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/admin/internships")
public class AdminInternshipController {

	private final InternshipService internshipService;

	public AdminInternshipController(InternshipService internshipService) {
		this.internshipService = internshipService;
	}

	@GetMapping
	public List<InternshipResponse> list(
			@RequestParam(required = false) String q,
			@RequestParam(required = false) String location
	) {
		return internshipService.search(q, location, false);
	}

	@PostMapping
	@ResponseStatus(HttpStatus.CREATED)
	public InternshipResponse create(@Valid @RequestBody InternshipRequest request) {
		return internshipService.create(request);
	}

	@PutMapping("/{id}")
	public InternshipResponse update(@PathVariable Long id, @Valid @RequestBody InternshipRequest request) {
		return internshipService.update(id, request);
	}

	@DeleteMapping("/{id}")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void delete(@PathVariable Long id) {
		internshipService.delete(id);
	}
}
