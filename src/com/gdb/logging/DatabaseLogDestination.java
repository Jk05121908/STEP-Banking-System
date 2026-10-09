package com.gdb.logging;

import com.gdb.command.TransactionCommand;
import java.util.ArrayList;
import java.util.List;

public class DatabaseLogDestination implements LogDestination {
    private final SimulatedDatabase db;

    public DatabaseLogDestination(SimulatedDatabase db) {
        this.db = db;
    }

    @Override
    public void write(TransactionCommand cmd) {
        db.insert("transaction_log", cmd);
    }

    @Override
    public List<TransactionCommand> readAll() {
        List<?> rows = db.selectAll("transaction_log");
        List<TransactionCommand> result = new ArrayList<>();

        for (Object row : rows) {
            if (row instanceof TransactionCommand) {
                result.add((TransactionCommand) row);
            }
        }

        return result;
    }

    @Override
    public void clear() {
        db.deleteAll("transaction_log");
    }

    @Override
    public String getDestinationName() {
        return "DATABASE";
    }
}
