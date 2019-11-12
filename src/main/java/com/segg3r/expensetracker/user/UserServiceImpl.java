package com.segg3r.expensetracker.user;

import com.segg3r.expensetracker.exception.InputException;
import com.segg3r.expensetracker.security.UsernamePassword;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {

	@Autowired
	private UserRepository userRepository;
	@Autowired
	private PasswordEncoder passwordEncoder;

	@Override
	public User createUser(UsernamePassword usernamePassword) {
		String username = usernamePassword.getUsername();
		String password = usernamePassword.getPassword();

		if (userExists(username)) {
			throw new InputException("User with username '" + username + "' already exists.");
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

	@Override
	public Optional<User> findByName(String username) {
		return userRepository.findByName(username);
	}

	@Override
	public void deleteUser(User user) {
		userRepository.deleteById(user.getId());
	}

}
