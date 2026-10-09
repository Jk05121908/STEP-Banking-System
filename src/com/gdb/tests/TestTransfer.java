package com.gdb.tests;

import com.gdb.domain.AbstractAccount;
import com.gdb.domain.AccountFactory;
import com.gdb.exceptions.AccountException;
import com.gdb.exceptions.InsufficientBalanceException;
import com.gdb.service.TransferService;

public class TestTransfer {

    public static void main(String[] args) throws Exception {

        System.out.println("=== ACTIVITY 15: TRANSFER WITH DAILY LIMITS ===");

        AbstractAccount acc1 =
                (AbstractAccount) AccountFactory.createAccount(
                        "SAVINGS", 1001, "Rajesh Sharma",
                        30, 100000.0, 0);

        AbstractAccount acc2 =
                (AbstractAccount) AccountFactory.createAccount(
                        "SAVINGS", 1002, "Priya Patel",
                        28, 20000.0, 0);

        acc1.setPin(1234);

        System.out.println("STEP 9: Account #1001 balance = "
                + acc1.getBalance());
        System.out.println("STEP 9: Account #1002 balance = "
                + acc2.getBalance());

        // STEP 10: Successful transfer
        TransferService.transfer(acc1, acc2, 5000.0, 1234);

        System.out.println("STEP 10: Transfer successful");
        System.out.println("acc1 balance = " + acc1.getBalance());
        System.out.println("acc2 balance = " + acc2.getBalance());

        // STEP 11: Attempt transfer beyond available balance
        try {
            TransferService.transfer(acc1, acc2, 100000.0, 1234);
        } catch (InsufficientBalanceException e) {
            System.out.println(
                    "STEP 11: Caught InsufficientBalanceException: "
                            + e.getMessage());
        }

        // STEP 12: Check daily limit
        System.out.println("STEP 12: Daily limit = "
                + acc1.getDailyTransferLimit());

        while (true) {
            try {
                TransferService.transfer(acc1, acc2, 20000.0, 1234);

                System.out.println("Transfer successful. Used today = "
                        + acc1.getDailyTransferTotal());

            } catch (AccountException e) {
                System.out.println("STEP 12: Caught AccountException: "
                        + e.getMessage());
                break;
            }
        }

        // STEP 13: Display total used and remaining
        System.out.println("STEP 13: Used today = "
                + acc1.getDailyTransferTotal());

        System.out.println("Remaining limit = "
                + acc1.getRemainingDailyTransferLimit());
    }
}