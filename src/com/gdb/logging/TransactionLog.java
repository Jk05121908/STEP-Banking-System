package com.gdb.logging;

import com.gdb.command.TransactionCommand;
import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class TransactionLog {
    private final File logFile = new File("data/transactions.ser");

    public void log(TransactionCommand command) throws IOException {
        File parent = logFile.getParentFile();
        if (parent != null) {
            parent.mkdirs();
        }

        boolean append = logFile.exists() && logFile.length() > 0;

        try (FileOutputStream fos = new FileOutputStream(logFile, true);
             ObjectOutputStream out = append
                     ? new AppendableObjectOutputStream(fos)
                     : new ObjectOutputStream(fos)) {
            out.writeObject(command);
            out.flush();
        }
    }

    public List<TransactionCommand> readAll()
            throws IOException, ClassNotFoundException {
        List<TransactionCommand> history = new ArrayList<>();

        if (!logFile.exists() || logFile.length() == 0) {
            return history;
        }

        try (ObjectInputStream in =
                     new ObjectInputStream(new FileInputStream(logFile))) {
            while (true) {
                try {
                    Object object = in.readObject();
                    if (object instanceof TransactionCommand) {
                        history.add((TransactionCommand) object);
                    }
                } catch (EOFException e) {
                    break;
                }
            }
        }

        return history;
    }

    public void clear() throws IOException {
        if (logFile.exists() && !logFile.delete()) {
            throw new IOException("Could not delete transaction log.");
        }
    }

    private static class AppendableObjectOutputStream
            extends ObjectOutputStream {
        AppendableObjectOutputStream(OutputStream out) throws IOException {
            super(out);
        }

        protected void writeStreamHeader() throws IOException {
            reset();
        }
    }
}
