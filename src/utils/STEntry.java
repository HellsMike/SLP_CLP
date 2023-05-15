package utils;

import ast.Type;

public class STEntry {
    /**
     * Type of the entry.
     */
    private final Type type;

    /**
     * Level of nesting in the symbol table scopes.
     */
    private final int nestingLevel;

    public STEntry(Type _type, int _nesting) {
        this.type = _type;
        this.nestingLevel = _nesting;
    }

    public Type getType() {
        return type;
    }

    public int getNesting() {
        return nestingLevel;
    }
}
