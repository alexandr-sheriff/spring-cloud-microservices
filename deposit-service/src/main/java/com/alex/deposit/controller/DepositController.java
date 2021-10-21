package com.alex.deposit.controller;

import com.alex.deposit.controller.dto.DepositRequestDTO;
import com.alex.deposit.controller.dto.DepositResponseDTO;
import com.alex.deposit.service.DepositService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
public class DepositController {

    private final DepositService depositService;

    @Autowired
    public DepositController(DepositService depositService) {
        this.depositService = depositService;
    }

    @PostMapping("/")
    public ResponseEntity<DepositResponseDTO> deposit(@Valid @RequestBody DepositRequestDTO requestDTO) {
        return new ResponseEntity<>(
                depositService.deposit(requestDTO.getAccountId(), requestDTO.getBillId(), requestDTO.getAmount()),
                HttpStatus.OK);
    }
}
