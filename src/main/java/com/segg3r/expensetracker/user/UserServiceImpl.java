package com.segg3r.expensetracker.user;

import com.segg3r.expensetracker.security.UsernamePassword;
import com.segg3r.expensetracker.security.exception.UserRegistrationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;

@Service
public class UserServiceImpl implements UserService {

	@Autowired
	private UserRepository userRepository;
	@Autowired
	private PasswordEncoder passwordEncoder;

	@Override
	public User createUser(UsernamePassword usernamePassword) throws UserRegistrationException {
		String username = usernamePassword.getUsername();
		String password = usernamePassword.getPassword();

		if (userExists(username)) {
			throw new UserRegistrationException("User with username '" + username + "' already exists.");
		}

		User user = User.builder()
				.name(username)
				.password(passwordEncoder.encode(password))
				.build();
		return userRepository.insert(user);
	}

	@Override
	public boolean userExists(String username) {
		return userRepository.findByName(username).isPresent();
	}

}
