package com.segg3r.expensetracker.user;

import com.segg3r.expensetracker.security.UsernamePassword;
import com.segg3r.expensetracker.security.exception.UserCreationException;

import java.util.Optional;

public interface UserService {

	User createUser(UsernamePassword usernamePassword) throws UserCreationException;
	boolean userExists(String username);
	Optional<User> findByName(String username);

}
