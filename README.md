# ProductSearch

> ProductSearch is a Spring boot webflex application, designed to query the specific ecommerce rest apis

---

## Table of Contents

- [Requirements](#Requirements)
- [Setup](#Setup)
- [Features](#Features)
- [Documentation](#Documentation)
- [Tests](#Tests)

---

## Requirements

- Java 8 

---

## Setup

The Product Search application uses maven and comes bundled with the maven wrapper, meaning that you will not have to have maven pre-installed to run this application, the maven wrapper will handle all the dirty work.

To get started, either download the zip file from github or clone the repo. once completed, open the new folder and look either of the following files 

> mvnw.cmd

> mvnw
 
 if you are using a windows machine open a terminal in the current location and running the following command 
 > mvnw.cmd install
 
 if you're on a linux/mac machine use the following command 
 > mvnw install

This command will download all the required dependancies of the application and run any existing tests, this install can take a couple of mins, so I suggest you go get a cup of coffee :P

Once done, all that's left to do is run the application, use the following command to start the spring application 

> mvnw spring-boot:run

The application should start up in about 15 secs, and can be accessed at the following location http://localhost:8080/swagger-ui.html

  ---

## Features

Swagger

WebFlux

---

## Documentation

ProductSearch comes bundle with swagger ui for all your documentation needs - to access documentation please use the following url if you're running the application on your localmachine 

> http://localhost:8080/swagger-ui.html

---

## Tests
> To run all tests assoicated with the application simply run one of the following commands 
- mvnw install
- mvnw test

> if you wish to skip the during mvn install, simply add -DskipTests to end of the command.
