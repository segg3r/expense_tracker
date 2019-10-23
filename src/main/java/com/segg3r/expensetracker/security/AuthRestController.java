package com.segg3r.expensetracker.security;

import com.segg3r.expensetracker.security.exception.UserAuthenticationException;
import com.segg3r.expensetracker.security.exception.UserInputValidationException;
import com.segg3r.expensetracker.security.exception.UserRegistrationException;
import com.segg3r.expensetracker.user.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Slf4j
@RestController
@RequestMapping("/api/auth")
public class AuthRestController {

	@Autowired
	private SecurityContext securityContext;
	@Autowired
	private UserService userService;

	@RequestMapping(value = "/login", method = RequestMethod.POST, consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
	public void login(HttpServletResponse response, @RequestParam MultiValueMap<String, String> map) {
		try {
			UsernamePassword usernamePassword = new UsernamePassword(map).validate();
			authenticate(usernamePassword, response);
		} catch (UserInputValidationException | UserAuthenticationException e) {
			log.warn(e.getMessage(), e);
			throw new HttpClientErrorException(HttpStatus.BAD_REQUEST, e.getMessage());
		}
	}

	@RequestMapping(value = "/login", method = RequestMethod.POST, consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
	public void register(HttpServletResponse response, @RequestParam MultiValueMap<String, String> map) {
		try {
			UsernamePassword usernamePassword = new UsernamePassword(map).validate();
			userService.register(usernamePassword);
			authenticate(usernamePassword, response);
		} catch (UserInputValidationException | UserRegistrationException e) {
			log.warn(e.getMessage(), e);
			throw new HttpClientErrorException(HttpStatus.BAD_REQUEST, e.getMessage());
		} catch (UserAuthenticationException e) {
			log.error(e.getMessage(), e);
			throw new HttpServerErrorException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
		}
	}

	private void authenticate(UsernamePassword usernamePassword, HttpServletResponse response)
			throws UserAuthenticationException {
		try {
			securityContext.authenticate(usernamePassword);
			response.sendRedirect("/home");
		} catch (IOException e) {
			throw new UserAuthenticationException("Could not redirect user to /home page.");
		}
	}
}