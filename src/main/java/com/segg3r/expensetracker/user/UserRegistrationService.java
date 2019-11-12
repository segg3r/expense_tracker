package com.segg3r.expensetracker.user;

import com.segg3r.expensetracker.security.UsernamePassword;

public interface UserRegistrationService {
	User registerUser(UsernamePassword usernamePassword);
	void unregisterUser(User user);
}
