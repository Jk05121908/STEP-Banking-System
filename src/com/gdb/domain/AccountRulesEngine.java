package com.gdb.domain;

import java.util.HashMap;
import java.util.Map;

public class AccountRulesEngine {

    private static final Map<String, Double> SAVINGS_MIN_BALANCE = new HashMap<>();
    private static final Map<String, Double> SAVINGS_INTEREST_RATE = new HashMap<>();

    static {
        SAVINGS_MIN_BALANCE.put("NEW", 10000.0);
        SAVINGS_MIN_BALANCE.put("STANDARD", 7500.0);
        SAVINGS_MIN_BALANCE.put("PREMIUM", 5000.0);
        SAVINGS_MIN_BALANCE.put("PRIVILEGE", 2500.0);

        SAVINGS_INTEREST_RATE.put("NEW", 2.70);
        SAVINGS_INTEREST_RATE.put("STANDARD", 3.00);
        SAVINGS_INTEREST_RATE.put("PREMIUM", 3.50);
        SAVINGS_INTEREST_RATE.put("PRIVILEGE", 4.00);
    }

    private static String getTenureBucket(int tenureYears) {
        if (tenureYears >= 5) {
            return "PRIVILEGE";
        } else if (tenureYears >= 3) {
            return "PREMIUM";
        } else if (tenureYears >= 1) {
            return "STANDARD";
        } else {
            return "NEW";
        }
    }

    public static double getSavingsMinBalance(int tenureYears) {
        return SAVINGS_MIN_BALANCE.get(getTenureBucket(tenureYears));
    }

    public static double getSavingsInterestRate(int tenureYears) {
        return SAVINGS_INTEREST_RATE.get(getTenureBucket(tenureYears));
    }

    public static double getCurrentOverdraftLimit(double monthlyTurnover) {
        return Math.max(monthlyTurnover * 2.5, 25000.0);
    }

    public static double getFDInterestRate(int months) {
        if (months >= 12) {
            return 6.5;
        } else if (months >= 6) {
            return 5.5;
        } else if (months >= 3) {
            return 4.5;
        } else {
            return 3.5;
        }
    }
}
