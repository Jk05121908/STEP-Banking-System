package com.gdb.command;

import com.gdb.domain.AbstractAccount;
import com.gdb.domain.Transaction;
import com.gdb.service.TransferService;

public class TransferCommand implements TransactionCommand {
    private static final long serialVersionUID = 1L;
    private transient AbstractAccount from;
    private transient AbstractAccount to;
    private final double amount;
    private final int pin;
    private Transaction transaction;

    public TransferCommand(AbstractAccount from, AbstractAccount to,
                           double amount, int pin) {
        this.from = from;
        this.to = to;
        this.amount = amount;
        this.pin = pin;
    }

    public void execute() throws Exception {
        transaction =
                TransferService.transferWithTransaction(from, to, amount, pin);
    }

    public Transaction getTransaction() {
        return transaction;
    }
}
