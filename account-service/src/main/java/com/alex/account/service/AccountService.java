package com.alex.account.service;

import com.alex.account.entity.Account;
import com.alex.account.exception.AccountNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.alex.account.repository.AccountRepository;

import java.time.OffsetDateTime;
import java.util.List;

@Service
public class AccountService {

    private final AccountRepository accountRepository;

    @Autowired
    public AccountService(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }

    public Account getAccountById(Long accountId) {
        return accountRepository.findById(accountId)
                .orElseThrow(() -> new AccountNotFoundException("Unable to find account with id: " + accountId));
    }

//    public Long createAccount(String name, String email, String phone, List<Long> bills) {
//        Account account = new Account(name, email, phone, OffsetDateTime.now(), bills);
//        return accountRepository.save(account).getAccountId();
//    }

    public Account createAccount(String name, String email, String phone, List<Long> bills) {
        Account account = new Account(name, email, phone, OffsetDateTime.now(), bills);
        return accountRepository.save(account);
    }

    public Account updateAccount(Long accountId, String name, String email, String phone, List<Long> bills) {
        Account account = new Account(accountId, name, email, phone, bills);
        return accountRepository.save(account);
    }

//    public Account deleteAccount(Long accountId) {
//        Account deletedAccount = getAccountById(accountId);
//        accountRepository.deleteById(accountId);
//        return deletedAccount;
//    }

    public void deleteAccount(Long accountId) {
        accountRepository.deleteById(accountId);
    }

}
