package com.segg3r.expensetracker.user;

import com.segg3r.expensetracker.security.UsernamePassword;
import com.segg3r.expensetracker.user.exception.UserRegistrationException;

public interface UserRegistrationService {
	User registerUser(UsernamePassword usernamePassword) throws UserRegistrationException;
	void unregisterUser(User user);
}
