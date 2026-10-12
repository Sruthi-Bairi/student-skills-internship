package com.ssims.dto;

import com.ssims.domain.Proficiency;

public record SkillResponse(Long id, String name, Proficiency proficiency) {
}
