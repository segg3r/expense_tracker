package com.segg3r.expensetracker.account;

import org.springframework.data.mongodb.repository.MongoRepository;

public interface AccountMoneyTransferRepository extends MongoRepository<AccountMoneyTransfer, String> {
}
