package com.segg3r.expensetracker.user;

import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface UserMongoRepository extends MongoRepository<MongoUser, ObjectId> {
	Optional<MongoUser> findByName(String name);
}
