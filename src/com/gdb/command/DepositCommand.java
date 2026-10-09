package com.gdb.command;

import com.gdb.domain.AbstractAccount;
import com.gdb.domain.Transaction;

public class DepositCommand implements TransactionCommand {
    private static final long serialVersionUID = 1L;
    private transient AbstractAccount account;
    private final double amount;
    private Transaction transaction;

    public DepositCommand(AbstractAccount account, double amount) {
        this.account = account;
        this.amount = amount;
    }

    public void execute() throws Exception {
        transaction = account.depositWithTransaction(amount);
    }

    public Transaction getTransaction() {
        return transaction;
    }
}
