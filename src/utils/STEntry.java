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
     * Function label for code generation.
     */
    private final String label;
    /**
     * Keep track of the initialization state.
     */
    private boolean initialized;
    /**
     * Offset to frame pointer.
     */
    private final int offset;

    public STEntry(Type type, int nestingLevel, int offset) {
        this.type = type;
        this.nestingLevel = nestingLevel;
        this.offset = offset;
        this.label = null;

        // If entry is a function, mark as initialized
        initialized = type instanceof FunType;
    }

    public STEntry(Type type, int nestingLevel, int offset, String label) {
        this.type = type;
        this.nestingLevel = nestingLevel;
        this.offset = offset;
        this.label = label;

        // If entry is a function, mark as initialized
        initialized = type instanceof FunType;
    }

    public Type getType() {
        return type;
    }

    public int getNesting() {
        return nestingLevel;
    }

    public int getOffset() {
        return offset;
    }

    public String getLabel() {
        return label;
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
