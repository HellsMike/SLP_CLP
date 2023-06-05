package ast.types;

import ast.nodes.Node;
import utils.*;

import java.util.ArrayList;

public abstract class Type implements Node {
    public boolean isEqual(Type comparisonType) {
        return comparisonType.getClass().equals(this.getClass());
    }

    @Override
    public abstract String toPrint(int tab);

    /**
     * DON'T USE! NOT IMPLEMENTED.
     *
     * @param symbolTable Symbol table to check in.
     * @param nestingLevel Level of scope the node is in.
     * @return Null.
     */
    @Override
    public ArrayList<SemanticError> checkSemantics(SymbolTable symbolTable, int nestingLevel) {
        return null;
    }

    /**
     * DON'T USE! NOT IMPLEMENTED.
     *
     * @return Null.
     */
    @Override
    public Type typeCheck() {
        return null;
    }

    /**
     * DON'T USE! NOT IMPLEMENTED.
     *
     * @return Empty string.
     */
    @Override
    public String codeGeneration() {
        return "";
    }
}
