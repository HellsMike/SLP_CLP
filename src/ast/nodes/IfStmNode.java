package ast.nodes;

import ast.types.BoolType;
import ast.types.ErrorType;
import ast.types.Type;
import utils.SemanticError;
import utils.SymbolTable;

import java.util.ArrayList;

public class IfStmNode implements Node {
    private final Node conditionExp;
    private final IfBodyStmNode thenBranch;
    private final IfBodyStmNode elseBranch;

    public IfStmNode (Node conditionExp, IfBodyStmNode thenBranch, IfBodyStmNode elseBranch) {
        this.conditionExp = conditionExp;
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
        // Create a new scope
        int ifScopeLevel = symbolTable.newScope();
        // Check for condition semantic errors
        ArrayList<SemanticError> errors = new ArrayList<>(conditionExp.checkSemantics(symbolTable, nestingLevel));
        // Check for then statements semantic errors
        errors.addAll(thenBranch.checkSemantics(symbolTable, ifScopeLevel));
        // check fort else statements semantic errors
        errors.addAll(elseBranch.checkSemantics(symbolTable, ifScopeLevel));
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
            if (thenBranch.typeCheck() instanceof ErrorType)
                return thenBranch.typeCheck();

            // Check for else branch type errors
            if (elseBranch.typeCheck() instanceof ErrorType)
                return elseBranch.typeCheck();
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
    public String toPrint(String string) {
        return string + "If " + conditionExp.toPrint("") + " ? " + thenBranch.toPrint("Then: ") +
                thenBranch.toPrint("Else: ") + "\n";
    }
}
