package com.alex.deposit.controller.dto;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.Digits;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

@Getter
@Setter
public class DepositRequestDTO {

    @NotNull
    private Long accountId;

    private Long billId;

    @NotNull
    @Digits(integer = 9, fraction = 2)
    private BigDecimal amount;
}
