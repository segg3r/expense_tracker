package com.segg3r.expensetracker.exception;

import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@ControllerAdvice
public class HttpExceptionHandler extends ResponseEntityExceptionHandler {

	@ExceptionHandler(value = HttpStatusCodeException.class)
	public void handle(HttpStatusCodeException e, HttpServletResponse response) throws IOException {
		response.sendError(e.getStatusCode().value(), e.getMessage());
	}

}
