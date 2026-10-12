package com.ssims.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import java.time.LocalDate;

public record InternshipRequest(
		@NotBlank @Size(max = 160) String title,
		@NotBlank @Size(max = 160) String company,
		@Size(max = 120) String location,
		@NotBlank @Size(max = 4000) String description,
		@Size(max = 400) String requiredSkills,
		@Size(max = 80) String duration,
		@Size(max = 80) String stipend,
		LocalDate deadline,
		Boolean active
) {
}
