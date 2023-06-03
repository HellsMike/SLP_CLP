package ast.nodes;

import ast.types.ErrorType;
import ast.types.Type;
import ast.types.VoidType;
import utils.SemanticError;
import utils.SymbolTable;

import java.util.ArrayList;

/**
 * Node for function parameters.
 */
public class ParamNode implements Node {
    private final String id;
    private final Type type;

    public ParamNode(String id, Type type) {
        this.id = id;
        this.type = type;
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

        // Check if there is more than one parameter with the same identifier
        if (symbolTable.lookup(id, nestingLevel) != null) {
            // Add the identifier in the inner scope of the symbol table
            symbolTable.add(id, type);
            // Set the entry as initialized
            symbolTable.initializeEntry(symbolTable.lookup(id, nestingLevel));
        } else
            errors.add(new SemanticError("Id " + id + " passed more than one time as argument of a function."));

        return errors;
    }

    /**
     * Determines the type of the token.
     *
     * @return Type class of the corresponding node type.
     */
    @Override
    public Type typeCheck() {
        // Check if variable is declared as void type
        if (type instanceof VoidType)
            return new ErrorType("Type Error: illegal use of type void in the variable " + id + ".");
        else
            return type;
    }

    /**
     * Bytecode generation for interpreter.
     *
     * @return Code.
     */
    @Override
    public String codeGeneration() {
        return "";
    }

    @Override
    public String toPrint(String string) {
        return string + "Var " + id + "\n";
    }
}
