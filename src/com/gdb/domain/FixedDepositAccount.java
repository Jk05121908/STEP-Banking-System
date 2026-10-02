package com.gdb.domain;

import com.gdb.exceptions.*;

public class FixedDepositAccount extends AbstractAccount {

    private int tenureMonths = 12;
    private double interestRate = 6.5;
    private long createdDate;

    public FixedDepositAccount(int accountNumber, String name, int age,
                               double initialBalance)
            throws IllegalArgumentException {

        super(accountNumber, name, age, initialBalance, "FIXED_DEPOSIT");
        this.createdDate = System.currentTimeMillis();
    }

    public FixedDepositAccount(int accountNumber, String name, int age,
                               double initialBalance, int tenureMonths,
                               double interestRate)
            throws IllegalArgumentException {

        super(accountNumber, name, age, initialBalance, "FIXED_DEPOSIT");
        this.tenureMonths = tenureMonths;
        this.interestRate = interestRate;
        this.createdDate = System.currentTimeMillis();
    }

    public int getTenureMonths() {
        return tenureMonths;
    }

    public void setTenureMonths(int tenureMonths) {
        this.tenureMonths = tenureMonths;
    }

    public double getInterestRate() {
        return interestRate;
    }

    public void setInterestRate(double interestRate) {
        this.interestRate = interestRate;
    }

    public long getCreatedDate() {
        return createdDate;
    }

    public double calculateMaturityAmount() {
        double years = tenureMonths / 12.0;
        return balance * Math.pow(1 + (interestRate / 100), years);
    }

    public boolean isMatured() {
        long monthsElapsed =
                (System.currentTimeMillis() - createdDate)
                        / (1000L * 60 * 60 * 24 * 30);

        return monthsElapsed >= tenureMonths;
    }

    public long getMonthsUntilMaturity() {
        long monthsElapsed =
                (System.currentTimeMillis() - createdDate)
                        / (1000L * 60 * 60 * 24 * 30);

        return Math.max(0, tenureMonths - monthsElapsed);
    }

    @Override
    protected void processDebit(double amount) throws AccountException {

        throw new AccountException(
                "Fixed Deposit accounts do not allow premature withdrawal. "
                        + "Please wait until maturity (after "
                        + tenureMonths + " months)");
    }
}
