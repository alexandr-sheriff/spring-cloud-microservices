package com.alex.deposit.service;

import com.alex.deposit.controller.dto.DepositResponseDTO;
import com.alex.deposit.entity.Deposit;
import com.alex.deposit.exception.DepositServiceException;
import com.alex.deposit.exception.ResourceNotFoundException;
import com.alex.deposit.repository.DepositRepository;
import com.alex.deposit.rest.account.AccountResponseDTO;
import com.alex.deposit.rest.account.AccountServiceClient;
import com.alex.deposit.rest.bill.BillRequestDTO;
import com.alex.deposit.rest.bill.BillResponseDTO;
import com.alex.deposit.rest.bill.BillServiceClient;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.OffsetDateTime;

@Service
public class DepositService {

    private static final String TOPIC_EXCHANGE_DEPOSIT = "js.deposit.notify.exchange";
    private static final String ROUTING_KEY_DEPOSIT = "js.key.deposit";


    private final DepositRepository depositRepository;

    private final AccountServiceClient accountServiceClient;

    private final BillServiceClient billServiceClient;

    private final RabbitTemplate rabbitTemplate;

    @Autowired
    public DepositService(DepositRepository depositRepository, AccountServiceClient accountServiceClient, BillServiceClient billServiceClient, RabbitTemplate rabbitTemplate) {
        this.depositRepository = depositRepository;
        this.accountServiceClient = accountServiceClient;
        this.billServiceClient = billServiceClient;
        this.rabbitTemplate = rabbitTemplate;
    }

    public DepositResponseDTO deposit(Long accountId, Long billId, BigDecimal amount) {
        if (accountId == null && billId == null) {
            throw new ResourceNotFoundException(String.format("Unable to find account with id %s and bill with id %s", accountId, billId));
        } else if (billId != null) {
            BillResponseDTO billResponseDTO = billServiceClient.getBillById(billId);
            AccountResponseDTO accountResponseDTO = accountServiceClient.getAccountById(billResponseDTO.getAccountId());

            BillRequestDTO billRequestDTO = createBillRequest(amount, billResponseDTO);

            billServiceClient.updateBill(billId, billRequestDTO);
            depositRepository.save(new Deposit(amount, billId, OffsetDateTime.now(), accountResponseDTO.getEmail()));

            return createResponse(amount, accountResponseDTO);
        } else {
            BillResponseDTO defaultBill = getDefaultBill(accountId);
            BillRequestDTO billRequestDTO = createBillRequest(amount, defaultBill);
            billServiceClient.updateBill(defaultBill.getBillId(), billRequestDTO);
            AccountResponseDTO accountResponseDTO = accountServiceClient.getAccountById(accountId);
            depositRepository.save(new Deposit(amount, defaultBill.getBillId(), OffsetDateTime.now(), accountResponseDTO.getEmail()));
            return createResponse(amount, accountResponseDTO);
        }
    }

    private DepositResponseDTO createResponse(BigDecimal amount, AccountResponseDTO accountResponseDTO) {
        DepositResponseDTO depositResponseDTO = new DepositResponseDTO(amount, accountResponseDTO.getEmail());

        ObjectMapper objectMapper = new ObjectMapper();
        try {
            rabbitTemplate.convertAndSend(TOPIC_EXCHANGE_DEPOSIT, ROUTING_KEY_DEPOSIT, objectMapper.writeValueAsString(depositResponseDTO));
        } catch (JsonProcessingException e) {
            throw new DepositServiceException("Can't send message to RabbitMQ");
        }
        return depositResponseDTO;
    }

    private BillRequestDTO createBillRequest(BigDecimal amount, BillResponseDTO billResponseDTO) {
        BillRequestDTO billRequestDTO = new BillRequestDTO();
        billRequestDTO.setAccountId(billResponseDTO.getAccountId());
        billRequestDTO.setIsDefault(billResponseDTO.getIsDefault());
        billRequestDTO.setCreationDate(billResponseDTO.getCreationDate());
        billRequestDTO.setOverdraftEnabled(billResponseDTO.getOverdraftEnabled());
        billRequestDTO.setAmount(billResponseDTO.getAmount().add(amount));
        return billRequestDTO;
    }

    private BillResponseDTO getDefaultBill(Long accountId) {
        return billServiceClient.getBillsByAccountId(accountId)
                .stream().filter(BillResponseDTO::getIsDefault).findAny()
                .orElseThrow(() -> new ResourceNotFoundException("Unable to find default bill for account: " + accountId));
    }
}
