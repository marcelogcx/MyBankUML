# 🏦 BankUml: Banking System Simulation

Welcome to **BankUml**, a Java-based banking application designed to simulate core banking operations such as account management, transactions, and receipts.  

This project demonstrates the use of Object-Oriented Programming (OOP) principles, including **Inheritance**, **Encapsulation**, **Abstraction**, and **Polymorphism**, strictly following the provided UML diagram.

## 📌 Features

- **Account Management**: Create and manage multiple types of bank accounts.
- **Transaction Handling**: Simulate payments and generate receipts.
- **UML-Driven Design**: Class structure directly follows the given UML diagram.

## 📊 Diagram

![BankUml Hierarchical Diagram](./BankUml_Hierarchical_Diagram.drawio.svg)

This diagram demonstrates the classes within the BankUml project template. Inheritance relationships are shown with solid line arrows, whilst implied relationships are shown with dashed line arrows. This is only a reference for the template, and you are free to change the application architecture as you see fit!

## 🚀 How to Run

Make sure you have the following installed:

- Java (USE VERSION 21)
- Maven (if Lombok is missing or not working correctly)

1. Clone the repository:

```bash
git clone https://github.com/M-PERSIC/BankUml.git
cd BankUml
```
2. Running Application

You can run the code by in root directory, 
mvn clean compile (note be on Java 21)
then after a sucessful build run
mvn exec:java.

The GUI will open up if its sucessful register a new account but remember if you mvn clean compile again it will wipe the database.
The database is stored in /target/classes/data so you can observe the account stored there.

OTHER METHOD

2. Compile the code:

```bash
javac -cp "libs/*" bank/*.java 
```

3. Run the program:

```bash
# Linux/MAC
java -cp ".:libs/*" bank.Main
# Windows
java -cp ".;libs/*" bank.Main

```
The repository has mock data so navigate to ~/data/user.json and use the username and password to login.

To redownload the Lombok jar:

```bash
mvn dependency:copy-dependencies -DoutputDirectory=./libs
```

---

Originally developed by [@shayanaminaei](https://github.com/shayanaminaei)
