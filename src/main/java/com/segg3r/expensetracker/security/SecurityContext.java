package com.segg3r.expensetracker.security;

import com.segg3r.expensetracker.user.User;

public interface SecurityContext {

	void authenticate(UsernamePassword usernamePassword);

	User getCurrentUser();

}
