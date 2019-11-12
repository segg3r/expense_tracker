package com.segg3r.expensetracker.spending;

import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;
import java.util.Optional;

public interface SpendingCategoryRepository extends MongoRepository<SpendingCategory, String> {
	Optional<SpendingCategory> findByUserIdAndName(String userId, String name);
	void deleteByUserId(String id);
	List<SpendingCategory> findByUserId(String userId);
}
