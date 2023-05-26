package ast.nodes;

import ast.types.ErrorType;
import ast.types.FunType;
import ast.types.Type;
import utils.STEntry;
import utils.SemanticError;
import utils.SymbolTable;

import java.util.ArrayList;

public class IdNode implements Node {
    private final String id;
    private STEntry entry;

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
        // Check for variable id in the symbol table
        entry = symbolTable.lookup(id);

        if (entry == null)
            errors.add(new SemanticError("Id " + id + " is not declared."));
        // Check if the variable is initialized
        else if (!entry.isInitialized())
            errors.add(new SemanticError("Id " + id + " used before initialization."));

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
        return null;
    }

    @Override
    public String toPrint(String string)  {
        return string + "Id: " + id + " is at nest level " + entry.getNesting() + "\n";
    }
}
