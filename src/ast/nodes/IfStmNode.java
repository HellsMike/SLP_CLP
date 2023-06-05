package ast.nodes;

import ast.types.BoolType;
import ast.types.ErrorType;
import ast.types.Type;
import utils.CodeGenSupport;
import utils.STEntry;
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
        // Get the lists of entries seen as initialized in the branches
        ArrayList<String> stInitialized = symbolTable.getInitializedEntries();
        ArrayList<String> estInitialized = elseSymbolTable.getInitializedEntries();

        // Check if then branch and else branch initialized the same variables
        if (stInitialized.size() != estInitialized.size() || !stInitialized.containsAll(estInitialized)) {
            // Make a list of all unique id initialized in ony one branch
            ArrayList<String> difference1 = new ArrayList<>(stInitialized);
            ArrayList<String> difference2 = new ArrayList<>(estInitialized);
            difference1.removeAll(estInitialized);
            difference2.removeAll(stInitialized);

            ArrayList<STEntry> difference = new ArrayList<>();

            for (String id : difference1)
                difference.add(symbolTable.lookup(id));

            for (String id : difference2)
                difference.add(symbolTable.lookup(id));

            // Mark entries with conditional warning
            for (STEntry entry : difference)
                entry.markConditionWarning();
        }

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
            Type thenType = thenBranch.typeCheck();
            // Check for then branch type errors
            if (thenType instanceof ErrorType)
                return thenType.typeCheck();

            // Check for else branch type errors
            Type elseType = elseBranch.typeCheck();
            if (elseType instanceof ErrorType)
                return elseType.typeCheck();
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
    public String toPrint(int tab) {
        return "  ".repeat(tab) + "If " +  "\n" + conditionExp.toPrint(tab + 1) +
                thenBranch.toPrint(tab + 1) +
                elseBranch.toPrint(tab + 1);
    }
}
