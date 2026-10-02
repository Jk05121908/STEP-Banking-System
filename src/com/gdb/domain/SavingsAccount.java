package com.gdb.domain;

import com.gdb.exceptions.*;

public class SavingsAccount extends AbstractAccount {

    private int tenureYears;
    private double minBalance;
    private double interestRate;

    // Default constructor: new customer, tenure 0.
    public SavingsAccount(int accountNumber, String name, int age,
                          double initialBalance)
            throws IllegalArgumentException {

        this(accountNumber, name, age, initialBalance, 0);
    }

    public SavingsAccount(int accountNumber, String name, int age,
                          double initialBalance, int tenureYears)
            throws IllegalArgumentException {

        super(accountNumber, name, age, initialBalance, "Savings");

        this.tenureYears = tenureYears;
        this.minBalance =
                AccountRulesEngine.getSavingsMinBalance(tenureYears);
        this.interestRate =
                AccountRulesEngine.getSavingsInterestRate(tenureYears);
    }

    public int getTenureYears() {
        return tenureYears;
    }

    public double getMinBalance() {
        return minBalance;
    }

    public double getInterestRate() {
        return interestRate;
    }

    // Monthly interest based on the annual rate.
    public double applyMonthlyInterest() {
        double interest = balance * (interestRate / 100) / 12;
        balance += interest;
        return interest;
    }

    @Override
    protected void processDebit(double amount) throws AccountException {

        if (balance - amount < minBalance) {
            throw new MinimumBalanceViolationException(
                    "Cannot withdraw. Minimum balance of Rs " + minBalance
                            + " required. Available after withdrawal: Rs "
                            + (balance - amount));
        }

        balance -= amount;
    }
}
