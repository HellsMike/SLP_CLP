package ast.nodes;

import ast.types.ErrorType;
import ast.types.Type;
import utils.STEntry;
import utils.SemanticError;
import utils.SymbolTable;

import java.util.ArrayList;

public class VarInitNode implements Node {
    private final String id;
    private final Node exp;
    private STEntry entry;

    public VarInitNode (String id, Node exp) {
        this.id = id;
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
        // Get the symbol table entry
        STEntry entry = symbolTable.lookup(id);

        // Check if the entry exist in the symbol table
        if (entry == null)
            errors.add(new SemanticError("Id " + id + " is not declared."));
        else
            // Mark the entry as initialized
            this.entry = symbolTable.initializeEntry(entry);

        // Check for expression semantic errors
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
        // Check if the expression is compatible with the entry type
        if (entry.getType().isEqual(exp.typeCheck()))
            return null;
        else
            return new ErrorType("Type error: wrong type assigned to id " + id + ".");
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
        return string + "Variable " + id + ": " + exp.toPrint("");
    }
}
