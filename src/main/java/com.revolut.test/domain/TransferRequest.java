package com.revolut.test.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TransferRequest {

    private String fromAccountName;
    private String toAccountName;
    private Double transactionAmount;
}
