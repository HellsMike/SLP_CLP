package ast.nodes;

import ast.types.ErrorType;
import ast.types.Type;
import utils.SemanticError;
import utils.SymbolTable;

import java.util.ArrayList;

public class ProgComplexNode implements Node {
    private final ArrayList<Node> declarationList;
    private final ArrayList<Node> statementList;
    private final Node exp;

    public ProgComplexNode (ArrayList<Node> declarationList, ArrayList<Node> statementList, Node exp) {
        this.declarationList = declarationList;
        this.statementList = statementList;
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
        ArrayList<SemanticError> errors = new ArrayList<>();

        // Check for declarations semantic errors
        for (Node declaration: declarationList)
            errors.addAll(declaration.checkSemantics(symbolTable, nestingLevel));

        // Check for statements semantic errors
        for (Node statement: statementList)
            errors.addAll(statement.checkSemantics(symbolTable, nestingLevel));

        // Check for expression semantic errors
        if (exp != null)
            errors.addAll(exp.checkSemantics(symbolTable, nestingLevel));

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
        // Check for declarations type errors
        for (Node declaration: declarationList)
            if (declaration.typeCheck() instanceof ErrorType)
                return declaration.typeCheck();

        // Check for statements type errors
        for (Node statement: statementList)
            if (statement.typeCheck() instanceof ErrorType)
                return statement.typeCheck();

        // Check for expression type
        return exp != null ? exp.typeCheck() : null;
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
        StringBuilder str = new StringBuilder(string + "Prog\n");

        for (Node declaration: declarationList)
            str.append(declaration.toPrint("  "));

        for (Node statement: statementList)
            str.append(statement.toPrint("  "));

        if (exp != null)
            str.append(exp.toPrint("  "));

        return str.toString();
    }
}
