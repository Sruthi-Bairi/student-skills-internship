package com.ssims.dto;

import com.ssims.domain.Proficiency;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record SkillRequest(
		@NotBlank @Size(max = 80) String name,
		@NotNull Proficiency proficiency
) {
}
