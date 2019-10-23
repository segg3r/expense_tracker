package com.segg3r.expensetracker.security;

import org.springframework.security.core.Authentication;

public interface SecurityContext {

	void setAuthentication(Authentication authentication);

}
