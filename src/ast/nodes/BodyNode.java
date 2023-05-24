package ast.nodes;

import ast.types.ErrorType;
import ast.types.Type;
import ast.types.VoidType;
import utils.SemanticError;
import utils.SymbolTable;

import java.util.ArrayList;

public class BodyNode implements Node {
    private final ArrayList<Node> declarationList;
    private final ArrayList<Node> statementList;
    private final Node exp;

    public BodyNode (ArrayList<Node> declarationList, ArrayList<Node> statementList, Node exp) {
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
        ArrayList<SemanticError> errors = new ArrayList<>();

        // Check declarations semantic
        for (Node declaration: declarationList)
            errors.addAll(declaration.checkSemantics(symbolTable, nestingLevel));

        // Check statements semantic
        for (Node statement: statementList)
            errors.addAll(statement.checkSemantics(symbolTable, nestingLevel));

        // Check expression semantic
        if (exp != null)
            errors.addAll(exp.checkSemantics(symbolTable, nestingLevel));

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
        for (Node declaration: declarationList) {
            if (declaration.typeCheck() instanceof ErrorType)
                return declaration.typeCheck();
        }

        // Check for statements type errors
        for (Node statement: statementList) {
            if (statement.typeCheck() instanceof ErrorType)
                return statement.typeCheck();
        }

        // If expression exists check for expression type errors, otherwise return Void
        return exp != null ? exp.typeCheck() : new VoidType();
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
        StringBuilder str = new StringBuilder(string);

        if (!declarationList.isEmpty()) {
            str.append("Dec: ");

            for (Node declaration: declarationList)
                str.append(declaration).append("\t");
        }

        if (!statementList.isEmpty()) {
            str.append("Stm: ");

            for (Node statement: statementList)
                str.append(statement).append("\t");
        }

        if (exp != null)
            str.append("Exp: ").append(exp);

        return str.toString();
    }
}
