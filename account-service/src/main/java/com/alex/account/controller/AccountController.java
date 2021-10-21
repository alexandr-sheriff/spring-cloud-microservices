package com.alex.account.controller;

import com.alex.account.controller.dto.AccountRequestDTO;
import com.alex.account.controller.dto.AccountResponseDTO;
import com.alex.account.entity.Account;
import com.alex.account.service.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
public class AccountController {

    private final AccountService accountService;

    @Autowired
    public AccountController(AccountService accountService) {
        this.accountService = accountService;
    }

    @GetMapping("/{accountId}")
    public ResponseEntity<AccountResponseDTO> getAccount(@PathVariable Long accountId) {
        return new ResponseEntity<>(
                new AccountResponseDTO(accountService.getAccountById(accountId)),
                HttpStatus.OK);
    }

    @PostMapping("/")
    public ResponseEntity<AccountResponseDTO> createAccount(@Valid @RequestBody AccountRequestDTO accountRequestDTO) {
        return new ResponseEntity<>(
                new AccountResponseDTO(accountService.createAccount(
                        accountRequestDTO.getName(),
                        accountRequestDTO.getEmail(),
                        accountRequestDTO.getPhone(),
                        accountRequestDTO.getBills())),
                HttpStatus.CREATED);
    }

    @PutMapping("/{accountId}")
    public ResponseEntity<AccountResponseDTO> updateAccount(@PathVariable Long accountId,
                                                            @Valid @RequestBody AccountRequestDTO accountRequestDTO) {
        return new ResponseEntity<>(
                new AccountResponseDTO(accountService.updateAccount(
                        accountId,
                        accountRequestDTO.getName(),
                        accountRequestDTO.getEmail(),
                        accountRequestDTO.getPhone(),
                        accountRequestDTO.getBills())),
                HttpStatus.OK);
    }

    @DeleteMapping("/{accountId}")
    public ResponseEntity<Void> deleteAccount(@PathVariable Long accountId) {
//        return ResponseEntity.noContent().build();
        accountService.deleteAccount(accountId);
        return ResponseEntity.noContent().build();
    }
}
