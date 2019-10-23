package com.segg3r.expensetracker.security;

import com.segg3r.expensetracker.MockitoTest;
import com.segg3r.expensetracker.security.exception.UserAuthenticationException;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.*;

public class SecurityContextTest extends MockitoTest {

	private static final String LOGIN = "login";
	private static final String PASSWORD = "password";

	@Mock
	private AuthenticationManager authenticationManager;
	@InjectMocks
	private SecurityContextImpl securityContext;

	@BeforeClass
	public void initMockedMethods() {
		securityContext = spy(securityContext);
		doNothing().when(securityContext).setAuthentication(any(Authentication.class));
	}

	@Test(description = "should successfully login the user and redirect to login page")
	public void testLogin_Success() throws UserAuthenticationException {
		Authentication authentication = givenSuccessfulAuthentication();
		tryToAuthenticate();

		verify(securityContext).setAuthentication(authentication);
	}

	@Test(description = "should throw UserAuthenticationException, if login or password is incorrect",
			expectedExceptions = UserAuthenticationException.class)
	public void testLogin_WrongLogin() throws UserAuthenticationException {
		givenFailedAuthentication();
		tryToAuthenticate();
	}

	private void tryToAuthenticate() throws UserAuthenticationException {
		UsernamePassword usernamePassword = givenUsernamePassword();
		this.securityContext.authenticate(usernamePassword);
	}

	private UsernamePassword givenUsernamePassword() {
		MultiValueMap<String, String> map = new LinkedMultiValueMap<>();
		map.add("username", LOGIN);
		map.add("password", PASSWORD);

		return new UsernamePassword(map);
	}

	private Authentication givenSuccessfulAuthentication() {
		Authentication authentication = new UsernamePasswordAuthenticationToken(LOGIN, PASSWORD);
		when(authenticationManager.authenticate(eq(authentication))).thenReturn(authentication);

		return authentication;
	}

	private void givenFailedAuthentication() {
		Authentication successfulAuthentication = new UsernamePasswordAuthenticationToken(LOGIN, PASSWORD);
		when(authenticationManager.authenticate(eq(successfulAuthentication)))
				.thenThrow(new BadCredentialsException("Bad credentials"));
	}

}