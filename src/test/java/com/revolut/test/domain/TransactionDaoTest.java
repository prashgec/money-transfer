package com.revolut.test.domain;

import io.dropwizard.testing.junit5.DAOTestExtension;
import io.dropwizard.testing.junit5.DropwizardExtensionsSupport;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import java.util.Collection;
import java.util.Date;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(DropwizardExtensionsSupport.class)
public class TransactionDaoTest {


    private TransactionDao transactionDao;

    @BeforeEach
    public void setUp() throws Exception {
        transactionDao = new TransactionDao();
        Cache.ACCOUNT_CACHE.clear();
        Cache.TRANSACTION_CACHE.clear();
    }

    @Test
    public void create()
    {
        final Transaction transaction =transactionDao.create(new Transaction("fromAccount","toAccount",123.45D,new Date()));
        assertThat(transaction.getFromAccountName()).isEqualTo("fromAccount");
        assertThat(transaction.getToAccountName()).isEqualTo("toAccount");
        assertThat(transaction.getTransactionAmount()).isEqualTo(123.45D);

    }

    @Test
    public void findAll()
    {

            transactionDao.create(new Transaction("myAccount", "yourAccount",123.45D,new Date()));
            transactionDao.create(new Transaction("yourAccount","myAccount", 128.45D,new Date()));

        final Collection<Transaction> transactionList = transactionDao.findAll();
        assertThat(transactionList).extracting("fromAccountName").containsOnly("myAccount", "yourAccount");
    }
}
