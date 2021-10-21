package com.alex.bill.service;

import com.alex.bill.entity.Bill;
import com.alex.bill.exception.ResourceNotFoundException;
import com.alex.bill.repository.BillRepository;
import com.alex.bill.rest.account.AccountResponseDTO;
import com.alex.bill.rest.account.AccountServiceClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.List;

@Service
public class BillService {

    private final BillRepository billRepository;

    private final AccountServiceClient accountServiceClient;

    @Autowired
    public BillService(BillRepository billRepository, AccountServiceClient accountServiceClient) {
        this.billRepository = billRepository;
        this.accountServiceClient = accountServiceClient;
    }

    public Bill getBillById(Long billId) {
        return billRepository.findById(billId).orElseThrow(() -> new ResourceNotFoundException("Unable to find bill with id: " + billId));
    }

//    public Long createBill(Long accountId, BigDecimal amount, Boolean isDefault, Boolean overdraftEnabled) {
//        Bill bill = new Bill(accountId, amount, isDefault, OffsetDateTime.now(), overdraftEnabled);
//        return billRepository.save(bill).getBillId();
//    }

    public Bill createBill(Long accountId, BigDecimal amount, Boolean isDefault, Boolean overdraftEnabled) {
        AccountResponseDTO accountResponseDTO = accountServiceClient.getAccountById(accountId);
        Bill bill = new Bill(accountId, amount, isDefault, OffsetDateTime.now(), overdraftEnabled);
        Bill save = billRepository.save(bill);
        accountResponseDTO.getBills().add(save.getBillId());
        accountServiceClient.updateAccountById(accountId, accountResponseDTO);
        return billRepository.save(bill);
    }

    public Bill updateBill(Long billId, Long accountId, BigDecimal amount, Boolean isDefault, Boolean overdraftEnabled) {
        Bill bill = new Bill(billId, accountId, amount, isDefault, overdraftEnabled);
        return billRepository.save(bill);
    }

//    public Bill deleteBill(Long billId) {
//        Bill deletedBill = getBillById(billId);
//        billRepository.deleteById(billId);
//        return deletedBill;
//    }

    public void deleteBill(Long billId) {
        billRepository.deleteById(billId);
        AccountResponseDTO accountResponseDTO = accountServiceClient.getAccountByBillId(billId);
        accountResponseDTO.getBills().remove(billId);
        accountServiceClient.updateAccountById(accountResponseDTO.getAccountId(), accountResponseDTO);
    }

    public List<Bill> getBillsByAccountId(Long accountId) {
        return billRepository.getBillsByAccountId(accountId);
    }
}
