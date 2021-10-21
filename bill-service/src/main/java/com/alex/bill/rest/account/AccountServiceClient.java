package com.alex.bill.rest.account;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.List;

@FeignClient(name = "account-service")
public interface AccountServiceClient {

    @RequestMapping(value = "/accounts/{accountId}", method = RequestMethod.GET)
    AccountResponseDTO getAccountById(@PathVariable Long accountId);

    @RequestMapping(value = "/accounts/{accountId}", method = RequestMethod.PUT)
    AccountResponseDTO updateAccountById(@PathVariable Long accountId, @RequestBody AccountResponseDTO accountResponseDTO);

    @RequestMapping(value = "/accounts/bills/{billId}", method = RequestMethod.GET)
    AccountResponseDTO getAccountByBillId(@PathVariable Long billId);
}
