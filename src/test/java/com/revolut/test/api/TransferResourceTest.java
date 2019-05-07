package com.revolut.test.api;

import com.revolut.test.AccountApplication;
import com.revolut.test.AccountConfiguration;
import com.revolut.test.domain.Account;
import com.revolut.test.domain.Cache;
import com.revolut.test.domain.TransferRequest;
import io.dropwizard.testing.junit5.DropwizardAppExtension;
import io.dropwizard.testing.junit5.DropwizardExtensionsSupport;
import org.assertj.core.data.Offset;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import javax.ws.rs.client.Entity;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(DropwizardExtensionsSupport.class)
public class TransferResourceTest {

    public static final DropwizardAppExtension<AccountConfiguration> RULE = new DropwizardAppExtension<>(
            AccountApplication.class);
    private static final String ACCOUNTS_ENDPOINT = "http://localhost:8080/accounts";
    private static final String TRANSACTION_ENDPOINT = "http://localhost:8080/transactions";

    Account fromAccount, toAccount;

    @BeforeEach
    public void setUp() {
        Cache.ACCOUNT_CACHE.clear();
        Cache.TRANSACTION_CACHE.clear();
        fromAccount = new Account();
        fromAccount.setAccountName("myFromAccount");
        fromAccount.setAccountBalance(223.89D);

        toAccount = new Account();
        toAccount.setAccountName("myToAccount");
        toAccount.setAccountBalance(187.89D);
        RULE.client().target(ACCOUNTS_ENDPOINT)
                .request()
                .post(Entity.entity(fromAccount, MediaType.APPLICATION_JSON_TYPE));
        RULE.client().target(ACCOUNTS_ENDPOINT)
                .request()
                .post(Entity.entity(toAccount, MediaType.APPLICATION_JSON_TYPE));
    }

    @Test
    public void transferFundsHappyPath()
    {
        TransferRequest transferRequest = new TransferRequest(fromAccount.getAccountName(),toAccount.getAccountName(),34.98D);

        Response response = performTransfer(transferRequest);
        assertThat(response.getStatusInfo()).isEqualTo(Response.Status.OK);

        response = RULE.client().target(ACCOUNTS_ENDPOINT+"/"+toAccount.getAccountName())
                .request()
                .get();
        assertThat(response.readEntity(Account.class).getAccountBalance()).isEqualTo(toAccount.getAccountBalance()+transferRequest.getTransactionAmount());
    }

    @Test
    public void transferFundsWithInvalidFromAccount()
    {
        TransferRequest transferRequest = new TransferRequest("invalidaccount",toAccount.getAccountName(),34.98D);

        Response response = performTransfer(transferRequest);
        assertThat(response.getStatusInfo()).isEqualTo(Response.Status.BAD_REQUEST);


    }

    @Test
    public void transferFundsWithInvalidToAccount()
    {
        TransferRequest transferRequest = new TransferRequest(fromAccount.getAccountName(),"invalid",34.98D);

        Response response = performTransfer(transferRequest);
        assertThat(response.getStatusInfo()).isEqualTo(Response.Status.BAD_REQUEST);


    }

    @Test
    public void transferFundsWithInsufficientAmount()
    {
        TransferRequest transferRequest = new TransferRequest(fromAccount.getAccountName(),toAccount.getAccountName(),305.80D);

        Response response = performTransfer(transferRequest);
        assertThat(response.getStatusInfo()).isEqualTo(Response.Status.BAD_REQUEST);


    }

    @Test
    public void multipleTransaction() throws InterruptedException {
        TransferRequest transferRequest1 = new TransferRequest(fromAccount.getAccountName(),toAccount.getAccountName(),35.80D);
        TransferRequest transferRequest2 = new TransferRequest(fromAccount.getAccountName(),toAccount.getAccountName(),18.00D);
        TransferRequest transferRequest3 = new TransferRequest(fromAccount.getAccountName(),toAccount.getAccountName(),5.80D);
        TransferRequest transferRequest4 = new TransferRequest(fromAccount.getAccountName(),toAccount.getAccountName(),8.00D);
        TransferRequest transferRequest5 = new TransferRequest(fromAccount.getAccountName(),toAccount.getAccountName(),25.80D);
        TransferRequest transferRequest6 = new TransferRequest(fromAccount.getAccountName(),toAccount.getAccountName(),28.00D);

        ExecutorService executor = Executors.newFixedThreadPool(8);
        List<Callable<Response>> callables = Arrays.asList(
                () -> performTransfer(transferRequest1),
                () -> performTransfer(transferRequest2),
                () -> performTransfer(transferRequest3),
                () -> performTransfer(transferRequest4),
                () -> performTransfer(transferRequest5),
                () -> performTransfer(transferRequest6));
        executor.invokeAll(callables).stream()
                .map(future -> {
                    try {
                        return future.get();
                    }
                    catch (Exception e) {
                        throw new IllegalStateException(e);
                    }
                }).forEach(System.out::println);


        Account updatedFromAccount = getAccount(fromAccount.getAccountName());
        Account updatedToAccount = getAccount(toAccount.getAccountName());
        assertThat((fromAccount.getAccountBalance()
                -transferRequest1.getTransactionAmount()
                -transferRequest2.getTransactionAmount()
                -transferRequest3.getTransactionAmount()
                -transferRequest4.getTransactionAmount()
                -transferRequest5.getTransactionAmount()
                -transferRequest6.getTransactionAmount())).isCloseTo(updatedFromAccount.getAccountBalance(), Offset.offset(1D));

    }

    private Response performTransfer(TransferRequest request)
    {
        return RULE.client().target(TRANSACTION_ENDPOINT)
                .request()
                .post(Entity.entity(request, MediaType.APPLICATION_JSON_TYPE));
    }

    private Account getAccount(String accountName)
    {
        final Response response = RULE.client().target(ACCOUNTS_ENDPOINT+"/"+accountName)
                .request(MediaType.APPLICATION_JSON_TYPE)
                .get();
        return response.readEntity(Account.class);
    }


}
