
package com.gdb.tests;

import com.gdb.command.*;
import com.gdb.domain.IAccount;
import com.gdb.domain.AccountFactory;
import com.gdb.logging.TransactionLog;

import java.util.List;

public class TestCommandLogging {
    public static void main(String[] args) throws Exception {
        System.out.println("=== ACTIVITY 17: COMMAND PATTERN + FILE LOGGING ===");

        TransactionLog log = new TransactionLog();
        log.clear();

        IAccount acc1 = AccountFactory.createAccount(
                "SAVINGS", 1001, "Rajesh Sharma", 30, 15000.0, 0);

        IAccount acc2 = AccountFactory.createAccount(
                "SAVINGS", 1002, "Priya Sharma", 25, 10000.0, 0);

        acc1.setPin(1234);

        TransactionCommand deposit = new DepositCommand(
                (com.gdb.domain.AbstractAccount) acc1, 5000);
        deposit.execute();
        log.log(deposit);
        System.out.println("Deposit: " + deposit.getTransaction());

        TransactionCommand withdraw = new WithdrawCommand(
                (com.gdb.domain.AbstractAccount) acc1, 2000, 1234);
        withdraw.execute();
        log.log(withdraw);
        System.out.println("Withdrawal: " + withdraw.getTransaction());

        TransactionCommand transfer = new TransferCommand(
                (com.gdb.domain.AbstractAccount) acc1,
                (com.gdb.domain.AbstractAccount) acc2,
                3000, 1234);
        transfer.execute();
        log.log(transfer);
        System.out.println("Transfer: " + transfer.getTransaction());

        System.out.println("Sender balance: " + acc1.getBalance());
        System.out.println("Receiver balance: " + acc2.getBalance());

        List<TransactionCommand> history = log.readAll();
        System.out.println("Transactions in log: " + history.size());

        if (history.size() != 3) {
            throw new AssertionError(
                    "Expected 3 commands, got " + history.size());
        }

        TransactionLog freshLog = new TransactionLog();
        List<TransactionCommand> persisted = freshLog.readAll();
        System.out.println("Persisted commands: " + persisted.size());

        if (persisted.size() != 3) {
            throw new AssertionError(
                    "Expected 3 persisted commands, got " + persisted.size());
        }

        System.out.println("Activity 17 test completed successfully!");
    }
}

