package ast.nodes;

import ast.types.Type;
import utils.SemanticError;
import utils.SymbolTable;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class NotNode implements Node {
    private Node exp;

    public NotNode(Node exp) {
        this.exp = exp;
    }

    /**
     * Function invoked to check for semantic errors.
     *
     * @param symbolTable  Symbol table to check in.
     * @param nestingLevel Level of scope the node is in.
     * @return List of semantic error.
     */
    @Override
    public ArrayList<SemanticError> checkSemantics(SymbolTable symbolTable, int nestingLevel) {
        return new ArrayList<>(exp.checkSemantics(symbolTable, nestingLevel));
    }

    /**
     * Determines the type of the token.
     *
     * @return Type class of the corresponding node type.
     */
    @Override
    public Type typeCheck() {
        return exp.typeCheck();
    }

    /**
     * Bytecode generation for interpreter.
     *
     * @return Code.
     */
    @Override
    public String codeGeneration() {
        return null;
    }

    @Override
    public String toString(String string) {
        return string + "Not\n" + exp.toString(string);
    }
}
