package ast;

import java.util.ArrayList;

import utils.*;

public class Type implements Node {
    public String toPrint(String string) {
        return string;
    }

    @Override
    public ArrayList<SemanticError> checkSemantics(SymbolTable symbolTable, int nestingLevel) {
        return null;
    }

    @Override
    public Type typeCheck() {
        return null;
    }

    @Override
    public String codeGeneration() {
        return "";
    }
}
