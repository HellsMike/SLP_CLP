package ast.nodes;

import ast.types.ErrorType;
import ast.types.FunType;
import ast.types.Type;
import utils.SemanticError;
import utils.SymbolTable;

import java.util.ArrayList;

public class FunDecNode  implements Node {
    private final String id;
    private final FunType type;
    private final ArrayList<ParamNode> paramList;
    private final BodyNode body;

    public FunDecNode (String id, FunType type, ArrayList<ParamNode> paramList, BodyNode body) {
        this.id = id;
        this.type = type;
        this.paramList = paramList;
        this.body = body;
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

        // Check if id is already declared in the inner scope
        if (symbolTable.lookup(id, nestingLevel) != null)
            errors.add(new SemanticError("Id " + id + " is already declared."));
        else {
            // Create a new scope
            int funScopeLevel = symbolTable.newScope();
            // Add new fun id to symbol table
            symbolTable.add(id, type, nestingLevel);

            // Check parameters semantic
            for (ParamNode param : paramList)
                errors.addAll(param.checkSemantics(symbolTable, funScopeLevel));

            // Check body semantic
            errors.addAll(body.checkSemantics(symbolTable, funScopeLevel));
            // Exit the inner scope
            symbolTable.exitScope();
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
        // Check for type errors in parameters
        for (ParamNode param : paramList) {
            if (param.typeCheck() instanceof ErrorType)
                return param.typeCheck();
        }

        Type bodyType = body.typeCheck();

        // If body type match with function return type return that type, otherwise return an error
        return bodyType != type.getOutputType() ? new ErrorType("Function " + id + " expected return type " +
                type.getOutputType() + " instead of " + bodyType + ".") : bodyType;
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
        StringBuilder paramString = new StringBuilder();

        for (ParamNode param : paramList)
            paramString.append(param.toString()).append(" ");

        return string + "Fun: " + id + "( "+ paramString + ")" + "\n" + body.toString("  ");
    }
}
