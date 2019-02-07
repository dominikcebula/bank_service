# Intro

This repository contains implementation of RESTful Bank Service for money transfer between accounts.

Implementation was created with focus on following requirements:
* Focus on small footprint
* Fast startup time
* Avoid usages of heavy framework

Currently service supports following operations:
* Opening Account
* Listing Accounts with their balance
* List Total Deposit
* Transferring Money between Accounts

# Tools used

Below is the list of outstanding tools used during development:
* IntelliJ
* Java 8
* Guice
* SparkJava
* JavaMoney
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
java -jar bank.service-1.0-SNAPSHOT-jar-with-dependencies.jar
```

# Runtime Configuration

Following propeties can be configured during startup by properties:
* host - service.host
* port - service.port
* max threads in pool - service.max.threads
* currency used for all operations - service.currency

For example, if you want to change port execute:
```
mvn exec:java -Dservice.port=8081
```
or from, when using jar:
```
java -Dservice.port=8081 -jar bank.service-1.0-SNAPSHOT-jar-with-dependencies.jar
```

# Making Request

## Checking if service is up

Request:
```
$ curl http://localhost:8080/ -X GET
```

Response:
```
"Bank Service is running"
```

## Opening Account

Request:
```
$ curl http://localhost:8080/accounts/open -X POST -d '{
  "initialDeposit": "100.00"
}'
```

Response:
```json
{"accountId":"7706480151894321","status":"SUCCESS","message":"Opened account: [7706480151894321]"}
```

## Listing Accounts and Total Deposit

Request:
```
$ curl http://localhost:8080/accounts/list -X GET
```

Response:
```json
{"accountsInfo":[{"accountId":"1396810841773412","balance":100.0},{"accountId":"5102307317444881","balance":200.0}],"totalDeposit":300.0}
```

## Transferring Money

Request:
```
$ curl http://localhost:8080/transfer -X POST -d '{
  "from": "1396810841773412",
  "to": "5102307317444881",
  "amount": "50.0"
}'
```

Response:
```json
{"from":"1396810841773412","to":"5102307317444881","amount":50.0,"status":"SUCCESS","message":"Transferred [50.00] from [1396810841773412] to [5102307317444881]"}
```
