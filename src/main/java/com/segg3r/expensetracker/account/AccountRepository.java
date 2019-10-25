package com.segg3r.expensetracker.account;

import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface AccountRepository extends MongoRepository<Account, ObjectId> {
	Optional<Account> findByUserIdAndName(ObjectId userId, String name);
}