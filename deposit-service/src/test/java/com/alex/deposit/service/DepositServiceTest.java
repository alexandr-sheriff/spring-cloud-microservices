package com.alex.deposit.service;

import com.alex.deposit.controller.dto.DepositResponseDTO;
import com.alex.deposit.exception.DepositServiceException;
import com.alex.deposit.repository.DepositRepository;
import com.alex.deposit.rest.account.AccountServiceClient;
import com.alex.deposit.rest.bill.BillRequestDTO;
import com.alex.deposit.rest.bill.BillResponseDTO;
import com.alex.deposit.rest.bill.BillServiceClient;
import org.assertj.core.api.Assertions;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.amqp.rabbit.core.RabbitTemplate;

import java.math.BigDecimal;

import static com.alex.deposit.utils.Utils.*;

@RunWith(MockitoJUnitRunner.class)
public class DepositServiceTest {

    @Mock
    private DepositRepository depositRepository;

    @Mock
    private AccountServiceClient accountServiceClient;

    @Mock
    private BillServiceClient billServiceClient;

    @Mock
    private RabbitTemplate rabbitTemplate;

    @InjectMocks
    private DepositService depositService;

    @Test
    public void depositServiceTest_withBillId() {
        BillResponseDTO billResponseDTO = createBillResponseDTO();
        Mockito.when(billServiceClient.getBillById(ArgumentMatchers.anyLong())).thenReturn(billResponseDTO);
        Mockito.when(accountServiceClient.getAccountById(ArgumentMatchers.anyLong())).thenReturn(createAccountResponseDTO());
        DepositResponseDTO deposit = depositService.deposit(null, 1L, BigDecimal.valueOf(1000));
        Assertions.assertThat(deposit.getAmount()).isEqualTo(billResponseDTO.getAmount());
        Assertions.assertThat(deposit.getMail()).isEqualTo(createAccountResponseDTO().getEmail());

        BillRequestDTO billRequestDTO = createBillRequestDTO(BigDecimal.valueOf(1000L));
        BigDecimal billAmount = deposit.getAmount().add(billResponseDTO.getAmount());
        Assertions.assertThat(billAmount).isEqualTo(billRequestDTO.getAmount());
    }

    @Test(expected = DepositServiceException.class)
    public void depositServiceTest_exception() {
        depositService.deposit(null, null, BigDecimal.valueOf(1000));
    }


}
