package ast.nodes;

import ast.types.ErrorType;
import ast.types.Type;
import ast.types.VoidType;
import utils.SemanticError;
import utils.SymbolTable;

import java.util.ArrayList;

public class VarDeclarationNode implements Node {
    private final String id;
    private final Type type;

    public VarDeclarationNode(String id, Type type) {
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
        if (symbolTable.lookup(id, nestingLevel) != null)
            errors.add(new SemanticError("Variable with id " + id + " already declared."));
        else
            symbolTable.add(id, type, nestingLevel);
        return errors;
    }

    /**
     * Determines the type of the token.
     *
     * @return Type class of the corresponding node type.
     */
    @Override
    public Type typeCheck() {
        if (type instanceof VoidType) {
            ErrorType error = new ErrorType("Type Error: illegal declaration of the variable " + id + ".");
            System.out.println(error);
            return error;
        } else
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
        return string + "Variable " + id + "\n";
    }
}
