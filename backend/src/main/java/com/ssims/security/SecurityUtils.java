package com.ssims.security;

import com.ssims.domain.User;
import com.ssims.web.ApiException;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

public final class SecurityUtils {

	private SecurityUtils() {
	}

	public static AppUserDetails currentDetails() {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		if (authentication == null || !(authentication.getPrincipal() instanceof AppUserDetails details)) {
			throw new ApiException(HttpStatus.UNAUTHORIZED, "Please sign in");
		}
		return details;
	}

	public static User currentUser() {
		return currentDetails().getUser();
	}

	public static Long currentUserId() {
		return currentDetails().getId();
	}
}
