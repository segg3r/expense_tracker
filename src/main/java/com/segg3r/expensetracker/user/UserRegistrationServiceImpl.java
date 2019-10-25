package com.segg3r.expensetracker.user;

import com.segg3r.expensetracker.security.UsernamePassword;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;

@Slf4j
@Service
public class UserRegistrationServiceImpl implements UserRegistrationService {

	@Autowired
	private UserService userService;
	@Autowired
	private UserRepository userRepository;

	@PostConstruct
	void initAdmin() {
		userRepository.findByName("admin")
				.ifPresent(user -> userRepository.deleteById(user.getId()));

		UsernamePassword usernamePassword = new UsernamePassword("admin", "admin");
		this.registerUser(usernamePassword);
	}

	@Override
	public User registerUser(UsernamePassword usernamePassword) {
		
	}

}
