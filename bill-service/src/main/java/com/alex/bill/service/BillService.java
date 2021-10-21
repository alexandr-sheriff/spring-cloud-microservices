package com.alex.bill.service;

import com.alex.bill.entity.Bill;
import com.alex.bill.exception.ResourceNotFoundException;
import com.alex.bill.repository.BillRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.List;

@Service
public class BillService {

    private final BillRepository billRepository;

    @Autowired
    public BillService(BillRepository billRepository) {
        this.billRepository = billRepository;
    }

    public Bill getBillById(Long billId) {
        return billRepository.findById(billId).orElseThrow(() -> new ResourceNotFoundException("Unable to find bill with id: " + billId));
    }

//    public Long createBill(Long accountId, BigDecimal amount, Boolean isDefault, Boolean overdraftEnabled) {
//        Bill bill = new Bill(accountId, amount, isDefault, OffsetDateTime.now(), overdraftEnabled);
//        return billRepository.save(bill).getBillId();
//    }

    public Bill createBill(Long accountId, BigDecimal amount, Boolean isDefault, Boolean overdraftEnabled) {
        Bill bill = new Bill(accountId, amount, isDefault, OffsetDateTime.now(), overdraftEnabled);
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
    }

    public List<Bill> getBillsByAccountId(Long accountId) {
        return billRepository.getBillsByAccountId(accountId);
    }
}
