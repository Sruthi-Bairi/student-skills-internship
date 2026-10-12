package com.ssims.dto;

import java.time.Instant;

public record StudentSummaryResponse(
		Long id,
		String email,
		String fullName,
		String college,
		String course,
		Instant createdAt
) {
}
