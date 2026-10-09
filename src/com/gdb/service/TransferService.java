package com.gdb.service;

import com.gdb.domain.AbstractAccount;
import com.gdb.exceptions.AccountException;
import com.gdb.exceptions.InactiveAccountException;
import com.gdb.exceptions.InsufficientBalanceException;
import com.gdb.exceptions.InvalidPinException;

public class TransferService {

    public static void transfer(
            AbstractAccount from,
            AbstractAccount to,
            double amount,
            int pin) throws AccountException {

        // 1. Check accounts exist
        if (from == null || to == null) {
            throw new AccountException(
                    "Source and destination accounts are required");
        }

        // 2. Check both accounts are active
        if (!from.isActive() || !to.isActive()) {
            throw new InactiveAccountException(
                    "Both accounts must be active to transfer funds");
        }

        // 3. Verify sender PIN
        if (!from.verifyPin(pin)) {
            throw new InvalidPinException("Incorrect PIN");
        }

        // 4. Check available balance and account withdrawal rules
        if (!from.canWithdraw(amount)) {
            throw new InsufficientBalanceException(
                    "Insufficient balance for transfer of Rs. " + amount);
        }

        // 5. Check daily limit before moving any money
        from.resetDailyTransferIfNeeded();

        if (!from.canTransfer(amount)) {
            throw new AccountException(
                    "Daily transfer limit exceeded. Remaining today: Rs. "
                            + from.getRemainingDailyTransferLimit());
        }

        // 6. Debit sender
        from.withdraw(amount, pin);

        // 7. Credit receiver
        to.deposit(amount);

        // 8. Record the transfer against the sender's daily limit
        from.updateDailyTransferTotal(amount);
    }
}