package utils;

import ast.types.Type;

public class STEntry {
    /**
     * Type of the entry.
     */
    private final Type type;

    /**
     * Level of nesting in the symbol table scopes.
     */
    private final int nestingLevel;

    public STEntry(Type type, int nestingLevel) {
        this.type = type;
        this.nestingLevel = nestingLevel;
    }

    public Type getType() {
        return type;
    }

    public int getNesting() {
        return nestingLevel;
    }
}
