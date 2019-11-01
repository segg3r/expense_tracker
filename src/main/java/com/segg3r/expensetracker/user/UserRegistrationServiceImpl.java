package com.segg3r.expensetracker.user;

import com.segg3r.expensetracker.account.AccountService;
import com.segg3r.expensetracker.account.exception.AccountCreationException;
import com.segg3r.expensetracker.security.UsernamePassword;
import com.segg3r.expensetracker.security.exception.UserCreationException;
import com.segg3r.expensetracker.security.exception.UserRegistrationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;

@Slf4j
@Service
public class UserRegistrationServiceImpl implements UserRegistrationService {

	@Autowired
	private UserService userService;
	@Autowired
	private AccountService accountService;
	@Autowired
	private UserRepository userRepository;

	@PostConstruct
	void initAdmin() throws UserRegistrationException {
		userRepository.findByName("admin")
				.ifPresent(user -> userRepository.deleteById(user.getId()));

		UsernamePassword usernamePassword = new UsernamePassword("admin", "admin");
		this.registerUser(usernamePassword);
	}

	@Transactional
	@Override
	public User registerUser(UsernamePassword usernamePassword) throws UserRegistrationException {
		try {
			String username = usernamePassword.getUsername();
			log.info("Attempting to register user '" + username + "'.");

			userService.createUser(usernamePassword);

			User createdUser = userService.findByName(usernamePassword.getUsername())
					.orElseThrow(() -> new UserRegistrationException("Could not find created user."));
			accountService.createDefaultUserAccounts(createdUser);

			log.info("Successfully registered user '" + username + "'.");
			return createdUser;
		} catch (UserCreationException e) {
			throw new UserRegistrationException("User could not be created during registration.", e);
		} catch (AccountCreationException e) {
			throw new UserRegistrationException("Could not create account during user registration.", e);
		}
	}

}
