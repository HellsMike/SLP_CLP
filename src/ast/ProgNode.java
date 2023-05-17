package ast;

import java.util.ArrayList;

import utils.*;

public class ProgNode implements Node {
    private final Node exp;

    public ProgNode (Node exp) {
        this.exp = exp;
    }

    @Override
    public ArrayList<SemanticError> checkSemantics(SymbolTable symbolTable, int nestingLevel) {
        return exp.checkSemantics(symbolTable, nestingLevel);
    }

    @Override
    public Type typeCheck() {
        return exp.typeCheck();
    }

    @Override
    public String codeGeneration() {
        return "";
    }

    @Override
    public String toPrint(String s) {
        return "Prog\n" + exp.toPrint("  ");
    }
}
