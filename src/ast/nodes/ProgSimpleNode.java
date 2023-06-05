package ast.nodes;

import ast.types.Type;
import utils.SemanticError;
import utils.SymbolTable;

import java.util.ArrayList;

/**
 * Node for program with expression.
 */
public class ProgSimpleNode implements Node {
    private final Node exp;

    public ProgSimpleNode(Node exp) {
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
        // Generate global scope
        symbolTable.newScope();
        // Check for expression semantic errors
        ArrayList<SemanticError> errors = new ArrayList<>(exp.checkSemantics(symbolTable, nestingLevel));
        // Exit global scope
        symbolTable.exitScope();

        return errors;
    }

    /**
     * Determines the type of the token.
     *
     * @return Type class of the corresponding node type.
     */
    @Override
    public Type typeCheck() {
        // Check for expression type
        return exp.typeCheck();
    }

    /**
     * Bytecode generation for interpreter.
     *
     * @return Code.
     */
    @Override
    public String codeGeneration() {
        return "pushr FP \n" +
                "pushr AL \n" +
                exp.codeGeneration() +
                "halt \n";
    }

    @Override
    public String toPrint(int tab) {
        return "Prog\n" + exp.toPrint(tab + 1);
    }
}
