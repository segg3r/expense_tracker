package com.segg3r.expensetracker.income;

import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;
import java.util.Optional;

public interface IncomeCategoryRepository extends MongoRepository<IncomeCategory, String> {
	Optional<IncomeCategory> findByUserIdAndName(String userId, String name);
	void deleteByUserId(String id);
	List<IncomeCategory> findByUserId(String userId);
}
