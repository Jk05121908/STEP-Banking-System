public class TestAccountEnhanced {

    // =========================================================
    // Display account information
    // =========================================================

    public static void displayAccount(AccountEnhanced account) {

        System.out.println(
                "Account #" + account.getAccountNumber()
                        + " | " + account.getName()
                        + " (" + account.getAge() + " yrs)"
                        + " | " + account.getAccountType()
                        + " | ₹" + account.getBalance()
                        + " | " + account.getStatus()
                        + " | PIN: " + (account.hasPin() ? "Yes" : "No")
        );
    }


    public static void main(String[] args) {

        System.out.println("============================================================");
        System.out.println("           ENHANCED ACCOUNT TEST (BOOLEAN RETURNS)");
        System.out.println("============================================================");


        // =========================================================
        // TEST 1
        // =========================================================

        System.out.println("\n>>> Test 1: Valid Account Creation");

        AccountEnhanced account1 =
                new AccountEnhanced(1001, "John Doe", 25, 1000, "Savings");

        displayAccount(account1);


        // =========================================================
        // TEST 2
        // =========================================================

        System.out.println("\n>>> Test 2: Invalid Age (under 18)");

        System.out.println("Creating account with age 16");

        AccountEnhanced account2 =
                new AccountEnhanced(1002, "Young Kid", 16, 500, "Savings");

        System.out.println(
                "Age auto-corrected to: " + account2.getAge()
        );

        displayAccount(account2);


        // =========================================================
        // TEST 3
        // =========================================================

        System.out.println("\n>>> Test 3: Invalid Account Type");

        System.out.println("Creating account with type \"Invalid\"");

        AccountEnhanced account3 =
                new AccountEnhanced(1003, "Test User", 25, 500, "Invalid");

        System.out.println(
                "Account type defaulted to: "
                        + account3.getAccountType()
        );

        displayAccount(account3);


        // =========================================================
        // TEST 4
        // =========================================================

        System.out.println(
                "\n>>> Test 4: Minimum Balance Enforcement on Creation"
        );

        System.out.println(
                "Creating Savings account with ₹300 (below minimum)"
        );

        AccountEnhanced account4 =
                new AccountEnhanced(1004, "Bob Wilson", 25, 300, "Savings");

        System.out.println(
                "Balance auto-corrected to minimum: ₹"
                        + account4.getBalance()
        );

        displayAccount(account4);


        // =========================================================
        // TEST 5
        // =========================================================

        System.out.println(
                "\n>>> Test 5: Withdrawal with Minimum Balance"
        );

        AccountEnhanced account5 =
                new AccountEnhanced(
                        1005, "Alice Brown", 30, 1000, "Current"
                );

        account5.setPin(1234);

        System.out.print("Initial: ");
        displayAccount(account5);


        boolean withdrawalResult =
                account5.withdraw(200, 1234);

        if (withdrawalResult) {

            System.out.println("Withdrawing ₹200.0: SUCCESS");

        } else {

            System.out.println("Withdrawing ₹200.0: FAILED");
        }

        System.out.println(
                "New balance: ₹" + account5.getBalance()
        );

        System.out.print("After withdrawal: ");
        displayAccount(account5);


        withdrawalResult =
                account5.withdraw(900, 1234);

        if (withdrawalResult) {

            System.out.println(
                    "Withdrawing ₹900.0: SUCCESS"
            );

        } else {

            System.out.println(
                    "Withdrawing ₹900.0 (would leave ₹-100): "
                            + "FAILED (Minimum balance violation)"
            );
        }

        System.out.println(
                "Current balance: ₹" + account5.getBalance()
        );


        // =========================================================
        // TEST 6
        // =========================================================

        System.out.println(
                "\n>>> Test 6: Account Status Management"
        );

        AccountEnhanced account6 =
                new AccountEnhanced(
                        1006, "Charlie Green", 35, 2000, "Savings"
                );

        System.out.print("Initial: ");
        displayAccount(account6);


        boolean closeResult =
                account6.closeAccount();

        if (closeResult) {

            System.out.println("Closing account: SUCCESS");

        } else {

            System.out.println("Closing account: FAILED");
        }


        System.out.print("After close: ");
        displayAccount(account6);


        boolean depositResult =
                account6.deposit(500);

        if (depositResult) {

            System.out.println(
                    "Depositing ₹500.0 to closed account: SUCCESS"
            );

        } else {

            System.out.println(
                    "Depositing ₹500.0 to closed account: "
                            + "FAILED (Account inactive)"
            );
        }


        boolean reopenResult =
                account6.reopenAccount();

        if (reopenResult) {

            System.out.println("Reopening account: SUCCESS");

        } else {

            System.out.println("Reopening account: FAILED");
        }


        System.out.print("After reopen: ");
        displayAccount(account6);


        // =========================================================
        // TEST 7
        // =========================================================

        System.out.println("\n>>> Test 7: PIN Protection");


        AccountEnhanced account7 =
                new AccountEnhanced(
                        1007, "Diana Prince", 28, 1500, "Savings"
                );


        // Set PIN
        boolean pinResult =
                account7.setPin(1234);

        if (pinResult) {

            System.out.println(
                    "Setting PIN 1234: SUCCESS"
            );

        } else {

            System.out.println(
                    "Setting PIN 1234: FAILED"
            );
        }


        // Correct PIN
        boolean correctPinWithdrawal =
                account7.withdraw(200, 1234);

        if (correctPinWithdrawal) {

            System.out.println(
                    "Withdrawing ₹200.0 with correct PIN (1234): SUCCESS"
            );

        } else {

            System.out.println(
                    "Withdrawing ₹200.0 with correct PIN (1234): FAILED"
            );
        }

        System.out.println(
                "New balance: ₹" + account7.getBalance()
        );


        // Incorrect PIN
        boolean incorrectPinWithdrawal =
                account7.withdraw(100, 9999);

        if (incorrectPinWithdrawal) {

            System.out.println(
                    "Withdrawing ₹100.0 with incorrect PIN (9999): SUCCESS"
            );

        } else {

            System.out.println(
                    "Withdrawing ₹100.0 with incorrect PIN (9999): "
                            + "FAILED (Incorrect PIN)"
            );
        }


        // PIN not set
        AccountEnhanced account8 =
                new AccountEnhanced(
                        1008, "No Pin User", 30, 1000, "Savings"
                );

        boolean noPinWithdrawal =
                account8.withdraw(100, 1234);

        if (noPinWithdrawal) {

            System.out.println(
                    "Withdrawing ₹100.0 with PIN not set: SUCCESS"
            );

        } else {

            System.out.println(
                    "Withdrawing ₹100.0 with PIN not set: "
                            + "FAILED (PIN not set)"
            );
        }


        // =========================================================
        // TEST 8
        // =========================================================

        System.out.println("\n>>> Test 8: All Accounts Summary\n");

        displayAccount(account1);
        System.out.println();

        displayAccount(account2);
        System.out.println();

        displayAccount(account3);
        System.out.println();

        displayAccount(account4);
        System.out.println();

        displayAccount(account5);
        System.out.println();

        displayAccount(account6);
        System.out.println();

        displayAccount(account7);


        System.out.println(
                "\n============================================================"
        );

        System.out.println(
                "                 ENHANCED TEST COMPLETED!"
        );

        System.out.println(
                "============================================================"
        );
    }
}