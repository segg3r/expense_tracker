package com.segg3r.expensetracker.security;

import com.segg3r.expensetracker.security.exception.UserInputValidationException;
import org.springframework.util.MultiValueMap;

import java.util.Objects;

public class UsernamePassword {
	private final String username;
	private final String password;

	public UsernamePassword(MultiValueMap<String, String> map) {
		this.username = map.getFirst("username");
		this.password = map.getFirst("password");
	}

	UsernamePassword validate() throws UserInputValidationException {
		if (username == null || password == null) {
			throw new UserInputValidationException("Username or password is not provided.");
		}

		return this;
	}

	public String getUsername() {
		return username;
	}

	public String getPassword() {
		return password;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		UsernamePassword that = (UsernamePassword) o;
		return Objects.equals(username, that.username) &&
				Objects.equals(password, that.password);
	}

	@Override
	public int hashCode() {
		return Objects.hash(username, password);
	}

}
