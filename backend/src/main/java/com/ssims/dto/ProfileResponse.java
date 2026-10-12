package com.ssims.dto;

public record ProfileResponse(
		Long id,
		Long userId,
		String email,
		String fullName,
		String college,
		String course,
		Integer yearOfStudy,
		String phone,
		String bio
) {
}
