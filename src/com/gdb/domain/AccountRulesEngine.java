package com.gdb.domain;

import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

public class AccountRulesEngine {

    private static final AccountRulesEngine INSTANCE =
            new AccountRulesEngine();

    private final Map<String, AccountRulesPropertiesLoader> loaders =
            new HashMap<>();

    private AccountRulesEngine() {
        loadRules();
    }

    public static AccountRulesEngine getInstance() {
        return INSTANCE;
    }

    private void loadRules() {
        loaders.put("SAVINGS",
                loadProperties("savings.properties"));

        loaders.put("CURRENT",
                loadProperties("current.properties"));

        loaders.put("FIXEDDEPOSIT",
                loadProperties("fixeddeposit.properties"));

        loaders.put("SALARY",
                loadProperties("salary.properties"));
    }

    private AccountRulesPropertiesLoader loadProperties(
            String fileName) {

        String resourcePath = "config/rules/" + fileName;

        InputStream input = getClass()
                .getClassLoader()
                .getResourceAsStream(resourcePath);

        if (input == null) {
            throw new RuntimeException(
                    "Properties file not found: " + resourcePath
                            + ". Check that src/main/resources is marked "
                            + "as Resources Root.");
        }

        System.out.println(
                "[Config] Loaded rules from src/main/resources/"
                        + resourcePath);

        return new AccountRulesPropertiesLoader(input);
    }

    private static String getTenureBucket(int tenureYears) {
        if (tenureYears >= 5) {
            return "privilege";
        } else if (tenureYears >= 3) {
            return "premium";
        } else if (tenureYears >= 1) {
            return "standard";
        } else {
            return "new";
        }
    }

    private double readRule(
            String accountType,
            int tenureYears,
            String key,
            double defaultValue) {

        AccountRulesPropertiesLoader loader =
                loaders.get(normalizeAccountType(accountType));

        if (loader == null) {
            return defaultValue;
        }

        return loader.getDouble(
                getTenureBucket(tenureYears) + "." + key,
                defaultValue);
    }

    private String normalizeAccountType(String accountType) {
        String type = accountType.trim()
                .toUpperCase()
                .replace(" ", "")
                .replace("_", "");

        if (type.equals("FD") || type.equals("FIXEDDEPOSITACCOUNT")) {
            return "FIXEDDEPOSIT";
        }

        return type;
    }

    // Existing savings methods — now properties-driven

    public static double getSavingsMinBalance(int tenureYears) {
        return INSTANCE.readRule(
                "SAVINGS", tenureYears,
                "min.balance", 10000.0);
    }

    public static double getSavingsInterestRate(int tenureYears) {
        return INSTANCE.readRule(
                "SAVINGS", tenureYears,
                "interest.rate", 2.70);
    }

    // Preserve the existing current-account API.

    public static double getCurrentOverdraftLimit(
            double monthlyTurnover) {

        AccountRulesPropertiesLoader loader =
                INSTANCE.loaders.get("CURRENT");

        double multiplier = loader.getDouble(
                "overdraft.multiplier", 2.5);

        double minimum = loader.getDouble(
                "minimum.overdraft.limit", 25000.0);

        return Math.max(monthlyTurnover * multiplier, minimum);
    }

    // Preserve the existing fixed-deposit API.

    public static double getFDInterestRate(int months) {

        String bucket;

        if (months >= 12) {
            bucket = "privilege";
        } else if (months >= 6) {
            bucket = "premium";
        } else if (months >= 3) {
            bucket = "standard";
        } else {
            bucket = "new";
        }

        AccountRulesPropertiesLoader loader =
                INSTANCE.loaders.get("FIXEDDEPOSIT");

        return loader.getDouble(
                bucket + ".interest.rate", 3.5);
    }

    public static double getSalaryInactiveMonths(int tenureYears) {
        return INSTANCE.readRule(
                "SALARY", tenureYears,
                "inactive.months", 3.0);
    }

    // Activity 15: Retrieve the daily limit for an account.

    public double getDailyTransferLimit(
            String accountType,
            int tenureYears) {

        Object value = getAdditionalFeature(
                accountType, tenureYears, "dailyTransferLimit");

        return value == null ? 0.0 : (Double) value;
    }

    public Object getAdditionalFeature(
            String accountType,
            int tenureYears,
            String featureName) {

        AccountRulesPropertiesLoader loader =
                loaders.get(normalizeAccountType(accountType));

        if (loader == null) {
            return null;
        }

        String bucket = getTenureBucket(tenureYears);
        String key;

        if ("dailyTransferLimit".equals(featureName)) {

            String dailyLimitKey = "daily.transfer.limit." + bucket;

            double value = loader.getDouble(dailyLimitKey, Double.NaN);

            // Support the older key format as a fallback
            if (Double.isNaN(value)) {
                value = loader.getDouble(
                        bucket + ".daily.transfer.limit",
                        Double.NaN);
            }

            return Double.isNaN(value) ? null : value;
        }

        if ("overdraftLimit".equals(featureName)) {
            key = bucket + ".overdraft.limit";

            double value = loader.getDouble(key, Double.NaN);

            if (Double.isNaN(value)) {
                return null;
            }

            return value;
        }

        return null;
    }
}