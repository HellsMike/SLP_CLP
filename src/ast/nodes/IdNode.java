package ast.nodes;

import ast.types.ErrorType;
import ast.types.FunType;
import ast.types.Type;
import utils.STEntry;
import utils.SemanticError;
import utils.SymbolTable;

import java.util.ArrayList;

/**
 * Node for variable usage (as expression).
 */
public class IdNode implements Node {
    private final String id;
    private STEntry entry;
    private int nestingUsage;

    public IdNode(String id) {
        this.id = id;
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
        this.nestingUsage = nestingLevel;
        // Check for variable id in the symbol table
        entry = symbolTable.lookup(id);

        if (entry == null)
            errors.add(new SemanticError("Id " + id + " is not declared."));
        // Check if the variable is initialized
        else if (!entry.isInitialized())
            errors.add(new SemanticError("Id " + id + " used before initialization."));
        // Check if variable has been initialized only in one if branch.
        else if (entry.hasConditionWarning())
            errors.add(new SemanticError("Id " + id + " may not be initialized."));

        return errors;
    }

    /**
     * Determines the type of the token.
     *
     * @return Type class of the corresponding node type.
     */
    @Override
    public Type typeCheck() {
        if (entry == null)
            return new ErrorType("Id " + id + " not declared.");
        // Check if id correspond to a function
        else if (entry.getType() instanceof FunType)
            return new ErrorType("Type error: wrong usage of function identifier.");
        else
            return entry.getType();
    }

    /**
     * Bytecode generation for interpreter.
     *
     * @return Code.
     */
    @Override
    public String codeGeneration() {
        return "move AL T1 \n" +
                "store T1 0(T1) \n".repeat(Math.max(0, nestingUsage - entry.getNesting())) +
                "subi T1 " + entry.getOffset() + " \n" +
                "store A0 0(T1) \n";
    }

    @Override
    public String toPrint(int tab)  {
        return "  ".repeat(tab) + "Id " + id + "\n";
    }
}
