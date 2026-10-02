package com.gdb.tests;

import com.gdb.domain.*;
import com.gdb.exceptions.*;

public class TestInterfaceFactory {

    public static void main(String[] args) {

        System.out.println("=== Activity 12: Factory-Driven System Suite ===");

        boolean t1 = testSavings();
        System.out.println(
                "[Test 1] Savings Account Creation & Deposit: "
                        + (t1 ? "[PASS]" : "[FAIL]"));

        boolean t2 = testCurrentOverdraft();
        System.out.println(
                "[Test 2] Current Account Overdraft Withdrawal: "
                        + (t2 ? "[PASS]" : "[FAIL]"));

        boolean t3 = testFixedDepositBlock();
        System.out.println(
                "[Test 3] Fixed Deposit Premature Withdrawal Block: "
                        + (t3 ? "[PASS]" : "[FAIL]"));

        boolean t4 = testInvalidType();
        System.out.println(
                "[Test 4] Invalid Type Rejection: "
                        + (t4 ? "[PASS]" : "[FAIL]"));

        if (t1 && t2 && t3 && t4) {
            System.out.println(
                    "Factory-driven architecture successfully verified!");
        } else {
            System.out.println(
                    "Factory-driven architecture verification FAILED!");
        }
    }

    // Fresh Savings object for deposit test.
    // Another fresh Savings object for minimum-balance test.
    private static boolean testSavings() {

        try {
            IAccount depositAccount =
                    AccountFactory.createAccount(
                            "Savings",
                            5001,
                            "Anita Rao",
                            28,
                            10000.0,
                            4);

            depositAccount.setPin(1234);
            depositAccount.deposit(2000);

            boolean depositOk =
                    depositAccount.getBalance() == 12000.0
                            && depositAccount.getAccountType().equals("Savings");

            IAccount minBalanceAccount =
                    AccountFactory.createAccount(
                            "Savings",
                            5002,
                            "Vikram Das",
                            33,
                            10000.0,
                            4);

            minBalanceAccount.setPin(1234);

            boolean minBalanceEnforced = false;

            try {
                minBalanceAccount.withdraw(6000, 1234);
            } catch (MinimumBalanceViolationException e) {
                minBalanceEnforced = true;
            }

            return depositOk
                    && minBalanceEnforced
                    && minBalanceAccount.getBalance() == 10000.0;

        } catch (Exception e) {
            return false;
        }
    }

    // Fresh Current object for overdraft success.
    // Another fresh Current object for overdraft-limit failure.
    private static boolean testCurrentOverdraft() {

        try {
            IAccount overdraftAccount =
                    AccountFactory.createAccount(
                            "Current",
                            5003,
                            "Sneha Iyer",
                            29,
                            5000.0);

            overdraftAccount.setPin(1234);
            overdraftAccount.withdraw(20000, 1234);

            boolean overdraftOk =
                    overdraftAccount.getBalance() == -15000.0;

            IAccount limitAccount =
                    AccountFactory.createAccount(
                            "Current",
                            5004,
                            "Arjun Menon",
                            41,
                            5000.0);

            limitAccount.setPin(1234);

            boolean limitEnforced = false;

            try {
                limitAccount.withdraw(31000, 1234);
            } catch (InsufficientBalanceException e) {
                limitEnforced = true;
            }

            return overdraftOk
                    && limitEnforced
                    && limitAccount.getBalance() == 5000.0;

        } catch (Exception e) {
            return false;
        }
    }

    // Fresh FD object for the premature-withdrawal test.
    private static boolean testFixedDepositBlock() {

        try {
            IAccount fd =
                    AccountFactory.createAccount(
                            "FIXED_DEPOSIT",
                            5005,
                            "Charlie Green",
                            35,
                            50000.0);

            fd.setPin(1234);

            try {
                fd.withdraw(10000, 1234);
                return false;

            } catch (AccountException e) {

                return e.getClass() == AccountException.class
                        && fd.getBalance() == 50000.0;
            }

        } catch (Exception e) {
            return false;
        }
    }

    // Separate factory call for invalid type.
    private static boolean testInvalidType() {

        try {
            AccountFactory.createAccount(
                    "Platinum",
                    5006,
                    "Test User",
                    30,
                    10000.0);

            return false;

        } catch (IllegalArgumentException e) {
            return true;
        }
    }
}
