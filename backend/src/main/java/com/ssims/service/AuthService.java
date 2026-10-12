package com.ssims.service;

import com.ssims.domain.Role;
import com.ssims.domain.StudentProfile;
import com.ssims.domain.User;
import com.ssims.dto.LoginRequest;
import com.ssims.dto.RegisterRequest;
import com.ssims.dto.UserResponse;
import com.ssims.repo.StudentProfileRepository;
import com.ssims.repo.UserRepository;
import com.ssims.security.AppUserDetails;
import com.ssims.web.ApiException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;
import org.springframework.security.web.context.SecurityContextRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AuthService {

	private final UserRepository userRepository;
	private final StudentProfileRepository profileRepository;
	private final PasswordEncoder passwordEncoder;
	private final AuthenticationManager authenticationManager;
	private final SecurityContextRepository securityContextRepository = new HttpSessionSecurityContextRepository();

	public AuthService(UserRepository userRepository, StudentProfileRepository profileRepository,
			PasswordEncoder passwordEncoder, AuthenticationManager authenticationManager) {
		this.userRepository = userRepository;
		this.profileRepository = profileRepository;
		this.passwordEncoder = passwordEncoder;
		this.authenticationManager = authenticationManager;
	}

	@Transactional
	public UserResponse register(RegisterRequest request, HttpServletRequest httpRequest, HttpServletResponse httpResponse) {
		if (userRepository.existsByEmailIgnoreCase(request.email())) {
			throw new ApiException(HttpStatus.CONFLICT, "An account with this email already exists");
		}
		User user = new User();
		user.setEmail(request.email().trim().toLowerCase());
		user.setPassword(passwordEncoder.encode(request.password()));
		user.setRole(Role.STUDENT);
		user = userRepository.save(user);

		StudentProfile profile = new StudentProfile();
		profile.setUser(user);
		profile.setFullName(request.fullName().trim());
		profileRepository.save(profile);

		return login(new LoginRequest(request.email(), request.password()), httpRequest, httpResponse);
	}

	public UserResponse login(LoginRequest request, HttpServletRequest httpRequest, HttpServletResponse httpResponse) {
		Authentication authentication = authenticationManager.authenticate(
				new UsernamePasswordAuthenticationToken(request.email().trim().toLowerCase(), request.password()));
		SecurityContext context = SecurityContextHolder.createEmptyContext();
		context.setAuthentication(authentication);
		SecurityContextHolder.setContext(context);
		securityContextRepository.saveContext(context, httpRequest, httpResponse);
		return toResponse((AppUserDetails) authentication.getPrincipal());
	}

	public void logout(HttpServletRequest request) {
		SecurityContextHolder.clearContext();
		HttpSession session = request.getSession(false);
		if (session != null) {
			session.invalidate();
		}
	}

	@Transactional(readOnly = true)
	public UserResponse me(AppUserDetails details) {
		User user = userRepository.findById(details.getId())
				.orElseThrow(() -> new ApiException(HttpStatus.UNAUTHORIZED, "Please sign in"));
		return toResponse(user);
	}

	public UserResponse toResponse(AppUserDetails details) {
		return toResponse(details.getUser());
	}

	public UserResponse toResponse(User user) {
		String fullName = profileRepository.findByUserId(user.getId())
				.map(StudentProfile::getFullName)
				.orElse(null);
		return new UserResponse(user.getId(), user.getEmail(), user.getRole(), fullName);
	}
}
