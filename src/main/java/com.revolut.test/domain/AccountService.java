package com.revolut.test.domain;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.revolut.test.domain.exception.CustomException;

import java.util.Collection;
import java.util.Optional;

@Singleton
public class AccountService {

    @Inject
    private AccountDao accountDAO;

    public Account createAccount(Account account) throws CustomException {
        if(accountDAO.findById(account.getAccountName()).isPresent())
            throw new CustomException(String.format("Account with accountName %s already present",account.getAccountName()));
        return accountDAO.createOrUpdate(account);
    }

    public Account updateAccount(Account account) throws CustomException{
       Account existingAccount = accountDAO.findById(account.getAccountName()).orElseThrow(()->new CustomException(String.format("Account with accountName %s not present",account.getAccountName())));
       existingAccount.lock();
       existingAccount.setAccountBalance(account.getAccountBalance());
       accountDAO.createOrUpdate(existingAccount);
       existingAccount.unlock();
       return existingAccount;
    }

    public Collection<Account> findAll()
    {
        return accountDAO.findAll();
    }

    public Optional<Account> findById(String accountName)
    {
        return accountDAO.findById(accountName);
    }

    public Boolean delete(String accountName)
    {
        return accountDAO.delete(accountName);
    }
}
