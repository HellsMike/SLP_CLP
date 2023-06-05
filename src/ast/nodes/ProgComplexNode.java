package ast.nodes;

import ast.types.ErrorType;
import ast.types.Type;
import ast.types.VoidType;
import utils.CodeGenSupport;
import utils.SemanticError;
import utils.SymbolTable;

import java.util.ArrayList;

/**
 * Node for program with declarations, statements and expression.
 */
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
        for (Node declaration : declarationList) {
            Type decType = declaration.typeCheck();
            if (decType instanceof ErrorType)
                return decType;
        }

        // Check for statements type errors
        for (Node statement : statementList) {
            Type stmType = statement.typeCheck();
            if (stmType instanceof ErrorType)
                return stmType;
        }

        // Check for expression type
        return exp != null ? exp.typeCheck() : new VoidType();
    }

    /**
     * Bytecode generation for interpreter.
     *
     * @return Code.
     */
    @Override
    public String codeGeneration() {
        StringBuilder decStmCode = new StringBuilder();

        for (Node declaration : declarationList)
            decStmCode.append(declaration.codeGeneration());

        for (Node statement : statementList)
            decStmCode.append(statement.codeGeneration());

        if (exp != null)
            decStmCode.append(exp.codeGeneration());

        return "pushr FP \n" +
                "pushr AL \n" +
                decStmCode +
                "halt \n" +
                CodeGenSupport.getFunctionsCode();
    }

    @Override
    public String toPrint(int tab) {
        StringBuilder str = new StringBuilder("Prog\n");

        for (Node declaration: declarationList)
            str.append(declaration.toPrint(tab + 1));

        for (Node statement: statementList)
            str.append(statement.toPrint(tab + 1));

        if (exp != null)
            str.append(exp.toPrint(tab + 1));

        return str.toString();
    }
}
