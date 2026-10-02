package com.gdb.domain;

import com.gdb.exceptions.*;

public abstract class AbstractAccount implements IAccount {

    protected static final double MIN_BALANCE_SAVINGS = 500.0;
    protected static final double MIN_BALANCE_CURRENT = 1000.0;
    protected static final int MIN_AGE = 18;
    protected static final int MIN_PIN = 1000;
    protected static final int MAX_PIN = 9999;

    protected int accountNumber;
    protected String name;
    protected int age;
    protected double balance;
    protected String accountType;
    protected String status;
    protected Integer pin;

    public AbstractAccount(int accountNumber, String name, int age,
                           double initialBalance, String accountType)
            throws IllegalArgumentException {

        if (age < MIN_AGE) {
            throw new IllegalArgumentException(
                    "Customer must be at least 18 years old. Provided: " + age);
        }

        if (!"Savings".equals(accountType)
                && !"Current".equals(accountType)
                && !"FIXED_DEPOSIT".equals(accountType)
                && !"SALARY".equals(accountType)) {
            throw new IllegalArgumentException(
                    "Invalid account type. Provided: " + accountType);
        }

        if ("Savings".equals(accountType) && initialBalance < MIN_BALANCE_SAVINGS) {
            throw new IllegalArgumentException(
                    "Savings account requires minimum balance of Rs "
                            + MIN_BALANCE_SAVINGS + ". Provided: Rs " + initialBalance);
        }

        if ("Current".equals(accountType) && initialBalance < MIN_BALANCE_CURRENT) {
            throw new IllegalArgumentException(
                    "Current account requires minimum balance of Rs "
                            + MIN_BALANCE_CURRENT + ". Provided: Rs " + initialBalance);
        }

        this.accountNumber = accountNumber;
        this.name = name;
        this.age = age;
        this.balance = initialBalance;
        this.accountType = accountType;
        this.status = "Active";
        this.pin = null;
    }

    @Override
    public void deposit(double amount)
            throws InvalidAmountException, InactiveAccountException {

        if (!status.equals("Active")) {
            throw new InactiveAccountException(
                    "Account is inactive. Please reopen the account or contact support.");
        }

        if (amount <= 0) {
            throw new InvalidAmountException(
                    "Deposit amount must be positive. Provided: Rs " + amount);
        }

        balance += amount;
    }

    // Template Method: every withdrawal follows the same fixed sequence.
    @Override
    public final void withdraw(double amount, int pin) throws AccountException {

        // 1. Validate PIN
        validatePin(pin);

        // 2. Validate account status
        if (!status.equals("Active")) {
            throw new InactiveAccountException(
                    "Account is inactive. Please reopen the account or contact support.");
        }

        // 3. Validate amount
        if (amount <= 0) {
            throw new InvalidAmountException(
                    "Withdrawal amount must be positive. Provided: Rs " + amount);
        }

        // 4. Delegate account-specific debit rule to subclass
        processDebit(amount);
    }

    protected abstract void processDebit(double amount) throws AccountException;

    public void validatePin(int pin) throws InvalidPinException {
        if (this.pin == null) {
            throw new InvalidPinException("PIN not set for this account");
        }

        if (this.pin != pin) {
            throw new InvalidPinException("Incorrect PIN");
        }
    }

    public void changePin(int oldPin, int newPin) throws InvalidPinException {
        validatePin(oldPin);

        if (newPin < MIN_PIN || newPin > MAX_PIN) {
            throw new InvalidPinException("New PIN must be a 4-digit number");
        }

        this.pin = newPin;
    }

    @Override
    public void setPin(int pin) throws IllegalArgumentException {
        if (pin < MIN_PIN || pin > MAX_PIN) {
            throw new IllegalArgumentException("PIN must be a 4-digit number");
        }

        this.pin = pin;
    }

    public boolean verifyPin(int pin) {
        return this.pin != null && this.pin == pin;
    }

    public boolean hasPin() {
        return pin != null;
    }

    @Override
    public void displayAccountInfo() {
        System.out.println("Account Number: " + accountNumber);
        System.out.println("Name: " + name);
        System.out.println("Age: " + age);
        System.out.println("Balance: Rs " + balance);
        System.out.println("Account Type: " + accountType);
        System.out.println("Status: " + status);
    }

    public void closeAccount() throws IllegalStateException {
        if (status.equals("Inactive")) {
            throw new IllegalStateException("Account is already closed");
        }

        status = "Inactive";
    }

    public void reopenAccount() throws IllegalStateException {
        if (status.equals("Active")) {
            throw new IllegalStateException("Account is already active");
        }

        status = "Active";
    }

    @Override
    public int getAccountNumber() {
        return accountNumber;
    }

    @Override
    public String getName() {
        return name;
    }

    public int getAge() {
        return age;
    }

    @Override
    public double getBalance() {
        return balance;
    }

    @Override
    public String getStatus() {
        return status;
    }

    @Override
    public String getAccountType() {
        return accountType;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setAge(int age) {
        this.age = (age < MIN_AGE) ? MIN_AGE : age;
    }

    public void setBalance(double balance) {
        this.balance = balance;
    }
}
