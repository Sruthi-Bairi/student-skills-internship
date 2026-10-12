package com.ssims.web;

import com.ssims.dto.ApiError;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authorization.AuthorizationDeniedException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.Instant;
import java.util.List;

@RestControllerAdvice
public class GlobalExceptionHandler {

	@ExceptionHandler(ApiException.class)
	public ResponseEntity<ApiError> handleApi(ApiException ex) {
		return error(ex.getStatus(), ex.getMessage(), List.of());
	}

	@ExceptionHandler(MethodArgumentNotValidException.class)
	public ResponseEntity<ApiError> handleValidation(MethodArgumentNotValidException ex) {
		List<String> details = ex.getBindingResult().getFieldErrors().stream()
				.map(this::formatField)
				.toList();
		return error(HttpStatus.BAD_REQUEST, "Validation failed", details);
	}

	@ExceptionHandler(DataIntegrityViolationException.class)
	public ResponseEntity<ApiError> handleConflict(DataIntegrityViolationException ex) {
		return error(HttpStatus.CONFLICT, "A matching record already exists", List.of());
	}

	@ExceptionHandler(BadCredentialsException.class)
	public ResponseEntity<ApiError> handleBadCredentials(BadCredentialsException ex) {
		return error(HttpStatus.UNAUTHORIZED, "Invalid email or password", List.of());
	}

	@ExceptionHandler(AuthorizationDeniedException.class)
	public ResponseEntity<ApiError> handleDenied(AuthorizationDeniedException ex) {
		return error(HttpStatus.FORBIDDEN, "You are not allowed to perform this action", List.of());
	}

	@ExceptionHandler(Exception.class)
	public ResponseEntity<ApiError> handleOther(Exception ex) {
		return error(HttpStatus.INTERNAL_SERVER_ERROR, "Unexpected server error", List.of());
	}

	private String formatField(FieldError error) {
		return error.getField() + ": " + error.getDefaultMessage();
	}

	private ResponseEntity<ApiError> error(HttpStatus status, String message, List<String> details) {
		ApiError body = new ApiError(Instant.now(), status.value(), status.getReasonPhrase(), message, details);
		return ResponseEntity.status(status).body(body);
	}
}
