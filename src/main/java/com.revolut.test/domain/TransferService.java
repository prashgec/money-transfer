package com.revolut.test.domain;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.revolut.test.domain.exception.CustomException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collection;
import java.util.Date;

@Singleton
public class TransferService {

    private static final Logger LOGGER = LoggerFactory.getLogger(TransferService.class);

    @Inject
    private TransactionDao transactionDAO;

    @Inject
    private AccountService accountService;






    public Transaction performTransfer(TransferRequest transferRequest) throws CustomException {

            Account fromAccount = accountService.findById(transferRequest.getFromAccountName()).orElseThrow(() -> new CustomException("From Account not found"));
            Account toAccount = accountService.findById(transferRequest.getToAccountName()).orElseThrow(() -> new CustomException("To Account not found"));

            {
                fromAccount.lock();

                fromAccount = accountService.findById(transferRequest.getFromAccountName()).get();
                LOGGER.info("account balance for accountname {} is {} for transaction amount {}", fromAccount.getAccountName(), fromAccount.getAccountBalance(), transferRequest.getTransactionAmount());
                try {
                    if (fromAccount.getAccountBalance().compareTo(transferRequest.getTransactionAmount()) < 0)
                        throw new CustomException("insufficient balance");
                    fromAccount.setAccountBalance(fromAccount.getAccountBalance() - transferRequest.getTransactionAmount());
                    toAccount.lock();
                    toAccount = accountService.findById(transferRequest.getToAccountName()).get();
                    try {
                        toAccount.setAccountBalance(toAccount.getAccountBalance() + transferRequest.getTransactionAmount());
                        accountService.updateAccount(toAccount);
                    } finally {
                       toAccount.unlock();
                    }
                    accountService.updateAccount(fromAccount);
                    LOGGER.info("account balance for accountname {} is updated to  {}  for transaction amount {}", fromAccount.getAccountName(), fromAccount.getAccountBalance(), transferRequest.getTransactionAmount());

                } finally {
                   fromAccount.unlock();
                }
            }
            Transaction transaction = new Transaction(fromAccount.getAccountName(), toAccount.getAccountName(), transferRequest.getTransactionAmount(), new Date());
            return transactionDAO.create(transaction);
        }

    public Collection<Transaction> getAllTransactions() {
        return transactionDAO.findAll();
    }


}
