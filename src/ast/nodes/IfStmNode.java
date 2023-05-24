package ast.nodes;

import ast.types.BoolType;
import ast.types.ErrorType;
import ast.types.Type;
import utils.SemanticError;
import utils.SymbolTable;

import java.util.ArrayList;

public class IfStmNode implements Node {
    private final Node conditionExp;
    private final ArrayList<Node> thenStmList;
    private final ArrayList<Node> elseStmList;

    public IfStmNode (Node conditionExp, ArrayList<Node> thenStmList, ArrayList<Node> elseStmList) {
        this.conditionExp = conditionExp;
        this.thenStmList = thenStmList;
        this.elseStmList = elseStmList;
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
            // Check for then branch type errors
            for (Node thenStm : thenStmList)
                if (thenStm.typeCheck() instanceof ErrorType)
                    return thenStm.typeCheck();

            // Check for else branch type errors
            for (Node elseStm: elseStmList)
                if (elseStm.typeCheck() instanceof ErrorType)
                    return elseStm.typeCheck();
        } else
            return new ErrorType("Type error: if condition must be a boolean.");

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
    public String toString(String string) {
        StringBuilder str = new StringBuilder();

        if (!thenStmList.isEmpty()) {
            str.append("Then: ");

            for (Node thenStm : thenStmList)
                str.append(thenStm).append("\t");
        }

        if (!elseStmList.isEmpty()) {
            str.append("Else: ");

            for (Node elseStm : elseStmList)
                str.append(elseStm).append("\t");
        }
        
        return string + "If " + conditionExp.toString() + " ? " + str + "\n";
    }
}
