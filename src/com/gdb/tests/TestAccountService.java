package com.gdb.tests;

import com.gdb.domain.*;
import com.gdb.exceptions.AccountException;
import com.gdb.exceptions.InvalidPinException;
import com.gdb.logging.MemoryLogDestination;
import com.gdb.logging.TransactionLogger;
import com.gdb.service.AccountService;

import java.util.List;

public class TestAccountService {
    public static void main(String[] args) throws Exception {
        System.out.println("============================================================");
        System.out.println("  ACTIVITY 19 — ACCOUNT SERVICE DEMO");
        System.out.println("============================================================");

        MemoryLogDestination memory = new MemoryLogDestination();
        AccountService service =
                new AccountService(new TransactionLogger(memory));

        IAccount john =
                service.openAccount("SAVINGS", "John Doe", 25, 15000.0);
        IAccount jane =
                service.openAccount("SAVINGS", "Jane Smith", 30, 10000.0);

        john.setPin(1234);
        jane.setPin(5678);

        System.out.println("[STEP 13] Opened: " + describe((AbstractAccount) john));
        System.out.println("[STEP 13] Opened: " + describe((AbstractAccount) jane));

        Transaction deposit =
                service.deposit(john.getAccountNumber(), 5000.0);
        System.out.println("\n[STEP 14] Deposit: " + deposit);

        Transaction withdrawal =
                service.withdraw(john.getAccountNumber(), 2000.0, 1234);
        System.out.println("[STEP 15] Withdrawal: " + withdrawal);

        Transaction transfer = service.transfer(
                john.getAccountNumber(), jane.getAccountNumber(), 1000.0, 1234);
        System.out.println("[STEP 16] Transfer: " + transfer);

        System.out.println("\n[STEP 17] Final Balances:");
        System.out.println("  John: Rs. " + john.getBalance());
        System.out.println("  Jane: Rs. " + jane.getBalance());

        List<Transaction> history = service.getTransactionHistory();
        System.out.println("\n[STEP 18] Transaction History ("
                + history.size() + " records):");

        for (int i = 0; i < history.size(); i++) {
            System.out.println("  [" + (i + 1) + "] " + history.get(i));
        }

        System.out.println("\n[STEP 19] Error Handling Checks:");

        try {
            service.deposit(9999, 100);
            throw new AssertionError("Missing account should fail");
        } catch (AccountException ex) {
            System.out.println("  Missing account caught: "
                    + ex.getMessage() + " [PASS]");
        }

        try {
            service.withdraw(john.getAccountNumber(), 100, 9999);
            throw new AssertionError("Wrong PIN should fail");
        } catch (InvalidPinException ex) {
            System.out.println("  Wrong PIN caught: "
                    + ex.getMessage() + " [PASS]");
        }

        if (history.size() != 3
                || john.getBalance() != 17000.0
                || jane.getBalance() != 11000.0) {
            throw new AssertionError("AccountService verification failed");
        }

        System.out.println("\nActivity 19 completed successfully!");
    }

    private static String describe(AbstractAccount account) {
        return "Account #" + account.getAccountNumber()
                + " | " + account.getName()
                + " | " + account.getAccountType()
                + " | Balance: Rs. " + account.getBalance()
                + " | Status: " + account.getStatus();
    }
}
