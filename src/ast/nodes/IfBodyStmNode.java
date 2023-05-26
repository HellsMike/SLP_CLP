package ast.nodes;

import ast.types.ErrorType;
import ast.types.Type;
import utils.SemanticError;
import utils.SymbolTable;

import java.util.ArrayList;

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

        // Create a new scope
        int ifScopeLevel = symbolTable.newScope();

        // Check for statements semantic errors
        for (Node statement : statementList)
            errors.addAll(statement.checkSemantics(symbolTable, ifScopeLevel));

        // Exit current scope
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
        return null;
    }

    @Override
    public String toPrint(String string) {
        StringBuilder str = new StringBuilder(string);

        if (!statementList.isEmpty())
            for (Node statement : statementList)
                str.append(statement.toPrint("")).append("\t");
        else
            str.append("do nothing\t");

        return str.toString();
    }
}
