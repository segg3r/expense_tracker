package com.segg3r.expensetracker.security;

import com.segg3r.expensetracker.security.exception.UserAuthenticationException;

public interface SecurityContext {

	void authenticate(UsernamePassword usernamePassword) throws UserAuthenticationException;

}
