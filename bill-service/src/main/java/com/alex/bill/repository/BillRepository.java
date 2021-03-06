package com.alex.bill.repository;

import com.alex.bill.entity.Bill;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface BillRepository extends CrudRepository<Bill, Long> {
    List<Bill> getBillsByAccountId(Long accountId);
}
