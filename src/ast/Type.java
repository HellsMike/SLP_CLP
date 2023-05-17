package ast;

import java.util.ArrayList;

import utils.*;

public class Type implements Node {
    public String toPrint(String string) {
        return string;
    }

    /**
     * DON'T USE! NOT IMPLEMENTED.
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
     * @return Null.
     */
    @Override
    public Type typeCheck() {
        return null;
    }

    /**
     * DON'T USE! NOT IMPLEMENTED.
     * @return Empty string.
     */
    @Override
    public String codeGeneration() {
        return "";
    }
}
