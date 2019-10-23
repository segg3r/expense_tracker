package com.segg3r.expensetracker.security;

import com.segg3r.expensetracker.security.exception.UserAuthenticationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class SecurityContextImpl implements SecurityContext {

	@Autowired
	private AuthenticationManager authenticationManager;

	@Override
	public void authenticate(UsernamePassword usernamePassword) throws UserAuthenticationException {
		String username = usernamePassword.getUsername();
		String password = usernamePassword.getPassword();

		log.info("Attempting to authenticate user '" + username + "'.");
		Authentication authentication = new UsernamePasswordAuthenticationToken(username, password);
		try {
			authentication = authenticationManager.authenticate(authentication);
		} catch (AuthenticationException e) {
			log.warn("Could not authenticate user '" + username + "'. Username or password is not correct.", e);
			throw new UserAuthenticationException("Username or password is not correct.");
		}

		log.info("Successfully authentication user " + usernamePassword.getUsername());
		this.setAuthentication(authentication);
	}

	protected void setAuthentication(Authentication authentication) {
		SecurityContextHolder.getContext().setAuthentication(authentication);
	}

}
