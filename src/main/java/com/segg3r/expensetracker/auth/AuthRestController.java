package com.segg3r.expensetracker.auth;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.HttpClientErrorException;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@RestController
@RequestMapping("/api/auth")
public class AuthRestController {

	@Autowired
	private AuthenticationManager authenticationManager;

	@RequestMapping(value = "/login", method = RequestMethod.POST,
			consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
	public void login(HttpServletResponse response, @RequestParam MultiValueMap<String, String> map) throws IOException {
		String username = map.getFirst("username");
		String password = map.getFirst("password");

		if (username == null || password == null) {
			throw new HttpClientErrorException(HttpStatus.BAD_REQUEST, "Username or password is not provided.");
		}

		Authentication authentication = new UsernamePasswordAuthenticationToken(username, password);
		try {
			authenticationManager.authenticate(authentication);
		} catch (AuthenticationException e) {
			throw new HttpClientErrorException(HttpStatus.BAD_REQUEST, "Username or password is not correct.");
		}

		SecurityContextHolder.getContext().setAuthentication(authentication);
		response.sendRedirect("/#/home");
	}

}
