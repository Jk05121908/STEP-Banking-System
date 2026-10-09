package com.gdb.tests;

import com.gdb.command.*;
import com.gdb.domain.*;
import com.gdb.logging.*;
import java.util.List;

public class TestBridgeLogging {

    private static void writeThree(
            TransactionLogger logger,
            TransactionCommand first,
            TransactionCommand second,
            TransactionCommand third) {

        logger.log(first);
        logger.log(second);
        logger.log(third);
    }

    private static void verifyCount(
            String name, TransactionLogger logger, int expected) {

        int actual = logger.readAll().size();
        System.out.println("  " + name + " count: " + actual
                + " [EXPECTED: " + expected + "]");

        if (actual != expected) {
            throw new AssertionError(
                    name + " expected " + expected + " records, got " + actual);
        }
    }

    public static void main(String[] args) throws Exception {
        System.out.println("============================================================");
        System.out.println("  ACTIVITY 18 — BRIDGE PATTERN (FILE + DB)");
        System.out.println("============================================================");

        FileLogDestination file = new FileLogDestination();
        DatabaseLogDestination database =
                new DatabaseLogDestination(new SimulatedDatabase());
        MemoryLogDestination memory = new MemoryLogDestination();

        // Start with empty destinations.
        file.clear();
        database.clear();
        memory.clear();

        IAccount account = AccountFactory.createAccount(
                "SAVINGS", 1001, "Rajesh Sharma", 30, 50000.0, 0);
        account.setPin(1234);

        AbstractAccount acc =
                (AbstractAccount) account;

        TransactionCommand deposit = new DepositCommand(acc, 1000);
        TransactionCommand withdrawal =
                new WithdrawCommand(acc, 500, 1234);

        AbstractAccount receiver = (AbstractAccount)
                AccountFactory.createAccount(
                        "SAVINGS", 1002, "Priya Sharma", 25, 10000.0, 0);

        TransactionCommand transfer =
                new TransferCommand(acc, receiver, 500, 1234);

        // Execute each transaction once.
        deposit.execute();
        withdrawal.execute();
        transfer.execute();

        // STEP 25: FILE destination
        TransactionLogger logger = new TransactionLogger(file);
        System.out.println("\n[STEP 25] Logging to FILE destination...");
        writeThree(logger, deposit, withdrawal, transfer);
        System.out.println("  FILE log count: " + logger.readAll().size());

        // STEP 26: DATABASE destination
        logger.setDestination(database);
        System.out.println("\n[STEP 26] Switched to DATABASE destination...");
        writeThree(logger, deposit, withdrawal, transfer);
        System.out.println("  DATABASE log count: "
                + logger.readAll().size());

        // STEP 27: MEMORY destination
        logger.setDestination(memory);
        System.out.println("\n[STEP 27] Switched to MEMORY destination...");
        writeThree(logger, deposit, withdrawal, transfer);
        System.out.println("  MEMORY log count: " + logger.readAll().size());

        // STEP 28: Verify isolation
        System.out.println("\n[STEP 28] Verifying Data Isolation:");
        verifyCount("FILE", new TransactionLogger(file), 3);
        verifyCount("DATABASE", new TransactionLogger(database), 3);
        verifyCount("MEMORY", new TransactionLogger(memory), 3);

        System.out.println("\n[STEP 29] All Bridge Pattern log backends verified successfully!");
    }
}
