package com.gdb.tests;

import com.gdb.domain.IAccount;
import com.gdb.logging.MemoryLogDestination;
import com.gdb.logging.TransactionLogger;
import com.gdb.service.AccountService;

public class TestAccountUI {
    public static void main(String[] args) throws Exception {
        System.out.println("============================================================");
        System.out.println("  ACTIVITY 20 — ACCOUNT UI INTEGRATION TEST");
        System.out.println("============================================================");

        AccountService service = new AccountService(
                new TransactionLogger(new MemoryLogDestination()));

        IAccount first = service.openAccount(
                "SAVINGS", "UI Test One", 25, 15000.0);
        IAccount second = service.openAccount(
                "SAVINGS", "UI Test Two", 25, 10000.0);

        first.setPin(1234);
        second.setPin(5678);

        service.deposit(first.getAccountNumber(), 1000.0);
        service.withdraw(first.getAccountNumber(), 500.0, 1234);
        service.transfer(first.getAccountNumber(),
                second.getAccountNumber(), 1000.0, 1234);

        boolean balancesCorrect =
                first.getBalance() == 14500.0
                && second.getBalance() == 11000.0;

        boolean historyCorrect =
                service.getTransactionHistory().size() == 3;

        boolean accountsCorrect =
                service.getAllAccounts().size() == 2;

        System.out.println("Account creation: "
                + (accountsCorrect ? "[PASS]" : "[FAIL]"));
        System.out.println("Deposit/withdraw/transfer: "
                + (balancesCorrect ? "[PASS]" : "[FAIL]"));
        System.out.println("Transaction history: "
                + (historyCorrect ? "[PASS]" : "[FAIL]"));

        if (!balancesCorrect || !historyCorrect || !accountsCorrect) {
            throw new AssertionError("Activity 20 integration test failed");
        }

        System.out.println("Activity 20 integration checks passed.");
    }
}
