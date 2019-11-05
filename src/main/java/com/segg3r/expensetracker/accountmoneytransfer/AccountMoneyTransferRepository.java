package com.segg3r.expensetracker.accountmoneytransfer;

import org.springframework.data.mongodb.repository.MongoRepository;

public interface AccountMoneyTransferRepository extends MongoRepository<AccountMoneyTransfer, String> {
}
