package ast.nodes;

import ast.types.ErrorType;
import ast.types.Type;
import ast.types.VoidType;
import utils.SemanticError;
import utils.SymbolTable;

import java.util.ArrayList;

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
        // Add the identifier in the inner scope of the symbol table
        symbolTable.add(id, type);
        // Set the entry as initialized
        symbolTable.initializeEntry(symbolTable.lookup(id, nestingLevel));

        return new ArrayList<>();
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
        return null;
    }

    @Override
    public String toPrint(String string) {
        return string + "Var " + id + "\n";
    }
}
