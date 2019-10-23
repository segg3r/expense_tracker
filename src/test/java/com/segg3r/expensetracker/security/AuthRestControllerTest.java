package com.segg3r.expensetracker.security;

import com.segg3r.expensetracker.MockitoTest;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.HttpClientErrorException;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import static com.segg3r.expensetracker.MvcTestUtils.givenMockedResponse;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class AuthRestControllerTest extends MockitoTest {

	@Mock
	private AuthenticationManager authenticationManager;
	@Mock
	private SecurityContext securityContext;
	@InjectMocks
	private AuthRestController controller;

	private HttpServletResponse response;

	@Test(description = "should successfully login the user and redirect to login page")
	public void testLogin_Success() throws IOException {
		Authentication authentication = givenSuccessfullAuthentication("login", "password");
		tryLogin("login", "password");

		verify(securityContext).setAuthentication(authentication);
		verify(this.response).sendRedirect("/home");
	}

	@Test(description = "should return 400, if login or password is incorrect",
		expectedExceptions = HttpClientErrorException.class)
	public void testLogin_WrongLogin() throws IOException {
		givenFailedAuthentication("login", "password");
		tryLogin("login", "password");
	}

	@Test(description = "should return 400, if login is empty",
		expectedExceptions = HttpClientErrorException.class)
	public void testLogin_EmptyLogin() throws IOException {
		tryLogin(null, "password");
	}

	@Test(description = "should return 400, if password is empty",
			expectedExceptions = HttpClientErrorException.class)
	public void testLogin_EmptyPassword() throws IOException {
		tryLogin("login", null);
	}

	private void tryLogin(String login, String password) throws IOException {
		this.response = givenMockedResponse();
		MultiValueMap<String, String> authData = givenAuthenticationData(login, password);

		controller.login(this.response, authData);
	}

	private Authentication givenSuccessfullAuthentication(String login, String password) {
		Authentication authentication = new UsernamePasswordAuthenticationToken(login, password);
		when(authenticationManager.authenticate(eq(authentication))).thenReturn(authentication);

		return authentication;
	}

	private void givenFailedAuthentication(String login, String password) {
		Authentication successfulAuthentication = new UsernamePasswordAuthenticationToken(login, password);
		when(authenticationManager.authenticate(eq(successfulAuthentication)))
				.thenThrow(new BadCredentialsException("Bad credentials"));
	}

	private MultiValueMap<String, String> givenAuthenticationData(String username, String password) {
		MultiValueMap<String, String> result = new LinkedMultiValueMap<>();
		result.add("username", username);
		result.add("password", password);

		return result;
	}

}