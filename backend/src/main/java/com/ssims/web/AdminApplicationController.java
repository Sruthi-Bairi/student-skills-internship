
package com.ssims.web;

import com.ssims.dto.ApplicationResponse;
import com.ssims.dto.StatusUpdateRequest;
import com.ssims.service.ApplicationService;
import jakarta.validation.Valid;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.List;

@RestController
@RequestMapping("/api/admin/applications")
@PreAuthorize("hasRole('ADMIN')")
public class AdminApplicationController {

    private final ApplicationService applicationService;

    public AdminApplicationController(ApplicationService applicationService) {
        this.applicationService = applicationService;
    }

    @GetMapping
    public List<ApplicationResponse> list() {
        return applicationService.all();
    }

    @PatchMapping("/{id}/status")
    public ApplicationResponse updateStatus(
            @PathVariable Long id,
            @Valid @RequestBody StatusUpdateRequest request
    ) {
        return applicationService.updateStatus(id, request);
    }
	@GetMapping("/debug-auth")
public java.util.Map<String, Object> debugAuth() {
    Authentication auth = SecurityContextHolder.getContext().getAuthentication();

    return java.util.Map.of(
        "authenticated", auth != null && auth.isAuthenticated(),
        "principal", auth == null ? "none" : auth.getName(),
        "authorities", auth == null ? java.util.List.of() : auth.getAuthorities()
    );
}
}
