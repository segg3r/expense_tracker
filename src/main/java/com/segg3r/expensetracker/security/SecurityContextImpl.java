package com.segg3r.expensetracker.security;

import com.segg3r.expensetracker.security.exception.UserAuthenticationException;
import com.segg3r.expensetracker.user.User;
import com.segg3r.expensetracker.user.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class SecurityContextImpl implements SecurityContext {

	@Autowired
	private AuthenticationManager authenticationManager;
	@Autowired
	private UserRepository userRepository;

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

		log.info("Successfully authenticated user '" + usernamePassword.getUsername() + "'.");
		this.setAuthentication(authentication);
	}

	@Override
	public User getCurrentUser() {
		Object principal = getAuthentication().getPrincipal();
		if (!(principal instanceof UserDetails))
			throw new IllegalArgumentException("Could not get current user from context. Principal is not instance of UserDetails.");

		String username = ((UserDetails) principal).getUsername();
		return userRepository.findByName(username).orElseThrow(() ->
				new IllegalArgumentException("Could not get current user from context. Could not find user by name '" + username + "'."));
	}

	protected void setAuthentication(Authentication authentication) {
		SecurityContextHolder.getContext().setAuthentication(authentication);
	}

	protected Authentication getAuthentication() {
		return SecurityContextHolder.getContext().getAuthentication();
	}

}
