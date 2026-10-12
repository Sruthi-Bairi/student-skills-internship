package com.ssims.service;

import com.ssims.domain.Skill;
import com.ssims.domain.StudentProfile;
import com.ssims.dto.SkillRequest;
import com.ssims.dto.SkillResponse;
import com.ssims.repo.SkillRepository;
import com.ssims.web.ApiException;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class SkillService {

	private final SkillRepository skillRepository;
	private final ProfileService profileService;

	public SkillService(SkillRepository skillRepository, ProfileService profileService) {
		this.skillRepository = skillRepository;
		this.profileService = profileService;
	}

	@Transactional(readOnly = true)
	public List<SkillResponse> listMine(Long userId) {
		StudentProfile profile = profileService.requireProfile(userId);
		return skillRepository.findByProfileIdOrderByNameAsc(profile.getId()).stream()
				.map(this::toResponse)
				.toList();
	}

	@Transactional
	public SkillResponse create(Long userId, SkillRequest request) {
		StudentProfile profile = profileService.requireProfile(userId);
		Skill skill = new Skill();
		skill.setProfile(profile);
		skill.setName(request.name().trim());
		skill.setProficiency(request.proficiency());
		return toResponse(skillRepository.save(skill));
	}

	@Transactional
	public SkillResponse update(Long userId, Long skillId, SkillRequest request) {
		StudentProfile profile = profileService.requireProfile(userId);
		Skill skill = skillRepository.findByIdAndProfileId(skillId, profile.getId())
				.orElseThrow(() -> new ApiException(HttpStatus.NOT_FOUND, "Skill not found"));
		skill.setName(request.name().trim());
		skill.setProficiency(request.proficiency());
		return toResponse(skillRepository.save(skill));
	}

	@Transactional
	public void delete(Long userId, Long skillId) {
		StudentProfile profile = profileService.requireProfile(userId);
		Skill skill = skillRepository.findByIdAndProfileId(skillId, profile.getId())
				.orElseThrow(() -> new ApiException(HttpStatus.NOT_FOUND, "Skill not found"));
		skillRepository.delete(skill);
	}

	private SkillResponse toResponse(Skill skill) {
		return new SkillResponse(skill.getId(), skill.getName(), skill.getProficiency());
	}
}
