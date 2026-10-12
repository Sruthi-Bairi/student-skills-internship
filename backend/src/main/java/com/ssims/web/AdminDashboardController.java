package com.ssims.web;

import com.ssims.dto.DashboardStatsResponse;
import com.ssims.dto.StudentSummaryResponse;
import com.ssims.service.AdminService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/admin")
public class AdminDashboardController {

	private final AdminService adminService;

	public AdminDashboardController(AdminService adminService) {
		this.adminService = adminService;
	}

	@GetMapping("/stats")
	public DashboardStatsResponse stats() {
		return adminService.stats();
	}

	@GetMapping("/students")
	public List<StudentSummaryResponse> students() {
		return adminService.students();
	}
}
