package ast.nodes;

import ast.types.BoolType;
import ast.types.ErrorType;
import ast.types.Type;
import utils.SemanticError;
import utils.SymbolTable;

import java.util.ArrayList;

/**
 * Node for logical operator "||".
 */
public class OrNode implements Node {
    private final Node left;
    private final Node right;

    public OrNode(Node left, Node right) {
        this.left = left;
        this.right = right;
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
        ArrayList<SemanticError> errors = new ArrayList<>();
        // Check for left expression semantic errors
        errors.addAll(left.checkSemantics(symbolTable, nestingLevel));
        // Check for right expression semantic errors
        errors.addAll(right.checkSemantics(symbolTable, nestingLevel));

        return errors;
    }

    /**
     * Determines the type of the token.
     *
     * @return Type class of the corresponding node type.
     */
    @Override
    public Type typeCheck() {
        // Check if both operands are booleans
        if (left.typeCheck() instanceof BoolType && right.typeCheck() instanceof BoolType)
            return new BoolType();
        else
            return new ErrorType("Type Error: Non booleans in or operation.");
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
        return string + "Or\n" + left.toPrint(string + "  ") + right.toPrint(string + "  ");
    }
}
