package com.ssims.dto;

public record DashboardStatsResponse(
		long students,
		long internships,
		long applications,
		long pending,
		long accepted,
		long rejected
) {
}
