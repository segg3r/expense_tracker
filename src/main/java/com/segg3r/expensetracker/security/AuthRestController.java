package com.segg3r.expensetracker.security;

import com.segg3r.expensetracker.user.User;
import com.segg3r.expensetracker.user.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.HttpClientErrorException;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Collections;
import java.util.Optional;

@Slf4j
@RestController
@RequestMapping("/api/auth")
public class AuthRestController {

	@Autowired
	private AuthenticationManager authenticationManager;
	@Autowired
	private PasswordEncoder passwordEncoder;
	@Autowired
	private UserRepository userRepository;
	@Autowired
	private SecurityContext securityContext;

	@PostConstruct
	public void setupUsers() {
		Optional<User> existingAdmin = userRepository.findByName("admin");
		if (!existingAdmin.isPresent()) {
			User admin = User.builder()
					.name("admin")
					.password(passwordEncoder.encode("admin"))
					.authorities(Collections.singletonList("admin"))
					.build();
			userRepository.insert(admin);
		}
	}

	@RequestMapping(value = "/login", method = RequestMethod.POST, consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
	public void login(HttpServletResponse response, @RequestParam MultiValueMap<String, String> map) throws IOException {
		String username = map.getFirst("username");
		String password = map.getFirst("password");

		if (username == null || password == null) {
			throw new HttpClientErrorException(HttpStatus.BAD_REQUEST, "Username or password is not provided.");
		}

		log.info("Attempting to authenticate user '" + username + "'.");
		Authentication authentication = new UsernamePasswordAuthenticationToken(username, password);
		try {
			authentication = authenticationManager.authenticate(authentication);
		} catch (AuthenticationException e) {
			log.warn("Could not authenticate user '" + username + "'. Username or password is not correct.", e);
			throw new HttpClientErrorException(HttpStatus.BAD_REQUEST, "Username or password is not correct.");
		}

		securityContext.setAuthentication(authentication);

		log.info("Successfully authenticated user '" + username + "'.");
		response.sendRedirect("/home");
	}

}
