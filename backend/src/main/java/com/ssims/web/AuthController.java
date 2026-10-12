package com.ssims.web;

import com.ssims.dto.CsrfResponse;
import com.ssims.dto.LoginRequest;
import com.ssims.dto.RegisterRequest;
import com.ssims.dto.UserResponse;
import com.ssims.security.AppUserDetails;
import com.ssims.service.AuthService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.web.csrf.CsrfToken;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

	private final AuthService authService;

	public AuthController(AuthService authService) {
		this.authService = authService;
	}

	@GetMapping("/csrf")
	public CsrfResponse csrf(CsrfToken token) {
		return new CsrfResponse(token.getToken(), token.getHeaderName(), "XSRF-TOKEN");
	}

	@PostMapping("/register")
	public UserResponse register(@Valid @RequestBody RegisterRequest request, HttpServletRequest httpRequest,
			HttpServletResponse httpResponse) {
		return authService.register(request, httpRequest, httpResponse);
	}

	@PostMapping("/login")
	public UserResponse login(@Valid @RequestBody LoginRequest request, HttpServletRequest httpRequest,
			HttpServletResponse httpResponse) {
		return authService.login(request, httpRequest, httpResponse);
	}

@PostMapping("/logout")
public ResponseEntity<Void> logout(HttpServletRequest request) {
    authService.logout(request);
    return ResponseEntity.noContent().build();
}

	@GetMapping("/me")
	public UserResponse me(@AuthenticationPrincipal AppUserDetails details) {
		return authService.me(details);
	}
}
