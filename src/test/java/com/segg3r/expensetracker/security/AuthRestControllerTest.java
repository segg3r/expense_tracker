package com.segg3r.expensetracker.security;

import com.segg3r.expensetracker.MockitoTest;
import com.segg3r.expensetracker.security.exception.UserAuthenticationException;
import com.segg3r.expensetracker.security.exception.UserRegistrationException;
import com.segg3r.expensetracker.user.UserService;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.Test;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import static com.segg3r.expensetracker.MvcTestUtils.givenMockedResponse;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.*;

public class AuthRestControllerTest extends MockitoTest {

	private static final String LOGIN = "login";
	private static final String PASSWORD = "password";

	@Mock
	private SecurityContext securityContext;
	@Mock
	private UserService userService;
	@InjectMocks
	private AuthRestController controller;

	private HttpServletResponse response;

	@AfterMethod
	public void clearMocks() {
		Mockito.reset(securityContext);
	}

	@Test(description = "should successfully login the user and redirect to login page")
	public void testLogin_Success() throws IOException, UserAuthenticationException {
		givenSuccessfulAuthentication();
		tryToLogin();

		verifyAuthenticated();
		verifyRedirectedToHomePage();
	}

	@Test(description = "should return 400, if login or password is incorrect on login",
		expectedExceptions = HttpClientErrorException.class)
	public void testLogin_WrongLogin() throws UserAuthenticationException {
		givenFailedAuthentication();
		tryToLogin();
	}

	@Test(description = "should return 400, if login is empty on login",
		expectedExceptions = HttpClientErrorException.class)
	public void testLogin_EmptyLogin() {
		tryToLogin(null, PASSWORD);
	}

	@Test(description = "should return 400, if password is empty on login",
			expectedExceptions = HttpClientErrorException.class)
	public void testLogin_EmptyPassword() {
		tryToLogin(LOGIN, null);
	}

	@Test(description = "should successfully register")
	public void testRegister_Success() throws UserAuthenticationException, UserRegistrationException, IOException {
		givenSuccessfulAuthentication();
		givenSuccessfulRegistration();

		tryToRegister();

		verifyAuthenticated();
		verifyRedirectedToHomePage();
	}

	@Test(description = "should return 400, if login is empty on login",
			expectedExceptions = HttpClientErrorException.class)
	public void testRegister_EmptyLogin() {
		tryToRegister(null, PASSWORD);
	}

	@Test(description = "should return 400, if password is empty on login",
			expectedExceptions = HttpClientErrorException.class)
	public void testRegister_EmptyPassword() {
		tryToRegister(LOGIN, null);
	}

	@Test(description = "should return 400, if registration failed",
			expectedExceptions = HttpClientErrorException.class)
	public void testRegister_FailedRegistration() throws UserRegistrationException {
		givenFailedRegistration();
		tryToRegister();
	}

	@Test(description = "should return 500, if authentication is failed for some reason after successful registration",
			expectedExceptions = HttpServerErrorException.class)
	public void testRegister_FailedAuthentication() throws UserAuthenticationException, UserRegistrationException {
		givenSuccessfulRegistration();
		givenFailedAuthentication();

		tryToRegister();
	}

	private void tryToLogin() {
		this.tryToLogin(LOGIN, PASSWORD);
	}

	private void tryToLogin(String username, String password) {
		this.response = givenMockedResponse();
		controller.login(this.response, givenAuthenticationData(username, password));
	}

	private void tryToRegister() {
		this.tryToRegister(LOGIN, PASSWORD);
	}

	private void tryToRegister(String username, String password) {
		this.response = givenMockedResponse();
		controller.register(this.response, givenAuthenticationData(username, password));
	}

	private void givenSuccessfulAuthentication() throws UserAuthenticationException {
		UsernamePassword usernamePassword = givenUsernamePassword();
		doNothing().when(securityContext).authenticate(eq(usernamePassword));
	}

	private void givenFailedAuthentication() throws UserAuthenticationException {
		UsernamePassword usernamePassword = givenUsernamePassword();
		doThrow(new UserAuthenticationException()).when(securityContext).authenticate(eq(usernamePassword));
	}

	private void givenSuccessfulRegistration() throws UserRegistrationException {
		UsernamePassword usernamePassword = givenUsernamePassword();
		doNothing().when(userService).register(usernamePassword);
	}

	private void givenFailedRegistration() throws UserRegistrationException {
		UsernamePassword usernamePassword = givenUsernamePassword();
		doThrow(new UserRegistrationException()).when(userService).register(eq(usernamePassword));
	}

	private UsernamePassword givenUsernamePassword() {
		return new UsernamePassword(givenAuthenticationData(LOGIN, PASSWORD));
	}

	private MultiValueMap<String, String> givenAuthenticationData(String username, String password) {
		MultiValueMap<String, String> result = new LinkedMultiValueMap<>();
		result.add("username", username);
		result.add("password", password);

		return result;
	}

	private void verifyAuthenticated() throws UserAuthenticationException {
		verify(this.securityContext).authenticate(eq(givenUsernamePassword()));
	}

	private void verifyRedirectedToHomePage() throws IOException {
		verify(this.response).sendRedirect("/home");
	}

}