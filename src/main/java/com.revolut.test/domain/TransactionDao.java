package com.revolut.test.domain;


import com.google.inject.Singleton;

import javax.transaction.Transactional;
import java.util.Collection;

@Singleton
@Transactional
public class TransactionDao  {

    public Collection<Transaction> findAll()
    {
        return Cache.TRANSACTION_CACHE.values();
    }

    public Transaction create(Transaction transfer)
    {
        Cache.TRANSACTION_CACHE.put(transfer.getFromAccountName(),transfer);
       return Cache.TRANSACTION_CACHE.get(transfer.getFromAccountName());
    }


}
