package com.segg3r.expensetracker.security;

import com.segg3r.expensetracker.exception.InternalException;
import com.segg3r.expensetracker.user.User;
import com.segg3r.expensetracker.user.UserRegistrationService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Slf4j
@RestController
@RequestMapping("/api/auth")
public class AuthRestController {

	@Autowired
	private SecurityContext securityContext;
	@Autowired
	private UserRegistrationService userRegistrationService;

	@RequestMapping(value = "/login", method = RequestMethod.POST, consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
	public void login(HttpServletResponse response, @RequestParam MultiValueMap<String, String> map) {
		UsernamePassword usernamePassword = new UsernamePassword(map).validate();
		authenticate(usernamePassword, response);
	}

	@RequestMapping(value = "/register", method = RequestMethod.POST, consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
	public User register(HttpServletResponse response, @RequestParam MultiValueMap<String, String> map) {
		UsernamePassword usernamePassword = new UsernamePassword(map).validate();
		User user = userRegistrationService.registerUser(usernamePassword);
		authenticate(usernamePassword, response);

		return user;
	}

	private void authenticate(UsernamePassword usernamePassword, HttpServletResponse response) {
		try {
			securityContext.authenticate(usernamePassword);
			response.sendRedirect("/home");
		} catch (IOException e) {
			throw new InternalException("Could not redirect user to /home page.");
		}
	}

}