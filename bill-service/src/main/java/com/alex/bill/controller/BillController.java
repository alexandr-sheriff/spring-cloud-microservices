package com.alex.bill.controller;

import com.alex.bill.controller.dto.BillRequestDTO;
import com.alex.bill.controller.dto.BillResponseDTO;
import com.alex.bill.entity.Bill;
import com.alex.bill.service.BillService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

@RestController
public class BillController {

    private final BillService billService;

    @Autowired
    public BillController(BillService billService) {
        this.billService = billService;
    }

    @GetMapping("/{billId}")
    public ResponseEntity<BillResponseDTO> getBill(@PathVariable Long billId) {
        return new ResponseEntity<>(
                new BillResponseDTO(billService.getBillById(billId)),
                HttpStatus.OK);
    }

//    @PostMapping("/")
//    public Long createBill(@RequestBody BillRequestDTO billRequestDTO) {
//        return billService.createBill(
//                billRequestDTO.getAccountId(),
//                billRequestDTO.getAmount(),
//                billRequestDTO.getIsDefault(),
//                billRequestDTO.getOverdraftEnabled());
//    }

    @PostMapping("/")
    public ResponseEntity<BillResponseDTO> createBill(@Valid @RequestBody BillRequestDTO billRequestDTO) {
        return new ResponseEntity<>(
                new BillResponseDTO(billService.createBill(
                        billRequestDTO.getAccountId(),
                        billRequestDTO.getAmount(),
                        billRequestDTO.getIsDefault(),
                        billRequestDTO.getOverdraftEnabled())),
                HttpStatus.CREATED);
    }

    @PutMapping("/{billId}")
    public ResponseEntity<BillResponseDTO> updateBill(@PathVariable Long billId,
                                      @Valid @RequestBody BillRequestDTO billRequestDTO) {
        return new ResponseEntity<>(
                new BillResponseDTO(billService.updateBill(
                        billId,
                        billRequestDTO.getAccountId(),
                        billRequestDTO.getAmount(),
                        billRequestDTO.getIsDefault(),
                        billRequestDTO.getOverdraftEnabled())),
                HttpStatus.OK);
    }

    @DeleteMapping("/{billId}")
    public ResponseEntity<Void> deleteBill(@PathVariable Long billId) {
//        return new BillResponseDTO(billService.deleteBill(billId));
        billService.deleteBill(billId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/accounts/{accountId}")
    public ResponseEntity<List<BillResponseDTO>> getBillsByAccountId(@PathVariable Long accountId) {
        return new ResponseEntity<>(
                billService.getBillsByAccountId(accountId).stream().map(BillResponseDTO::new).collect(Collectors.toList()),
                HttpStatus.OK);
    }
}
