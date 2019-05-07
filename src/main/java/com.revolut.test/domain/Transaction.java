package com.revolut.test.domain;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;


@Data
@NoArgsConstructor
public class Transaction {

    private String fromAccountName;
    private String toAccountName;
    private Double transactionAmount;
    private Date transactionDate;

    public Transaction(String fromAccountName, String toAccountName, Double transactionAmount, Date transactionDate) {
        this.fromAccountName = fromAccountName;
        this.toAccountName = toAccountName;
        this.transactionAmount = transactionAmount;
        this.transactionDate = transactionDate;
    }


}
