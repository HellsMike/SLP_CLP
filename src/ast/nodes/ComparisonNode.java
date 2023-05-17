package ast.nodes;

import ast.types.BoolType;
import ast.types.ErrorType;
import ast.types.IntType;
import ast.types.Type;
import utils.SemanticError;
import utils.SymbolTable;

import java.util.ArrayList;

public class ComparisonNode implements Node {
    private final Node left;
    private final Node right;

    public ComparisonNode(Node left, Node right) {
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
        errors.addAll(left.checkSemantics(symbolTable, nestingLevel));
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
        if (left.typeCheck().getClass().equals(right.typeCheck().getClass()))
            return new BoolType();
        else {
            ErrorType error = new ErrorType("Type Error: Different types in comparison operation.");
            System.out.println(error);
            return error;
        }
    }

    /**
     * Bytecode generation for interpreter.
     *
     * @return Code.
     */
    @Override
    public String codeGeneration() {
        return "";
    }

    @Override
    public String toString(String string) {
        return string + "Comparison\n" + left.toString(string + "  ") + right.toString(string + "  ");
    }
}
