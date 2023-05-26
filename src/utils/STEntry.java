package utils;

import ast.types.FunType;
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
    /**
     * Keep track of the initialization state.
     */
    private boolean initialized;

    public STEntry(Type type, int nestingLevel) {
        this.type = type;
        this.nestingLevel = nestingLevel;

        // If entry is a function, mark as initialized
        initialized = type instanceof FunType;
    }

    public Type getType() {
        return type;
    }

    public int getNesting() {
        return nestingLevel;
    }

    /**
     * Check if entry is initialized.
     *
     * @return true if entry is initialized, false otherwise.
     */
    public boolean isInitialized() {
        return initialized;
    }

    /**
     * Mark entry as initialized
     */
    public void initialize() {
        initialized = true;
    }
}
