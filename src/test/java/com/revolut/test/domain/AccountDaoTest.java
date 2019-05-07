package com.revolut.test.domain;

import io.dropwizard.testing.junit5.DropwizardExtensionsSupport;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import java.util.Collection;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(DropwizardExtensionsSupport.class)
public class AccountDaoTest {

    private AccountDao accountDAO;

    @BeforeEach
    public void setUp() throws Exception {
        accountDAO = new AccountDao();
        Cache.ACCOUNT_CACHE.clear();
        Cache.TRANSACTION_CACHE.clear();
    }

    @Test
    public void createOrUpdateAccount()
    {
        final Account myAccount =accountDAO.createOrUpdate(new Account("myAccount",123.45D));
        assertThat(myAccount.getAccountName()).isEqualTo("myAccount");
        assertThat(myAccount.getAccountBalance()).isEqualTo(123.45D);
        assertThat(accountDAO.findById(myAccount.getAccountName())).isEqualTo(Optional.of(myAccount));
    }

    @Test
    public void findAll()
    {

            accountDAO.createOrUpdate(new Account("myAccount", 123.45D));
            accountDAO.createOrUpdate(new Account("yourAccount", 128.45D));

        final Collection<Account> accountList = accountDAO.findAll();
        assertThat(accountList).extracting("accountName").containsOnly("myAccount", "yourAccount");
    }

    @Test
    public void delete()
    {
        final Account myAccount =accountDAO.createOrUpdate(new Account("myAccount",123.45D));
        accountDAO.delete(myAccount.getAccountName());
        assertThat(accountDAO.findById(myAccount.getAccountName()).isPresent()).isEqualTo(false);
    }

}
