package com.alex.bill.controller.dto;

import lombok.Getter;

import javax.validation.constraints.*;
import java.math.BigDecimal;
import java.time.OffsetDateTime;

@Getter
public class BillRequestDTO {

    @NotNull
    @Positive
    private Long accountId;

    @NotNull
    @Digits(integer = 9, fraction = 2)
    private BigDecimal amount;

    @NotNull
    private Boolean isDefault;

    private OffsetDateTime creationDate;

    @NotNull
    private Boolean overdraftEnabled;

}
