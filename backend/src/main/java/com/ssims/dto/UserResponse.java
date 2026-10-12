package com.ssims.dto;

import com.ssims.domain.Role;

public record UserResponse(Long id, String email, Role role, String fullName) {
}
