package com.alex.deposit.utils;

import com.alex.deposit.rest.account.AccountResponseDTO;
import com.alex.deposit.rest.bill.BillRequestDTO;
import com.alex.deposit.rest.bill.BillResponseDTO;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.Arrays;

public class Utils {

    public static AccountResponseDTO createAccountResponseDTO() {
        AccountResponseDTO accountResponseDTO = new AccountResponseDTO();
        accountResponseDTO.setAccountId(1L);
        accountResponseDTO.setName("Alex");
        accountResponseDTO.setEmail("alex@gmail.com");
        accountResponseDTO.setPhone("+123456789");
        accountResponseDTO.setCreationDate(OffsetDateTime.now());
        accountResponseDTO.setBills(Arrays.asList(1L, 2L, 3L));
        return accountResponseDTO;
    }

    public static BillResponseDTO createBillResponseDTO() {
        BillResponseDTO billResponseDTO = new BillResponseDTO();
        billResponseDTO.setBillId(1L);
        billResponseDTO.setAccountId(1L);
        billResponseDTO.setAmount(BigDecimal.valueOf(1000));
        billResponseDTO.setIsDefault(true);
        billResponseDTO.setCreationDate(OffsetDateTime.now());
        billResponseDTO.setOverdraftEnabled(true);
        return billResponseDTO;
    }

    public static BillRequestDTO createBillRequestDTO(BigDecimal amount) {
        BillRequestDTO billRequestDTO = new BillRequestDTO();
        billRequestDTO.setAccountId(createBillResponseDTO().getAccountId());
        billRequestDTO.setIsDefault(createBillResponseDTO().getIsDefault());
        billRequestDTO.setCreationDate(createBillResponseDTO().getCreationDate());
        billRequestDTO.setOverdraftEnabled(createBillResponseDTO().getOverdraftEnabled());
        billRequestDTO.setAmount(createBillResponseDTO().getAmount().add(amount));
        return billRequestDTO;
    }

}
