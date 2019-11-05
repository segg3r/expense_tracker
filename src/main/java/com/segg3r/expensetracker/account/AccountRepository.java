package com.segg3r.expensetracker.account;

import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;
import java.util.Optional;

public interface AccountRepository extends MongoRepository<Account, String> {
	Optional<Account> findByUserIdAndName(String userId, String name);
	void deleteByUserId(String userId);
	List<Account> findByUserId(String userId);
}