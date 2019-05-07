package com.revolut.test.api;

import com.revolut.test.AccountApplication;
import com.revolut.test.AccountConfiguration;
import com.revolut.test.domain.Account;
import com.revolut.test.domain.Cache;
import io.dropwizard.testing.junit5.DropwizardAppExtension;
import io.dropwizard.testing.junit5.DropwizardExtensionsSupport;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;

import javax.ws.rs.client.Entity;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(DropwizardExtensionsSupport.class)
public class AccountResourceTest {



   @Test
    public void createAccount()  {
        final Response response = RULE.client().target(URL)
                .request(MediaType.APPLICATION_JSON_TYPE)
                .post(Entity.entity(account, MediaType.APPLICATION_JSON_TYPE));

        assertThat(response.getStatusInfo()).isEqualTo(Response.Status.ACCEPTED);

    }

    @Test
    public void updateAccount()  {

        RULE.client().target(URL)
                .request(MediaType.APPLICATION_JSON_TYPE)
                .post(Entity.entity(account, MediaType.APPLICATION_JSON_TYPE));
        account.setAccountBalance(234.65D);
        final Response response = RULE.client().target(URL)
                .request(MediaType.APPLICATION_JSON_TYPE)
                .put(Entity.entity(account, MediaType.APPLICATION_JSON_TYPE));

        assertThat(response.getStatusInfo()).isEqualTo(Response.Status.ACCEPTED);
        assertThat((response.readEntity(Account.class)).getAccountBalance()).isEqualTo(account.getAccountBalance());
    }

    @Test
    public void deleteAccount()  {

        RULE.client().target(URL)
                .request(MediaType.APPLICATION_JSON_TYPE)
                .post(Entity.entity(account, MediaType.APPLICATION_JSON_TYPE));
        final Response response = RULE.client().target(URL+"/"+account.getAccountName())
                .request(MediaType.APPLICATION_JSON_TYPE)
                .delete();

        assertThat(response.getStatusInfo()).isEqualTo(Response.Status.OK);
    }

    @Test
    public void getAccounts()
    {
        final Response response = RULE.client().target(URL)
                .request(MediaType.APPLICATION_JSON_TYPE)
                .get();

        assertThat(response.getStatusInfo()).isEqualTo(Response.Status.OK);
    }


    private static final String URL = "http://localhost:8080/accounts";//here port can be read from config


    private ArgumentCaptor<Account> accountCaptor = ArgumentCaptor.forClass(Account.class);
    private Account account;

    public static final DropwizardAppExtension<AccountConfiguration> RULE = new DropwizardAppExtension<>(
            AccountApplication.class);

    @BeforeEach
    public void setUp() {
        Cache.ACCOUNT_CACHE.clear();
        Cache.TRANSACTION_CACHE.clear();
        account = new Account();
        account.setAccountName("myAccount");
        account.setAccountBalance(123.89D);
    }



}
