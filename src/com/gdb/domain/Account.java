package com.gdb.domain;
import com.gdb.exceptions.*;

public class Account {

    // ===== Constants =====

    private static final double MIN_BALANCE_SAVINGS = 500.0;
    private static final double MIN_BALANCE_CURRENT = 1000.0;
    private static final int MIN_AGE = 18;
    private static final int MIN_PIN = 1000;
    private static final int MAX_PIN = 9999;


    // ===== Fields =====

    protected int accountNumber;
    protected String name;
    protected int age;
    protected double balance;
    protected String accountType;
    protected String status;
    protected Integer pin;


    // ===== Constructor =====

    public Account(int accountNumber, String name, int age,
                   double initialBalance, String accountType)
            throws IllegalArgumentException {

        // Age validation
        if (age < MIN_AGE) {
            throw new IllegalArgumentException(
                    "Customer must be at least 18 years old. Provided: " + age
            );
        }

        // Account Type Validation
        if (!"Savings".equals(accountType)
                && !"Current".equals(accountType)
                && !"FIXED_DEPOSIT".equals(accountType)
                && !"SALARY".equals(accountType)) {
            throw new IllegalArgumentException(
                    "Invalid account type. Provided: " + accountType
            );
        }

        // Minimum balance validation for Savings
        if ("Savings".equals(accountType) && initialBalance < MIN_BALANCE_SAVINGS) {
            throw new IllegalArgumentException(
                    accountType + " account requires minimum balance of ₹" + MIN_BALANCE_SAVINGS
                            + ". Provided: ₹" + initialBalance
            );
        }

        // Minimum balance validation for Current
        if ("Current".equals(accountType) && initialBalance < MIN_BALANCE_CURRENT) {
            throw new IllegalArgumentException(
                    "Current account requires minimum balance of ₹" + MIN_BALANCE_CURRENT
                            + ". Provided: ₹" + initialBalance
            );
        }

        // Initialize fields
        this.accountNumber = accountNumber;
        this.name = name;
        this.age = age;
        this.balance = initialBalance;
        this.accountType = accountType;
        this.status = "Active";
        this.pin = null;
    }


    // ===== Deposit =====

    public void deposit(double amount)
            throws InvalidAmountException,
            InactiveAccountException {

        // Check account status
        if (status.equals("Inactive")) {
            throw new InactiveAccountException(
                    "Account is inactive. Please reopen the account or contact support."
            );
        }

        // Check amount
        if (amount <= 0) {
            throw new InvalidAmountException(
                    "Deposit amount must be positive. Provided: ₹" + amount
            );
        }

        balance += amount;
    }


    // ===== Withdraw =====

    public void withdraw(double amount, int pin)
            throws AccountException,
            InvalidAmountException,
            InsufficientBalanceException,
            MinimumBalanceViolationException,
            InactiveAccountException,
            InvalidPinException {

        // Check account status
        if (!status.equals("Active")) {
            throw new InactiveAccountException(
                    "Account is inactive. Please reopen the account or contact support."
            );
        }

        // Check PIN is set
        if (this.pin == null) {
            throw new InvalidPinException(
                    "PIN not set for this account"
            );
        }

        // Verify PIN
        if (!verifyPin(pin)) {
            throw new InvalidPinException(
                    "Incorrect PIN"
            );
        }

        // Check amount
        if (amount <= 0) {
            throw new InvalidAmountException(
                    "Withdrawal amount must be positive. Provided: ₹" + amount
            );
        }

        // Check sufficient balance
        if (amount > balance) {
            throw new InsufficientBalanceException(
                    "Insufficient balance. Available: ₹" + balance
                            + ", Requested: ₹" + amount
            );
        }

        // Check minimum balance for Savings account
        if ("Savings".equalsIgnoreCase(accountType)
                && balance - amount < MIN_BALANCE_SAVINGS) {
            throw new MinimumBalanceViolationException(
                    "Cannot withdraw. Minimum balance of ₹" + MIN_BALANCE_SAVINGS
                            + " required. Available after withdrawal: ₹" + (balance - amount)
            );
        }

        // Check minimum balance for Current account (FIXED BUG)
        if ("Current".equalsIgnoreCase(accountType)
                && balance - amount < MIN_BALANCE_CURRENT) {
            throw new MinimumBalanceViolationException(
                    "Cannot withdraw. Minimum balance of ₹" + MIN_BALANCE_CURRENT
                            + " required. Available after withdrawal: ₹" + (balance - amount)
            );
        }

        // Withdraw
        balance -= amount;
    }


    // ===== Account Status =====

    public void closeAccount()
            throws IllegalStateException {
        if (status.equals("Inactive")) {
            throw new IllegalStateException(
                    "Account is already closed"
            );
        }
        status = "Inactive";
    }


    public void reopenAccount()
            throws IllegalStateException {
        if (status.equals("Active")) {
            throw new IllegalStateException(
                    "Account is already active"
            );
        }
        status = "Active";
    }


    // ===== PIN Management =====

    public void setPin(int pin)
            throws IllegalArgumentException {
        if (pin < MIN_PIN || pin > MAX_PIN) {
            throw new IllegalArgumentException(
                    "PIN must be a 4-digit number"
            );
        }
        this.pin = pin;
    }


    public boolean verifyPin(int pin) {
        if (this.pin == null) {
            return false;
        }
        return this.pin == pin;
    }


    public boolean hasPin() {
        return pin != null;
    }


    // ===== Getters =====

    public int getAccountNumber() {
        return accountNumber;
    }

    public String getName() {
        return name;
    }

    public int getAge() {
        return age;
    }

    public double getBalance() {
        return balance;
    }

    public String getStatus() {
        return status;
    }

    public String getAccountType() {
        return accountType;
    }


    // ===== Setters =====

    public void setName(String name) {
        this.name = name;
    }

    public void setAge(int age) {
        if (age < MIN_AGE) {
            this.age = MIN_AGE;
        } else {
            this.age = age;
        }
    }

    public void setBalance(double balance) {
        this.balance = balance;
    }
}