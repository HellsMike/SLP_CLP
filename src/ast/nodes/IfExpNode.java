package ast.nodes;

import ast.types.BoolType;
import ast.types.ErrorType;
import ast.types.Type;
import utils.SemanticError;
import utils.SymbolTable;

import java.util.ArrayList;

public class IfExpNode implements Node {
    private final Node conditionExp;
    private final ArrayList<Node> thenStmList;
    private final ArrayList<Node> elseStmList;
    private final Node thenExp;
    private final Node elseExp;

    public IfExpNode (Node conditionExp, ArrayList<Node> thenStmList, ArrayList<Node> elseStmList, Node thenExp,
                      Node elseExp) {
        this.conditionExp = conditionExp;
        this.thenStmList = thenStmList;
        this.elseStmList = elseStmList;
        this.thenExp = thenExp;
        this.elseExp = elseExp;
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
        // Create a new scope
        int ifScopeLevel = symbolTable.newScope();
        // Check for condition semantic errors
        ArrayList<SemanticError> errors = new ArrayList<>(conditionExp.checkSemantics(symbolTable, nestingLevel));
        // Check for then expression semantic errors
        errors.addAll(thenExp.checkSemantics(symbolTable, ifScopeLevel));
        // Check for else expression semantic errors
        errors.addAll(elseExp.checkSemantics(symbolTable, ifScopeLevel));

        // Check for then statements semantic errors
        for (Node thenStm : thenStmList)
            errors.addAll(thenStm.checkSemantics(symbolTable, ifScopeLevel));

        // check fort else statements semantic errors
        for (Node elseStm : elseStmList)
            errors.addAll(elseStm.checkSemantics(symbolTable, ifScopeLevel));

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
        // Check if condition is a boolean type
        if (conditionExp.typeCheck() instanceof BoolType) {
            // Check for then statements type errors
            for (Node thenStm : thenStmList)
                if (thenStm.typeCheck() instanceof ErrorType)
                    return thenStm.typeCheck();

            // Check for then expression type errors
            if (thenExp.typeCheck() instanceof ErrorType)
                return thenExp.typeCheck();

            // Check for else statements type errors
            for (Node elseStm: elseStmList)
                if (elseStm.typeCheck() instanceof ErrorType)
                    return elseStm.typeCheck();

            // Check for else expression type errors
            if (elseExp.typeCheck() instanceof ErrorType)
                return elseExp.typeCheck();
        } else
            return new ErrorType("Type error: if condition must be a boolean.");

        return thenExp.typeCheck().getClass() == elseExp.typeCheck().getClass() ? thenExp.typeCheck() :
                new ErrorType("Type error: then branch and else branch mismatch return type.");
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
        StringBuilder str = new StringBuilder("Then: ");

        if (!thenStmList.isEmpty())
            for (Node thenStm : thenStmList)
                str.append(thenStm).append("\t");

        str.append(thenExp.toString()).append("\tElse: ");

        if (!elseStmList.isEmpty())
            for (Node elseStm : elseStmList)
                str.append(elseStm).append("\t");

        str.append(elseExp.toString());

        return string + "If " + conditionExp.toString() + " ? " + str + "\n";
    }
}
