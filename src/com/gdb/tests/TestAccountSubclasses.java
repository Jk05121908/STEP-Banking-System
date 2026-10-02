package com.gdb.tests;

import com.gdb.domain.*;
import com.gdb.exceptions.*;

public class TestAccountSubclasses {

    public static void main(String[] args) {

        System.out.println("=== Activity 8: Polymorphism Test ===");

        // ================= SAVINGS ACCOUNT =================

        SavingsAccount savings = new SavingsAccount(
                1001,
                "John Doe",
                25,
                10000
        );

        savings.setPin(1234);

        try {

            savings.withdraw(9500, 1234);

            System.out.println(
                    "[Savings] Withdraw 9500 (breaches min balance 1000): "
                            + "FAILED [FAIL]"
            );

        } catch (MinimumBalanceViolationException e) {

            System.out.println(
                    "[Savings] Withdraw 9500 (breaches min balance 1000): "
                            + "Caught MinimumBalanceViolationException [PASS]"
            );
        } catch (Exception e) {

            System.out.println(
                    "[Savings] Withdraw 9500 (breaches min balance 1000): "
                            + "Caught unexpected exception [FAIL]"
            );
        }


        // ================= CURRENT ACCOUNT =================

        CurrentAccount current = new CurrentAccount(
                1002,
                "Alice Brown",
                30,
                10000
        );

        current.setPin(1234);

        // Withdraw 15000
        // Balance becomes -5000
        try {

            current.withdraw(15000, 1234);

            System.out.println(
                    "[Current] Withdraw with Overdraft (Balance goes to -5000): "
                            + "SUCCESS [PASS]"
            );

        } catch (Exception e) {

            System.out.println(
                    "[Current] Withdraw with Overdraft (Balance goes to -5000): "
                            + "FAILED [FAIL]"
            );

        }


        // Withdraw another 21000
        // Current balance = -5000
        // Maximum allowed overdraft = -25000
        // New balance would be -26000, so it must fail
        try {

            current.withdraw(21000, 1234);

            System.out.println(
                    "[Current] Withdraw exceeding Overdraft (exceeds -25000): "
                            + "FAILED [FAIL]"
            );

        } catch (InsufficientBalanceException e) {

            System.out.println(
                    "[Current] Withdraw exceeding Overdraft (exceeds -25000): "
                            + "Caught InsufficientBalanceException [PASS]"
            );

        } catch (Exception e) {

            System.out.println(
                    "[Savings] Withdraw 9500 (breaches min balance 1000): "
                            + "Caught unexpected exception [FAIL]"
            );
        }


        // ================= FIXED DEPOSIT ACCOUNT =================

        FixedDepositAccount fixedDeposit = new FixedDepositAccount(
                1003,
                "Charlie Green",
                35,
                50000
        );

        fixedDeposit.setPin(1234);

        try {

            fixedDeposit.withdraw(10000, 1234);

            System.out.println(
                    "[FixedDeposit] Withdraw attempt: "
                            + "FAILED [FAIL]"
            );

        } catch (AccountException e) {

            System.out.println(
                    "[FixedDeposit] Withdraw attempt: "
                            + "Caught AccountException [PASS]"
            );

        } catch (Exception e) {

            System.out.println(
                    "[FixedDeposit] Withdraw attempt: "
                            + "Caught unexpected exception [FAIL]"
            );
        }
        System.out.println("All polymorphic behaviors verified!");

    }
}