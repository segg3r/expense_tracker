package com.segg3r.expensetracker.security;

import com.segg3r.expensetracker.user.User;
import com.segg3r.expensetracker.user.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

public class UserDetailsMongoService implements UserDetailsService {

	@Autowired
	private UserRepository userRepository;

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		User user = userRepository.findByName(username)
				.orElseThrow(() -> new UsernameNotFoundException("Could not find user by name: " + username));

		return new org.springframework.security.core.userdetails.User(
				user.getName(), user.getPassword(), user.getSimpleGrantedAuthorities());
	}

}
