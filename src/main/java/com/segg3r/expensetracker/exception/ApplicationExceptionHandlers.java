package com.segg3r.expensetracker.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@ControllerAdvice
@Slf4j
public class ApplicationExceptionHandlers extends ResponseEntityExceptionHandler {

	@ExceptionHandler(value = HttpStatusCodeException.class)
	public void handleHttpException(HttpStatusCodeException e, HttpServletResponse response) throws IOException {
		response.sendError(e.getStatusCode().value(), e.getMessage());
	}

	@ExceptionHandler(value = InputException.class)
	public void handleUserInputException(InputException e, HttpServletResponse response) throws IOException {
		log.warn("User input exception occurred.", e);
		response.sendError(HttpStatus.BAD_REQUEST.value(), e.getMessage());
	}

	@ExceptionHandler(value = InputException.class)
	public void handleAuthenticationException(AuthenticationException e, HttpServletResponse response) throws IOException {
		log.warn("Authentication exception occurred.", e);
		response.sendError(HttpStatus.UNAUTHORIZED.value(), e.getMessage());
	}

	@ExceptionHandler(value = PermissionException.class)
	public void handlePermissionException(PermissionException e, HttpServletResponse response) throws IOException {
		log.warn("Permission exception occurred.", e);
		response.sendError(HttpStatus.FORBIDDEN.value(), e.getMessage());
	}

	@ExceptionHandler(value = InternalException.class)
	public void handleInternalException(InternalException e, HttpServletResponse response) throws IOException {
		log.error("Internal exception occurred.", e);
		response.sendError(HttpStatus.INTERNAL_SERVER_ERROR.value(), e.getMessage());
	}


}
