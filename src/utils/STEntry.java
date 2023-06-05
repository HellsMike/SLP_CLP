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
     * Offset to frame pointer.
     */
    private final int offset;
    /**
     * Keep track of the initialization state.
     */
    private boolean initialized;
    /**
     * Keep track if variable is initialized in a if branch.
     */
    private boolean conditionInitializedWarning = false;

    /**
     * Constructor for variable entries.
     *
     * @param type Type of the entry.
     * @param nestingLevel Nesting level of the entry declaration.
     * @param offset Offset from AL for code generation.
     */
    public STEntry(Type type, int nestingLevel, int offset) {
        this.type = type;
        this.nestingLevel = nestingLevel;
        this.offset = offset;
        this.label = null;

        // If entry is a function, mark as initialized
        initialized = type instanceof FunType;
    }

    /**
     * Constructor for function entries.
     *
     * @param type Type of the entry.
     * @param nestingLevel Nesting level of the entry declaration.
     * @param offset Offset from AL for code generation.
     * @param label Label of the function for code generation.
     */
    public STEntry(Type type, int nestingLevel, int offset, String label) {
        this.type = type;
        this.nestingLevel = nestingLevel;
        this.offset = offset;
        this.label = label;

        // If entry is a function, mark as initialized
        initialized = type instanceof FunType;
    }

    /**
     * Create a deep copy of the entry.
     *
     * @param entry Original entry.
     */
    public STEntry(STEntry entry) {
        this.type = entry.type;
        this.nestingLevel = entry.nestingLevel;
        this.offset = entry.offset;
        this.label = entry.label;
        this.initialized = entry.initialized;
        this.conditionInitializedWarning = entry.conditionInitializedWarning;
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

    /**
     * Check if entry is initialized only in one of the if branches.
     *
     * @return true if entry has condition warning, false otherwise.
     */
    public boolean hasConditionWarning() {
        return conditionInitializedWarning;
    }

    /**
     * Mark entry with condition warning.
     */
    public void markConditionWarning() {
        conditionInitializedWarning = true;
        initialized = true;
    }
}
