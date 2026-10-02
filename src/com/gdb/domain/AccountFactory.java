package com.gdb.domain;

public class AccountFactory {

    // Existing signature: treats customer as a new customer with tenure 0.
    public static IAccount createAccount(String accountType, int accountNumber,
                                         String name, int age, double initialBalance)
            throws IllegalArgumentException {

        return createAccount(
                accountType, accountNumber, name, age, initialBalance, 0);
    }

    // New signature: tenure is used for SavingsAccount rules.
    public static IAccount createAccount(String accountType, int accountNumber,
                                         String name, int age,
                                         double initialBalance, int tenureYears)
            throws IllegalArgumentException {

        switch (accountType.toUpperCase()) {
            case "SAVINGS":
                return new SavingsAccount(
                        accountNumber, name, age, initialBalance, tenureYears);

            case "CURRENT":
                return new CurrentAccount(
                        accountNumber, name, age, initialBalance);

            case "FIXED_DEPOSIT":
            case "FD":
                return new FixedDepositAccount(
                        accountNumber, name, age, initialBalance);

            case "SALARY":
                return new SalaryAccount(
                        accountNumber, name, age, initialBalance, "Unknown Employer");

            default:
                throw new IllegalArgumentException(
                        "Unknown account type: " + accountType);
        }
    }
}
