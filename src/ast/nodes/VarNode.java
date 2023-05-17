package ast.nodes;

import ast.types.ErrorType;
import ast.types.FunType;
import ast.types.Type;
import utils.STEntry;
import utils.SemanticError;
import utils.SymbolTable;

import java.util.ArrayList;

public class VarNode implements Node {
    private final String id;
    private STEntry entry;
    private int nestingLevel;

    public VarNode(String id) {
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
        this.nestingLevel = nestingLevel;
        STEntry entry = symbolTable.lookup(id);
        if (entry == null)
            errors.add(new SemanticError("Id " + id + " is not declared."));
        else this.entry = entry;
        return errors;
    }

    /**
     * Determines the type of the token.
     *
     * @return Type class of the corresponding node type.
     */
    @Override
    public Type typeCheck() {
        if (entry.getType() instanceof FunType) {
            System.out.println("Wrong usage of function identifier.");
            return new ErrorType();
        } else
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
    public String toString(String string)  {
        return string + "Id: " + id + " is at nest level " + entry.getNesting() + "\n";
    }
}
