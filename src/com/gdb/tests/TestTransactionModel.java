package com.gdb.tests;

import com.gdb.domain.AbstractAccount;
import com.gdb.domain.AccountFactory;
import com.gdb.domain.Transaction;
import com.gdb.service.TransferService;

public class TestTransactionModel {

    public static void main(String[] args) throws Exception {

        System.out.println("============================================================");
        System.out.println("  ACTIVITY 16 — TRANSACTION MODEL TEST");
        System.out.println("============================================================");

        // Create two accounts.
        AbstractAccount acc1 =
                (AbstractAccount) AccountFactory.createAccount(
                        "SAVINGS",
                        1001,
                        "Rajesh Sharma",
                        30,
                        50000.0,
                        0
                );

        AbstractAccount acc2 =
                (AbstractAccount) AccountFactory.createAccount(
                        "SAVINGS",
                        1002,
                        "Priya Patel",
                        28,
                        10000.0,
                        0
                );

        acc1.setPin(1234);

        // STEP 10: Deposit with transaction.
        Transaction depositTransaction =
                acc1.depositWithTransaction(5000);

        System.out.println(
                "[STEP 10] Deposit Transaction: "
                        + depositTransaction
        );

        // STEP 11: Withdraw with transaction.
        Transaction withdrawalTransaction =
                acc1.withdrawWithTransaction(2000, 1234);

        System.out.println(
                "[STEP 11] Withdrawal Transaction: "
                        + withdrawalTransaction
        );

        // STEP 12: Transfer with transaction.
        Transaction transferTransaction =
                TransferService.transferWithTransaction(
                        acc1,
                        acc2,
                        1000,
                        1234
                );

        System.out.println(
                "[STEP 12] Transfer Transaction: "
                        + transferTransaction
        );

        // STEP 13: Verify the original deposit method still works.
        acc1.deposit(1000);

        System.out.println(
                "[STEP 13] Legacy Deposit +1000: Account #"
                        + acc1.getAccountNumber()
                        + " | Balance: Rs. "
                        + acc1.getBalance()
        );

        System.out.println("============================================================");
        System.out.println("  ACTIVITY 16 TEST COMPLETED");
        System.out.println("============================================================");
    }
}