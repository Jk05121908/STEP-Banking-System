package com.gdb.logging;

import com.gdb.command.TransactionCommand;
import java.util.ArrayList;
import java.util.List;

public class MemoryLogDestination implements LogDestination {
    private final List<TransactionCommand> commands = new ArrayList<>();

    @Override
    public synchronized void write(TransactionCommand cmd) {
        commands.add(cmd);
    }

    @Override
    public synchronized List<TransactionCommand> readAll() {
        return new ArrayList<>(commands);
    }

    @Override
    public synchronized void clear() {
        commands.clear();
    }

    @Override
    public String getDestinationName() {
        return "MEMORY";
    }
}
