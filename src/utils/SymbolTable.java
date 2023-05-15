package utils;

import java.util.*;

import ast.Type;

public class SymbolTable {
    /**
     * List of hash maps that represents a symbol table. Hash map: <key -> String for id, value -> STEntry object>
     */
    private final ArrayList<HashMap<String,STEntry>> table;

    public SymbolTable() {
        this.table = new ArrayList<>();
    }

    /**
     * Check if param 'id' already exists in the symbol table.
     * @param id Identifier to check for in the symbol table.
     * @return STEntry object if found, otherwise null.
     */
    public STEntry lookup (String id) {
        for (HashMap<String, STEntry> scope : table) {
            if (scope.containsKey(id)) {
                return scope.get(id);
            }
        }

        return null;
    }

    /**
     * Add a new entry to the symbol table.
     * @param id Identifier of the entry.
     * @param type Type of the entry.
     * @param nestingLevel Level of scope where the entry have to be inserted.
     */
    public void add (String id, Type type, int nestingLevel) {
        STEntry entry = new STEntry(type, nestingLevel);
        int lastIndex = this.table.size() - 1;
        HashMap<String,STEntry> scope = this.table.get(lastIndex);
        scope.put(id, entry);
        this.table.set(lastIndex, scope);
    }

    /**
     * Enter a new scope.
     * @param scope HashMap of the new scope.
     */
    public void newScope(HashMap<String,STEntry> scope) {
        this.table.add(scope);
    }

    /**
     * Exits the current scope.
     */
    public void exitScope() {
        this.table.remove(this.table.size() - 1);
    }
}
