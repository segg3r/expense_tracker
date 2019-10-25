package com.segg3r.expensetracker.user;

import com.segg3r.expensetracker.security.UsernamePassword;
import com.segg3r.expensetracker.security.exception.UserRegistrationException;

public interface UserService {

	User createUser(UsernamePassword usernamePassword) throws UserRegistrationException;

	boolean userExists(String username);
}
