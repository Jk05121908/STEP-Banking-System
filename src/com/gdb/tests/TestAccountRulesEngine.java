package com.gdb.tests;

import com.gdb.domain.AccountFactory;
import com.gdb.domain.AccountRulesEngine;
import com.gdb.domain.SavingsAccount;

public class TestAccountRulesEngine {

    public static void main(String[] args) {

        // ================= Activity 13.1 =================
        System.out.println(
                "=== Activity 13.1: Hardcoded Rules Engine Test ===");

        printRule(0);
        printRule(2);
        printRule(4);
        printRule(6);

        System.out.println(
                "Rules Engine lookup completed successfully!");

        System.out.println();

        // ================= Activity 13.2 =================
        System.out.println(
                "=== Activity 13.2: Dynamic Account Rules Test ===");

        boolean allPassed = true;

        // Separate object for tenure 4.
        SavingsAccount s4 =
                (SavingsAccount) AccountFactory.createAccount(
                        "Savings",
                        4001,
                        "Rajesh Sharma",
                        28,
                        8000.0,
                        4);

        System.out.println(
                "Created Savings Account (Tenure: "
                        + s4.getTenureYears() + " yrs):");

        System.out.println(
                " -> Min Balance: Rs "
                        + s4.getMinBalance()
                        + " (Dynamically fetched)");

        System.out.println(
                " -> Interest Rate: "
                        + s4.getInterestRate()
                        + "% (Dynamically fetched)");

        allPassed =
                allPassed
                        && s4.getMinBalance() == 5000.0
                        && s4.getInterestRate() == 3.5;

        // Separate object for every additional tenure.
        SavingsAccount s0 =
                (SavingsAccount) AccountFactory.createAccount(
                        "Savings", 4002, "Priya Nair",
                        22, 20000.0, 0);

        SavingsAccount s2 =
                (SavingsAccount) AccountFactory.createAccount(
                        "Savings", 4003, "Karthik Raj",
                        31, 20000.0, 2);

        SavingsAccount s6 =
                (SavingsAccount) AccountFactory.createAccount(
                        "Savings", 4004, "Lakshmi Devi",
                        45, 20000.0, 6);

        allPassed = allPassed && verify(s0, 10000.0, 2.7);
        allPassed = allPassed && verify(s2, 7500.0, 3.0);
        allPassed = allPassed && verify(s6, 2500.0, 4.0);

        System.out.println(
                allPassed
                        ? "Dynamic rule integration verified!"
                        : "Dynamic rule integration FAILED!");
    }

    private static boolean verify(SavingsAccount account,
                                  double expectedMin,
                                  double expectedRate) {

        boolean ok =
                account.getMinBalance() == expectedMin
                        && account.getInterestRate() == expectedRate;

        System.out.println(
                "Tenure " + account.getTenureYears()
                        + " yrs -> Min Balance: Rs "
                        + account.getMinBalance()
                        + " | Interest: "
                        + account.getInterestRate()
                        + "% "
                        + (ok ? "[PASS]" : "[FAIL]"));

        return ok;
    }

    private static void printRule(int tenureYears) {

        double minBalance =
                AccountRulesEngine.getSavingsMinBalance(tenureYears);

        double interestRate =
                AccountRulesEngine.getSavingsInterestRate(tenureYears);

        System.out.println(
                "Tenure " + tenureYears
                        + " yrs -> Min Balance: Rs "
                        + minBalance
                        + " | Interest: "
                        + interestRate + "%");
    }
}
