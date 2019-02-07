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
mvn run
```

## Running Project with Executable JAR

To run project from Executable JAR, to to target folder and type:
```
java -jar bank.service-1.0-SNAPSHOT-jar-with-dependencies.jar
```

# Making Request

## Opening Account

TODO

## Listing Accounts and Total Deposit

TODO

## Transferring Money

TODO