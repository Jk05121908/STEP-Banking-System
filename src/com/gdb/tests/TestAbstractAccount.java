package com.gdb.tests;

import com.gdb.domain.*;
import com.gdb.exceptions.*;

public class TestAbstractAccount {

    private static final int PIN = 1234;

    public static boolean transferFunds(AbstractAccount source,
                                        AbstractAccount destination,
                                        double amount,
                                        int pin) {

        try {
            source.withdraw(amount, pin);
        } catch (AccountException e) {
            return false;
        }

        try {
            destination.deposit(amount);
            return true;
        } catch (AccountException e) {
            // Refund source if destination deposit unexpectedly fails.
            try {
                source.deposit(amount);
            } catch (AccountException ignored) {
                // Nothing more can be done here.
            }
            return false;
        }
    }

    public static void main(String[] args) {

        System.out.println("=== Activity 10: Banking Operations Suite ===");

        boolean allPassed = true;

        // ----- Portfolio -----
        AbstractAccount[] portfolio = new AbstractAccount[3];

        portfolio[0] =
                new SavingsAccount(1001, "Kavya", 19, 10000.0, 4);

        portfolio[1] =
                new CurrentAccount(1002, "Kavya", 19, 5000.0);

        portfolio[2] =
                new SalaryAccount(1003, "Kavya", 25, 2500.0, "Neha");

        for (AbstractAccount account : portfolio) {
            account.setPin(PIN);
        }

        // ----- Successful transfer -----
        boolean successfulTransfer =
                transferFunds(portfolio[0], portfolio[1], 3000, PIN);

        System.out.println(
                "Transfer Rs 3000 from Savings to Current: "
                        + (successfulTransfer ? "SUCCESS" : "FAILED"));

        System.out.println(
                "Savings Balance: Rs " + portfolio[0].getBalance()
                        + " | Current Balance: Rs "
                        + portfolio[1].getBalance());

        if (!successfulTransfer
                || portfolio[0].getBalance() != 7000.0
                || portfolio[1].getBalance() != 8000.0) {

            allPassed = false;
        }

        // ----- Failed transfer uses completely fresh objects -----
        SavingsAccount wrongPinSource =
                new SavingsAccount(2001, "Ravi", 30, 10000.0, 4);

        CurrentAccount wrongPinDestination =
                new CurrentAccount(2002, "Ravi", 30, 5000.0);

        wrongPinSource.setPin(PIN);
        wrongPinDestination.setPin(PIN);

        boolean failedTransfer =
                !transferFunds(
                        wrongPinSource,
                        wrongPinDestination,
                        3000,
                        9999);

        boolean balancesUnchanged =
                wrongPinSource.getBalance() == 10000.0
                        && wrongPinDestination.getBalance() == 5000.0;

        if (failedTransfer && balancesUnchanged) {
            System.out.println(
                    "Failed Transfer (Wrong PIN): Exception caught, "
                            + "no balance changed [PASS]");
        } else {
            System.out.println(
                    "Failed Transfer (Wrong PIN): balances changed [FAIL]");
            allPassed = false;
        }

        // ----- Monthly banking cycle -----
        double savingsBefore = portfolio[0].getBalance();

        for (AbstractAccount account : portfolio) {

            if (account instanceof SavingsAccount) {

                SavingsAccount savings =
                        (SavingsAccount) account;

                savings.applyMonthlyInterest();

            } else if (account instanceof SalaryAccount) {

                SalaryAccount salary =
                        (SalaryAccount) account;

                salary.incrementInactiveMonths();

                if (!salary.hasRecentSalaryCredit()) {
                    System.out.println(
                            "Warning: no salary credit for "
                                    + salary.getInactiveMonths()
                                    + " months on account "
                                    + salary.getAccountNumber());
                }
            }
        }

        if (portfolio[0].getBalance() <= savingsBefore) {
            allPassed = false;
        }

        System.out.println(
                "Monthly Interest Cycle processed for all qualifying accounts.");

        System.out.println(
                allPassed
                        ? "All banking operations passed!"
                        : "Some banking operations FAILED!");
    }
}
