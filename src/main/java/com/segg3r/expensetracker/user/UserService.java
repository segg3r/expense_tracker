package com.segg3r.expensetracker.user;

import com.segg3r.expensetracker.security.UsernamePassword;

import java.util.Optional;

public interface UserService {

	User createUser(UsernamePassword usernamePassword);
	boolean userExists(String username);
	Optional<User> findByName(String username);
	void deleteUser(User user);

}
