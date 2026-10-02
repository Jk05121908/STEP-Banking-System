package com.gdb.domain;

import com.gdb.exceptions.*;

public class SalaryAccount extends AbstractAccount {

    private static final int MAX_INACTIVE_MONTHS = 3;

    private String employerName;
    private int inactiveMonths = 0;

    public SalaryAccount(int accountNumber, String name, int age,
                         double initialBalance, String employerName)
            throws IllegalArgumentException {

        super(accountNumber, name, age, initialBalance, "SALARY");
        this.employerName = employerName;
    }

    public String getEmployerName() {
        return employerName;
    }

    public void setEmployerName(String employerName) {
        this.employerName = employerName;
    }

    public int getInactiveMonths() {
        return inactiveMonths;
    }

    public void incrementInactiveMonths() {
        this.inactiveMonths++;
    }

    public void resetInactiveMonths() {
        this.inactiveMonths = 0;
    }

    public boolean hasRecentSalaryCredit() {
        return inactiveMonths < MAX_INACTIVE_MONTHS;
    }

    public void recordSalaryDeposit(double salaryAmount)
            throws InvalidAmountException, InactiveAccountException {

        deposit(salaryAmount);
        inactiveMonths = 0;
    }

    @Override
    protected void processDebit(double amount) throws AccountException {

        if (amount > balance) {
            throw new InsufficientBalanceException(
                    "Insufficient balance. Available: Rs " + balance
                            + ", Requested: Rs " + amount);
        }

        balance -= amount;
    }
}
