package com.segg3r.expensetracker.config;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.MongoDbFactory;
import org.springframework.data.mongodb.MongoTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;

@Configuration
public class MongoConfig {

	@Bean
	@ConditionalOnProperty(name = "spring.data.mongodb.transactional", havingValue = "true")
	PlatformTransactionManager transactionManager(MongoDbFactory mongoDbFactory) {
		return new MongoTransactionManager(mongoDbFactory);
	}

}
