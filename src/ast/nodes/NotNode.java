package ast.nodes;

import ast.types.BoolType;
import ast.types.ErrorType;
import ast.types.Type;
import utils.SemanticError;
import utils.SymbolTable;

import java.util.ArrayList;

/**
 * Node for logical operator "!".
 */
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
        // Check for expression semantic errors
        return new ArrayList<>(exp.checkSemantics(symbolTable, nestingLevel));
    }

    /**
     * Determines the type of the token.
     *
     * @return Type class of the corresponding node type.
     */
    @Override
    public Type typeCheck() {
        // Check if expression is boolean
        if (exp.typeCheck() instanceof BoolType)
            return new BoolType();
        else
            return new ErrorType("Type Error: Non booleans in not operation.");
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
    public String toPrint(String string) {
        return string + "Not\n" + exp.toPrint(string + "  ");
    }
}
