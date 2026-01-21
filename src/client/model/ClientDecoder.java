package client.model;

import java.util.HashMap;
import java.util.Map;

public class ClientDecoder {
    private final Map<Integer, String> broadcastTable;

    public ClientDecoder() {
        broadcastTable = loadInitialTable();
    }

    private Map<Integer, String> loadInitialTable() {
        Map<Integer, String> table = new HashMap<>();

        for (int i = 0; i < 256; i++) {
            table.put(i, (char) i + "");
        }
        return table;
    }

    public boolean valueExists(String value) {
        return broadcastTable.containsValue(value);
    }

    public void addEntry(String value) {
        broadcastTable.put(broadcastTable.size(), value);
    }

    public String getValueCode(int code) {
        return broadcastTable.get(code);
    }

    public Map<Integer, String> getTableCopy() {
        return new HashMap<>(broadcastTable);
    }
}
