package ast.nodes;

import ast.types.ErrorType;
import ast.types.Type;
import utils.SemanticError;
import utils.SymbolTable;

import java.util.ArrayList;

/**
 * Node for if body with statements.
 */
public class IfBodyStmNode implements Node {
    private final ArrayList<Node> statementList;

    public IfBodyStmNode (ArrayList<Node> statementList) {
        this.statementList = statementList;
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

        // Check for statements semantic errors
        for (Node statement : statementList)
            errors.addAll(statement.checkSemantics(symbolTable, nestingLevel));

        return errors;
    }

    /**
     * Determines the type of the token.
     *
     * @return Type class of the corresponding node type.
     */
    @Override
    public Type typeCheck() {
        // Check for statements type errors
        for (Node statement : statementList)
            if (statement.typeCheck() instanceof ErrorType)
                return statement.typeCheck();

        return null;
    }

    /**
     * Bytecode generation for interpreter.
     *
     * @return Code.
     */
    @Override
    public String codeGeneration() {
        StringBuilder statementsCode = new StringBuilder();

        for (Node statement : statementList)
            statementsCode.append(statement.codeGeneration());

        return statementsCode.toString();
    }

    @Override
    public String toPrint(int tab) {
        StringBuilder str = new StringBuilder();

        if (!statementList.isEmpty())
            for (Node statement : statementList)
                str.append(statement.toPrint(tab));
        else
            str.append("  ".repeat(tab)).append("do nothing\n");

        return str.toString();
    }
}
