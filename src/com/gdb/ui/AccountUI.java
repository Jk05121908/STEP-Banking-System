package com.gdb.ui;

import com.gdb.domain.AbstractAccount;
import com.gdb.domain.IAccount;
import com.gdb.domain.Transaction;
import com.gdb.exceptions.AccountException;
import com.gdb.service.AccountService;

import java.util.List;
import java.util.Scanner;

public class AccountUI {
    private final AccountService service;
    private final Scanner scanner;

    public AccountUI(AccountService service) {
        this(service, new Scanner(System.in));
    }

    public AccountUI(AccountService service, Scanner scanner) {
        this.service = service;
        this.scanner = scanner;
    }

    public void run() {
        boolean running = true;

        while (running) {
            printMenu();
            int choice = readInt("Choose an option: ");

            try {
                switch (choice) {
                    case 1: openAccount(); break;
                    case 2: deposit(); break;
                    case 3: withdraw(); break;
                    case 4: transfer(); break;
                    case 5: showAccount(); break;
                    case 6: showHistory(); break;
                    case 7: closeAccount(); break;
                    case 0:
                        running = false;
                        System.out.println(
                                "Thank you for using STEP Banking System!");
                        break;
                    default:
                        System.out.println("Invalid option.");
                }
            } catch (AccountException | IllegalArgumentException ex) {
                System.out.println("Operation failed: " + ex.getMessage());
            } catch (RuntimeException ex) {
                System.out.println("Unexpected error: " + ex.getMessage());
            }

            System.out.println();
        }
    }

    private void printMenu() {
        System.out.println("========================================");
        System.out.println("       STEP BANKING SYSTEM");
        System.out.println("========================================");
        System.out.println("1. Open account");
        System.out.println("2. Deposit");
        System.out.println("3. Withdraw");
        System.out.println("4. Transfer funds");
        System.out.println("5. View account");
        System.out.println("6. Transaction history");
        System.out.println("7. Close account");
        System.out.println("0. Exit");
    }

    private void openAccount() {
        System.out.print(
                "Account type (SAVINGS/CURRENT/FIXED_DEPOSIT/SALARY): ");
        String type = scanner.nextLine().trim();

        System.out.print("Customer name: ");
        String name = scanner.nextLine().trim();

        int age = readInt("Age: ");
        double balance = readDouble("Initial balance: Rs. ");

        IAccount account =
                service.openAccount(type, name, age, balance);

        int pin = readInt("Set a 4-digit PIN: ");
        account.setPin(pin);

        System.out.println("Account opened: #"
                + account.getAccountNumber()
                + " | " + account.getName()
                + " | " + account.getAccountType()
                + " | Balance Rs. " + account.getBalance());
    }

    private void deposit() throws AccountException {
        int number = readInt("Account number: ");
        double amount = readDouble("Amount to deposit: Rs. ");
        Transaction transaction = service.deposit(number, amount);
        System.out.println(transaction);
    }

    private void withdraw() throws AccountException {
        int number = readInt("Account number: ");
        double amount = readDouble("Amount to withdraw: Rs. ");
        int pin = readInt("PIN: ");
        System.out.println(service.withdraw(number, amount, pin));
    }

    private void transfer() throws AccountException {
        int from = readInt("Source account number: ");
        int to = readInt("Destination account number: ");
        double amount = readDouble("Amount to transfer: Rs. ");
        int pin = readInt("Source account PIN: ");
        System.out.println(service.transfer(from, to, amount, pin));
    }

    private void showAccount() {
        int number = readInt("Account number: ");
        IAccount account = service.getAccount(number);

        if (account == null) {
            System.out.println("Account not found: " + number);
        } else if (account instanceof AbstractAccount) {
            ((AbstractAccount) account).displayAccountInfo();
        } else {
            System.out.println("Account #" + account.getAccountNumber()
                    + " | " + account.getName()
                    + " | Balance Rs. " + account.getBalance());
        }
    }

    private void showHistory() {
        List<Transaction> history = service.getTransactionHistory();

        if (history.isEmpty()) {
            System.out.println("No transactions recorded yet.");
            return;
        }

        System.out.println("Transaction history ("
                + history.size() + " records):");

        for (int i = 0; i < history.size(); i++) {
            System.out.println("[" + (i + 1) + "] " + history.get(i));
        }
    }

    private void closeAccount() throws AccountException {
        int number = readInt("Account number: ");
        int pin = readInt("PIN: ");
        service.closeAccount(number, pin);
        System.out.println("Account #" + number + " closed successfully.");
    }

    private int readInt(String prompt) {
        while (true) {
            System.out.print(prompt);
            String value = scanner.nextLine().trim();

            try {
                return Integer.parseInt(value);
            } catch (NumberFormatException ex) {
                System.out.println("Please enter a whole number.");
            }
        }
    }

    private double readDouble(String prompt) {
        while (true) {
            System.out.print(prompt);
            String value = scanner.nextLine().trim();

            try {
                return Double.parseDouble(value);
            } catch (NumberFormatException ex) {
                System.out.println("Please enter a numeric amount.");
            }
        }
    }
}
