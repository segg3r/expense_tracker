package com.segg3r.expensetracker.auth;

import com.segg3r.expensetracker.user.MongoUser;
import com.segg3r.expensetracker.user.UserMongoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

public class UserDetailsMongoService implements UserDetailsService {

	@Autowired
	private UserMongoRepository userMongoRepository;

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		MongoUser mongoUser = userMongoRepository.findByName(username)
				.orElseThrow(() -> new UsernameNotFoundException("Could not find user by name: " + username));

		return new User(mongoUser.getName(), mongoUser.getPassword(), mongoUser.getSimpleGrantedAuthorities());
	}

}
