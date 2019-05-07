## Introduction

###Revolut Home task
Money transfer api, everything is being saved in-memory (dummy cache implementation using ConcurrentHashmap)

## Available End Points

|Method  | endpoint | possible response | Purpose |
|--------|----------|-------------------|---------|
|GET     |/accounts (com.revolut.test.api.AccountResource)|200|To get the list of accounts|
|POST    |/accounts (com.revolut.test.api.AccountResource)|202, 409|To create account |
|PUT     |/accounts (com.revolut.test.api.AccountResource)|202, 404|To update any account|
|DELETE  |/accounts/{accountName} (com.revolut.test.api.AccountResource)|200, 204|To delete specific account|
|GET     |/accounts/{accountName} (com.revolut.test.api.AccountResource)|200|get specific account |
|GET     |/transactions (com.revolut.test.api.TransferResource)|200| get list of all the transactions |
|POST    |/transactions (com.revolut.test.api.TransferResource)|200, 400| perform the transfer|

### Sample request

- /accounts
      
      {
          "accountName": "fromaccount",
          "accountBalance": 123.334
      }
      POST to create new account PUT to update any existing account
      
-  /accounts/{accountName}

        DELETE to delete the account with accountName provided.
        GET to get the account with accountname provided.

## How to Run
- to build and run use command.
      
      mvn clean package
      java -jar target/money-transfer-1.0-SNAPSHOT.jar
- to run test cases.

      mvn clean test
   

