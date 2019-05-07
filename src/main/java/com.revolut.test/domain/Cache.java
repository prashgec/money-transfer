package com.revolut.test.domain;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class Cache {
    public static Map<String, Account> ACCOUNT_CACHE= new ConcurrentHashMap<>();
    public static Map<String, Transaction> TRANSACTION_CACHE= new ConcurrentHashMap<>();
}
