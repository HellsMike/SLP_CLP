package ast.nodes;

import ast.types.BoolType;
import ast.types.ErrorType;
import ast.types.Type;
import utils.SemanticError;
import utils.SymbolTable;

import java.util.ArrayList;

public class IfExpNode implements Node {
    private final Node condition;
    private final Node thenBranch;
    private final Node elseBranch;

    public IfExpNode(Node condition, Node thenBranch, Node elseBranch) {
        this.condition = condition;
        this.thenBranch = thenBranch;
        this.elseBranch = elseBranch;
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
        errors.addAll(condition.checkSemantics(symbolTable, nestingLevel));
        errors.addAll(thenBranch.checkSemantics(symbolTable, nestingLevel));
        errors.addAll(elseBranch.checkSemantics(symbolTable, nestingLevel));
        return errors;
    }

    /**
     * Determines the type of the token.
     *
     * @return Type class of the corresponding node type.
     */
    @Override
    public Type typeCheck() {
        if (condition.typeCheck() instanceof BoolType) {

        } else {
            ErrorType error = new ErrorType("Type error: non boolean condition in if expression.");
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
        return null;
    }

    @Override
    public String toString(String string) {
        return null;
    }
}