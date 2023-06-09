package utils;

import java.util.*;

import ast.types.Type;

public class SymbolTable {
    /**
     * List of hash maps that represents a symbol table. Hash map: <key -> String for id, value -> STEntry object>.
     */
    private final ArrayList<HashMap<String, STEntry>> table;
    /**
     * Offset list.
     */
    private final ArrayList<Integer> offsetList;

    public SymbolTable() {
        this.table = new ArrayList<>();
        this.offsetList = new ArrayList<>();
    }

    /**
     * Create a deep copy of the symbol table.
     *
     * @param symbolTable Original symbol table.
     */
    public SymbolTable(SymbolTable symbolTable) {
        this.table = new ArrayList<>();

        for (HashMap<String, STEntry> map : symbolTable.table) {
            HashMap<String, STEntry> newMap = new HashMap<>();

            for (Map.Entry<String, STEntry> entry : map.entrySet())
                newMap.put(entry.getKey(), new STEntry(entry.getValue()));

            this.table.add(newMap);
        }

        this.offsetList = new ArrayList<>(symbolTable.offsetList);
    }


    /**
     * Check for inner declaration of entry in the symbol table.
     *
     * @param id Identifier to check for in the symbol table.
     * @return STEntry object if found, otherwise null.
     */
    public STEntry lookup(String id) {
        // Reverse search for find the inner occurrence first
        for (int i = table.size() - 1; i >= 0; i--) {
            HashMap<String, STEntry> scope = table.get(i);

            if (scope.containsKey(id))
                return scope.get(id);
        }

        return null;
    }

    /**
     * Check if param 'id' already exists in the symbol table. Optimized version that check in just one given scope.
     *
     * @param id Identifier to check for in the symbol table.
     * @param nestingLevel Nesting level of the scope where to search.
     * @return STEntry object if found, otherwise null.
     */
    public STEntry lookup(String id, int nestingLevel) {
        HashMap<String, STEntry> scope = table.get(nestingLevel);

        return scope.getOrDefault(id, null);
    }

    /**
     * Add a new entry to the symbol table inner scope; used for variables.
     *
     * @param id Identifier of the entry.
     * @param type Type of the entry.
     * @return STEntry generated.
     */
    public STEntry add(String id, Type type) {
        int lastIndex = table.size() - 1;
        int offset = offsetList.get(lastIndex);
        STEntry entry = new STEntry(type, lastIndex, offset);
        HashMap<String,STEntry> scope = table.get(lastIndex);
        scope.put(id, entry);
        table.set(lastIndex, scope);
        offsetList.set(lastIndex, (offset + 1));

        return entry;
    }

    /**
     * Add a new entry to the symbol table inner scope; used for functions.
     *
     * @param id Identifier of the entry.
     * @param type Type of the entry.
     * @param label Function label for code generation.
     * @return STEntry generated.
     */
    public STEntry add(String id, Type type, String label) {
        int lastIndex = table.size() - 1;
        int offset = offsetList.get(lastIndex);
        STEntry entry = new STEntry(type, lastIndex, offset, label);
        HashMap<String,STEntry> scope = table.get(lastIndex);
        scope.put(id, entry);
        table.set(lastIndex, scope);
        offsetList.set(lastIndex, (offset + 1));

        return entry;
    }

    /**
     * Enter a new scope.
     *
     * @return Nesting level of the new scope.
     */
    public int newScope() {
        table.add(new HashMap<>());
        // Start from 1 to leave space for AL
        offsetList.add(1);

        return table.size() - 1;
    }

    /**
     * Exits the current scope.
     */
    public void exitScope() {
        table.remove(table.size() - 1);
    }

    /**
     * Get a list of identifiers of initialized entries.
     *
     * @return List of initialized entries.
     */
    public ArrayList<String> getInitializedEntries() {
        ArrayList<String> entryList = new ArrayList<>();

        for (HashMap<String, STEntry> scope : table)
            for (String key : scope.keySet())
                if (scope.get(key).isInitialized())
                    entryList.add(key);

        return entryList;
    }

    /**
     * Increase the offset of the inner scope.
     */
    public void increaseOffset() {
        int lastIndex = offsetList.size() - 1 ;
        offsetList.set(lastIndex, offsetList.get(lastIndex) + 1) ;
    }
}
