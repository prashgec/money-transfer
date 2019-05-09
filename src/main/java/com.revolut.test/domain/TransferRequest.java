package com.revolut.test.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TransferRequest {

    @NotNull
    private String fromAccountName;
    @NotNull
    private String toAccountName;
    @NotNull
    private Double transactionAmount;
}
