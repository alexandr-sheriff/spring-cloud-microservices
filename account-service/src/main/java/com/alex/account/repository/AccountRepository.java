package com.alex.account.repository;

import com.alex.account.entity.Account;
import org.springframework.data.repository.CrudRepository;

public interface AccountRepository extends CrudRepository<Account, Long> {
    Account getAccountByBillsContaining(Long billId);
}
