package com.segg3r.expensetracker.user;

import com.segg3r.expensetracker.security.UsernamePassword;
import com.segg3r.expensetracker.security.exception.UserRegistrationException;

public interface UserService {

	void register(UsernamePassword usernamePassword) throws UserRegistrationException;

}
