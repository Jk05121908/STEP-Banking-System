package com.gdb.logging;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SimulatedDatabase {
    private final Map<String, List<Object>> tables = new HashMap<>();

    public synchronized void insert(String table, Object value) {
        tables.computeIfAbsent(table, ignored -> new ArrayList<>())
              .add(value);
    }

    public synchronized List<Object> selectAll(String table) {
        return new ArrayList<>(
                tables.getOrDefault(table, new ArrayList<>()));
    }

    public synchronized void deleteAll(String table) {
        tables.remove(table);
    }
}
