package com.gdb.domain;

import com.gdb.exceptions.*;

public class CurrentAccount extends AbstractAccount {

    private double overdraftLimit = 25000.0;

    public CurrentAccount(int accountNumber, String name, int age,
                          double initialBalance)
            throws IllegalArgumentException {

        super(accountNumber, name, age, initialBalance, "Current");
    }

    public double getOverdraftLimit() {
        return overdraftLimit;
    }

    public void setOverdraftLimit(double overdraftLimit) {
        this.overdraftLimit = overdraftLimit;
    }

    public double getAvailableBalance() {
        return balance + overdraftLimit;
    }

    public boolean isInOverdraft() {
        return balance < 0;
    }

    public double getOverdraftAmount() {
        return balance < 0 ? Math.abs(balance) : 0;
    }

    @Override
    protected void processDebit(double amount) throws AccountException {

        double minimumAllowedBalance = -overdraftLimit;

        if (balance - amount < minimumAllowedBalance) {
            throw new InsufficientBalanceException(
                    "Cannot withdraw. Available balance + overdraft: Rs "
                            + (balance + overdraftLimit)
                            + ", Requested: Rs " + amount);
        }

        balance -= amount;
    }
}
