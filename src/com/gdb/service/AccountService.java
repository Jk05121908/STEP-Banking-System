package com.gdb.service;

import com.gdb.command.*;
import com.gdb.domain.*;
import com.gdb.exceptions.AccountException;
import com.gdb.exceptions.InvalidPinException;
import com.gdb.logging.TransactionLogger;

import java.util.*;

public class AccountService {
    private final Map<Integer, IAccount> accounts = new LinkedHashMap<>();
    private final TransactionLogger logger;
    private int nextAccountNumber = 1001;

    public AccountService(TransactionLogger logger) {
        if (logger == null) {
            throw new IllegalArgumentException("TransactionLogger cannot be null");
        }
        this.logger = logger;
    }

    public IAccount openAccount(
            String type, String name, int age, double initialBalance) {
        IAccount account = AccountFactory.createAccount(
                type, nextAccountNumber, name, age, initialBalance, 0);
        accounts.put(nextAccountNumber, account);
        nextAccountNumber++;
        return account;
    }

    public void closeAccount(int accountNumber, int pin)
            throws AccountException {
        AbstractAccount account = requireAccount(accountNumber);
        if (!account.verifyPin(pin)) {
            throw new InvalidPinException("Incorrect PIN");
        }
        account.closeAccount();
    }

    public Transaction deposit(int accountNumber, double amount)
            throws AccountException {
        AbstractAccount account = requireAccount(accountNumber);
        DepositCommand command = new DepositCommand(account, amount);
        executeAndLog(command);
        return command.getTransaction();
    }

    public Transaction withdraw(int accountNumber, double amount, int pin)
            throws AccountException {
        AbstractAccount account = requireAccount(accountNumber);
        WithdrawCommand command = new WithdrawCommand(account, amount, pin);
        executeAndLog(command);
        return command.getTransaction();
    }

    public Transaction transfer(
            int fromNumber, int toNumber, double amount, int pin)
            throws AccountException {
        AbstractAccount from = requireAccount(fromNumber);
        AbstractAccount to = requireAccount(toNumber);
        TransferCommand command = new TransferCommand(from, to, amount, pin);
        executeAndLog(command);
        return command.getTransaction();
    }

    private void executeAndLog(TransactionCommand command)
            throws AccountException {
        try {
            command.execute();
            logger.log(command);
        } catch (Exception ex) {
            if (ex instanceof AccountException) {
                throw (AccountException) ex;
            }
            throw new AccountException(
                    "Operation or transaction logging failed: " + ex.getMessage());
        }
    }

    private AbstractAccount requireAccount(int number)
            throws AccountException {
        IAccount account = accounts.get(number);

        if (account == null) {
            throw new AccountException("Account not found: " + number);
        }

        if (!(account instanceof AbstractAccount)) {
            throw new AccountException("Unsupported account implementation");
        }

        return (AbstractAccount) account;
    }

    public IAccount getAccount(int number) {
        return accounts.get(number);
    }

    public List<IAccount> getAllAccounts() {
        return new ArrayList<>(accounts.values());
    }

    public List<Transaction> getTransactionHistory() {
        List<Transaction> history = new ArrayList<>();

        for (TransactionCommand command : logger.readAll()) {
            if (command.getTransaction() != null) {
                history.add(command.getTransaction());
            }
        }

        return history;
    }

    public int getNextAccountNumber() {
        return nextAccountNumber;
    }

    public TransactionLogger getLogger() {
        return logger;
    }
}
