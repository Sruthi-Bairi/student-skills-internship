package com.ssims.web;

import com.ssims.dto.SkillRequest;
import com.ssims.dto.SkillResponse;
import com.ssims.security.SecurityUtils;
import com.ssims.service.SkillService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/skills")
@PreAuthorize("hasRole('STUDENT')")
public class SkillController {

	private final SkillService skillService;

	public SkillController(SkillService skillService) {
		this.skillService = skillService;
	}

	@GetMapping
	public List<SkillResponse> list() {
		return skillService.listMine(SecurityUtils.currentUserId());
	}

	@PostMapping
	@ResponseStatus(HttpStatus.CREATED)
	public SkillResponse create(@Valid @RequestBody SkillRequest request) {
		return skillService.create(SecurityUtils.currentUserId(), request);
	}

	@PutMapping("/{id}")
	public SkillResponse update(@PathVariable Long id, @Valid @RequestBody SkillRequest request) {
		return skillService.update(SecurityUtils.currentUserId(), id, request);
	}

	@DeleteMapping("/{id}")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void delete(@PathVariable Long id) {
		skillService.delete(SecurityUtils.currentUserId(), id);
	}
}
