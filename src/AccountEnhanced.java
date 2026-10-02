public class AccountEnhanced {

    // ===== Constants =====

    private static final double MIN_BALANCE_SAVINGS = 500.0;
    private static final double MIN_BALANCE_CURRENT = 1000.0;
    private static final int MIN_AGE = 18;
    private static final int MIN_PIN = 1000;
    private static final int MAX_PIN = 9999;


    // ===== Fields =====

    private int accountNumber;
    private String name;
    private int age;
    private double balance;
    private String accountType;
    private String status;
    private Integer pin;


    // ===== Constructor =====

    public AccountEnhanced(int accountNumber, String name, int age,
                           double initialBalance, String accountType) {

        this.accountNumber = accountNumber;
        this.name = name;

        // Age validation
        if (age < MIN_AGE) {
            this.age = MIN_AGE;
        } else {
            this.age = age;
        }


        // Account type validation
        if (accountType.equals("Savings")
                || accountType.equals("Current")) {

            this.accountType = accountType;

        } else {

            this.accountType = "Savings";
        }


        // Minimum balance on account creation
        if (this.accountType.equals("Savings")) {

            if (initialBalance < MIN_BALANCE_SAVINGS) {
                this.balance = MIN_BALANCE_SAVINGS;
            } else {
                this.balance = initialBalance;
            }

        } else {

            if (initialBalance < MIN_BALANCE_CURRENT) {
                this.balance = MIN_BALANCE_CURRENT;
            } else {
                this.balance = initialBalance;
            }
        }


        // New accounts start active
        this.status = "Active";

        // PIN initially not set
        this.pin = null;
    }


    // ===== Deposit =====

    public boolean deposit(double amount) {

        // Cannot deposit into inactive account
        if (status.equals("Inactive")) {
            return false;
        }

        // Amount must be positive
        if (amount <= 0) {
            return false;
        }

        balance += amount;

        return true;
    }


    // ===== Withdrawal =====

    public boolean withdraw(double amount, int pin) {

        // Account must be active
        if (status.equals("Inactive")) {
            return false;
        }

        // PIN must be correct
        if (!verifyPin(pin)) {
            return false;
        }

        // Amount must be positive
        if (amount <= 0) {
            return false;
        }

        // Cannot withdraw more than balance
        if (amount > balance) {
            return false;
        }

        balance -= amount;

        return true;
    }


    // ===== Account Status Management =====

    public boolean closeAccount() {

        // Already closed
        if (status.equals("Inactive")) {
            return false;
        }

        status = "Inactive";

        return true;
    }


    public boolean reopenAccount() {

        // Already active
        if (status.equals("Active")) {
            return false;
        }

        status = "Active";

        return true;
    }


    // ===== PIN Management =====

    public boolean setPin(int pin) {

        // PIN must be exactly 4 digits
        if (pin >= MIN_PIN && pin <= MAX_PIN) {

            this.pin = pin;

            return true;
        }

        return false;
    }


    public boolean verifyPin(int pin) {

        // PIN not set
        if (this.pin == null) {
            return false;
        }

        return this.pin == pin;
    }


    public boolean hasPin() {

        return pin != null;
    }


    // ===== Getters =====

    public int getAccountNumber() {
        return accountNumber;
    }

    public String getName() {
        return name;
    }

    public int getAge() {
        return age;
    }

    public double getBalance() {
        return balance;
    }

    public String getAccountType() {
        return accountType;
    }

    public String getStatus() {
        return status;
    }


    // ===== Setters =====

    public void setName(String name) {
        this.name = name;
    }

    public void setAge(int age) {

        if (age < MIN_AGE) {
            this.age = MIN_AGE;
        } else {
            this.age = age;
        }
    }

}
