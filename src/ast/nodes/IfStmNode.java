package ast.nodes;

import ast.types.BoolType;
import ast.types.ErrorType;
import ast.types.Type;
import utils.CodeGenSupport;
import utils.SemanticError;
import utils.SymbolTable;

import java.util.ArrayList;

/**
 * Node for if block with statements.
 */
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
        // Check for condition semantic errors
        ArrayList<SemanticError> errors = new ArrayList<>(conditionExp.checkSemantics(symbolTable, nestingLevel));
        // Create a deep copy of the symbol table and pass it to else branch, this is needed to check if both branches
        // have the same entries initialized
        SymbolTable elseSymbolTable = new SymbolTable(symbolTable);
        // Check for then branch semantic errors
        errors.addAll(thenBranch.checkSemantics(symbolTable, nestingLevel));
        // Check for else branch semantic errors
        errors.addAll(elseBranch.checkSemantics(elseSymbolTable, nestingLevel));
        ArrayList<String> stInitialized = symbolTable.getInitializedEntries();
        ArrayList<String> estInitialized = elseSymbolTable.getInitializedEntries();

        // Check if then branch and else branch initialized the same variables, otherwise the rest of the program is
        // unpredictable
        if (stInitialized.size() != estInitialized.size() || !stInitialized.containsAll(estInitialized))
            errors.add(new SemanticError("Then branch and else branch must have the same variables initialized."));

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
        String labelThen = CodeGenSupport.newLabel();
        String labelEnd = CodeGenSupport.newLabel();

        return conditionExp.codeGeneration() +
                // 1 -> true
                "storei T1 1 \n" +
                // Check if condition return true
                "beq A0 T1 " + labelThen + "\n" +
                elseBranch.codeGeneration() +
                "b " + labelEnd + "\n" +
                labelThen + ": \n" +
                thenBranch.codeGeneration() +
                labelEnd + ": \n" ;
    }

    @Override
    public String toPrint(String string) {
        return string + "If " + conditionExp.toPrint("") + " ? " + thenBranch.toPrint("Then: ") +
                thenBranch.toPrint("Else: ") + "\n";
    }
}
