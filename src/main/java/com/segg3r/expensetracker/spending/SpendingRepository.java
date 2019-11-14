package com.segg3r.expensetracker.spending;

import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface SpendingRepository extends MongoRepository<Spending, String> {
	void deleteByUserId(String id);
	List<SpendingCategory> findByUserId(String userId);
}
