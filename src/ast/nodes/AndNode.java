package ast.nodes;

import ast.types.BoolType;
import ast.types.ErrorType;
import ast.types.Type;
import utils.CodeGenSupport;
import utils.SemanticError;
import utils.SymbolTable;

import java.util.ArrayList;

/**
 * Node for logical operator "&&".
 */
public class AndNode implements Node {
    private final Node left;
    private final Node right;

    public AndNode(Node left, Node right) {
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
            return new ErrorType("Type Error: Non booleans in and operation.");
    }

    /**
     * Bytecode generation for interpreter.
     *
     * @return Code.
     */
    @Override
    public String codeGeneration() {
        String labelEnd = CodeGenSupport.newLabel();

        return left.codeGeneration() +
                "storei T1 0 \n" +
                "beq A0 T1 " + labelEnd + "\n" +
                right.codeGeneration() +
                labelEnd + ": \n";
    }

    @Override
    public String toPrint(int tab) {
        return "  ".repeat(tab) + "And\n" + left.toPrint(tab + 1) + right.toPrint(tab + 1);
    }
}
