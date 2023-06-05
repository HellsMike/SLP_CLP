package ast.nodes;

import ast.types.BoolType;
import ast.types.ErrorType;
import ast.types.Type;
import utils.CodeGenSupport;
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
        String labelEnd = CodeGenSupport.newLabel();
        String labelTrue = CodeGenSupport.newLabel();

        return exp.codeGeneration() +
                "storei T1 1 \n" +
                "beq A0 T1 " + labelTrue + "\n" +
                "storei A0 1 \n" +
                "b " + labelEnd + "\n" +
                labelTrue + ": \n" +
                "storei A0 0 \n" +
                labelEnd + ": \n";
    }

    @Override
    public String toPrint(int tab) {
        return "  ".repeat(tab) + "Not\n" + exp.toPrint(tab + 1);
    }
}
