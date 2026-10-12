package com.ssims.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record ApplyRequest(
		@NotNull Long internshipId,
		@Size(max = 1000) String coverNote
) {
}
