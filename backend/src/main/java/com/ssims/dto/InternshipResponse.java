package com.ssims.dto;

import java.time.LocalDate;

public record InternshipResponse(
		Long id,
		String title,
		String company,
		String location,
		String description,
		String requiredSkills,
		String duration,
		String stipend,
		LocalDate deadline,
		boolean active
) {
}
