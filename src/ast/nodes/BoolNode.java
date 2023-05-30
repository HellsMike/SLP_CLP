package ast.nodes;

import ast.types.BoolType;
import ast.types.Type;
import utils.SemanticError;
import utils.SymbolTable;

import java.util.ArrayList;

public class BoolNode implements Node {
    private final boolean value;

    public BoolNode (boolean value) {
        this.value = value;
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
        return new ArrayList<>();
    }

    /**
     * Determines the type of the token.
     *
     * @return Type class of the corresponding node type.
     */
    @Override
    public Type typeCheck() {
        return new BoolType();
    }

    /**
     * Bytecode generation for interpreter.
     *
     * @return Code.
     */
    @Override
    public String codeGeneration() {
        return "storei A0 " + (value ? 1 : 0) + "\n";
    }

    @Override
    public String toPrint(String string) {
        return string + value + "\n";
    }
}
