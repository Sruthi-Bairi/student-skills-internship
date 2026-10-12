package com.ssims.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record ProfileRequest(
		@NotBlank @Size(max = 120) String fullName,
		@Size(max = 160) String college,
		@Size(max = 120) String course,
		Integer yearOfStudy,
		@Size(max = 20) String phone,
		@Size(max = 1000) String bio
) {
}
