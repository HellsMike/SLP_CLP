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
     */
    public void add(String id, Type type) {
        int lastIndex = table.size() - 1;
        int offset = offsetList.get(lastIndex);
        STEntry entry = new STEntry(type, lastIndex, offset);
        HashMap<String,STEntry> scope = table.get(lastIndex);
        scope.put(id, entry);
        table.set(lastIndex, scope);
        offsetList.set(lastIndex, offset + 1);
    }

    /**
     * Add a new entry to the symbol table inner scope; used for functions.
     *
     * @param id Identifier of the entry.
     * @param type Type of the entry.
     * @param label Function label for code generation.
     */
    public void add(String id, Type type, String label) {
        int lastIndex = table.size() - 1;
        int offset = offsetList.get(lastIndex);
        STEntry entry = new STEntry(type, lastIndex, offset, label);
        HashMap<String,STEntry> scope = table.get(lastIndex);
        scope.put(id, entry);
        table.set(lastIndex, scope);
        offsetList.set(lastIndex, offset + 1);
    }

    /**
     * Enter a new scope.
     *
     * @return Nesting level of the new scope.
     */
    public int newScope() {
        table.add(new HashMap<>());
        // Leave one offset slot free for AL
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
     * Mark the entry as initialized and update the symbol table.
     *
     * @param entry The entry to initialize.
     * @return Updated entry.
     */
    public STEntry initializeEntry(STEntry entry) {
        HashMap<String, STEntry> scope = table.get(entry.getNesting());
        int lastIndex = table.size() - 1;

        // Get the id of the entry
        for (String key : scope.keySet())
            if (scope.get(key).equals(entry)) {
                // Insert the initialized entry in the inner scope for visibility reason -> the entry will be seen as
                // initialized only in the inner scope and those created later
                if (entry.getNesting() == lastIndex)
                    entry.initialize();
                else {
                    // A copy is needed to prevent values in the other scopes from being changed as well
                    STEntry newEntry = new STEntry(entry.getType(), lastIndex, entry.getOffset());
                    newEntry.initialize();
                    table.get(lastIndex).put(key, newEntry);
                }

                break;
            }

        table.set(entry.getNesting(), scope);

        return entry;
    }

    /**
     * Increase the offset of the inner scope.
     */
    public void increaseOffset() {
        int lastIndex = offsetList.size() - 1 ;
        offsetList.set(lastIndex, offsetList.get(lastIndex) + 1) ;
    }
}
