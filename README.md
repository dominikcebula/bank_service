# Intro

This repository contains implementation of RESTful Bank Service for money transfer between accounts.

Implementation was created with focus on following requirements:
* Focus on small footprint
* Fast startup time
* Avoid usages of heavy framework

Currently service supports following operations:
* Creating Account
* Listing Accounts with their balance
* List Total Deposit
* Transferring Money between Accounts

# Tools used

Below is the list of tools used during development:
* IntelliJ
* Java 8
* Guice
* SparkJava
* Mockito
* ...

# Compiling project

To compile project type:
```
mvn clean install
```

# Running Project

There are two ways to run project:
* Directly from maven
* Using executable JAR

## Running Project with Maven

To run project from maven, execute:
```
mvn exec:java
```

## Running Project with Executable JAR

To run project from Executable JAR, go to target folder and type:
```
java -jar bank-service-1.0-SNAPSHOT-jar-with-dependencies.jar
```

# Runtime Configuration

Following properties can be configured during startup by properties:
* host - service.host
* port - service.port
* max threads in pool - service.max.threads

For example, if you want to change port execute:
```
mvn exec:java -Dservice.port=8081
```
or from, when using jar:
```
java -Dservice.port=8081 -jar bank-service-1.0-SNAPSHOT-jar-with-dependencies.jar
```

# Making Request

## Checking if service is up

Request:
```
$ curl http://localhost:8080/ -X GET
```

Response:
```
{
  "status": "up"
}
```

## Opening Account

Request:
```
$ curl http://localhost:8080/accounts/create -X POST -d '{
  "initialDeposit": "100.00"
}'
```

Response:
```json
{
  "status": {
    "code": "ACCOUNT_CREATED"
  },
  "account": {
    "accountId": "0481998112065640",
    "balance": 100
  }
}
```

## Listing Accounts and Total Deposit

Request:
```
$ curl http://localhost:8080/accounts/list -X GET
```

Response:
```json
{
  "status": {
    "code": "ACCOUNTS_LISTED"
  },
  "accounts": {
    "accounts": [
      {
        "accountId": "8955895919049153",
        "balance": 100
      },
      {
        "accountId": "0481998112065640",
        "balance": 200
      }
    ],
    "totalDeposit": 300
  }
}
```

## Transferring Money

Request:
```
$ curl http://localhost:8080/transfer -X POST -d '{
  "from": "8955895919049153",
  "to": "0481998112065640",
  "amount": "50.0"
}'
```

Response:
```json
{
  "status": {
    "code": "MONEY_TRANSFERRED"
  },
  "from": "8955895919049153",
  "to": "0481998112065640",
  "amount": 50
}
```
