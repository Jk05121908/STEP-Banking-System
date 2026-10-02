package com.gdb.domain;

import com.gdb.exceptions.AccountException;
import com.gdb.exceptions.InactiveAccountException;
import com.gdb.exceptions.InvalidAmountException;

public interface IAccount {

    int getAccountNumber();

    String getName();

    double getBalance();

    String getAccountType();

    String getStatus();

    void setPin(int pin);

    void deposit(double amount)
            throws InvalidAmountException, InactiveAccountException;

    void withdraw(double amount, int pin)
            throws AccountException;

    void displayAccountInfo();
}
