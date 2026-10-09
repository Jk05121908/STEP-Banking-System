package com.gdb.logging;

import com.gdb.command.TransactionCommand;
import java.io.IOException;
import java.util.List;

public class FileLogDestination implements LogDestination {
    private TransactionLog log;

    public FileLogDestination() {
        this.log = new TransactionLog();
    }

    @Override
    public void write(TransactionCommand cmd) {
        try {
            log.log(cmd);
        } catch (IOException e) {
            throw new RuntimeException("Unable to write file log", e);
        }
    }

    @Override
    public List<TransactionCommand> readAll() {
        try {
            return log.readAll();
        } catch (IOException | ClassNotFoundException e) {
            throw new RuntimeException("Unable to read file log", e);
        }
    }

    @Override
    public void clear() {
        try {
            log.clear();
        } catch (IOException e) {
            throw new RuntimeException("Unable to clear file log", e);
        }
    }

    @Override
    public String getDestinationName() {
        return "FILE";
    }
}
