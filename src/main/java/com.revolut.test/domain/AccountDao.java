package com.revolut.test.domain;

import com.google.inject.Singleton;

import javax.transaction.Transactional;
import java.util.Collection;
import java.util.Optional;

@Singleton
@Transactional
public class AccountDao {


    public Account createOrUpdate(Account account) {
        Cache.ACCOUNT_CACHE.put(account.getAccountName(), account);
        return Cache.ACCOUNT_CACHE.get(account.getAccountName());
    }

    public Optional<Account> findById(String accountName) {
        return Optional.ofNullable(Cache.ACCOUNT_CACHE.get(accountName));
    }

    public Collection<Account> findAll() {
        return Cache.ACCOUNT_CACHE.values();
    }

    public Boolean delete(String accountName) {
        if (Cache.ACCOUNT_CACHE.containsKey(accountName)) {
            Cache.ACCOUNT_CACHE.remove(accountName);
            return true;
        }
        return false;

    }
}
