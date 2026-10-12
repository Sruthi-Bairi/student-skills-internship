package com.ssims.dto;

import com.ssims.domain.ApplicationStatus;

import java.time.Instant;

public record ApplicationResponse(
		Long id,
		Long studentId,
		String studentEmail,
		String studentName,
		Long internshipId,
		String internshipTitle,
		String company,
		ApplicationStatus status,
		Instant appliedAt,
		String coverNote
) {
}
