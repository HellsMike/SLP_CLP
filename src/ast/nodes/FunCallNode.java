package ast.nodes;

import ast.types.ErrorType;
import ast.types.FunType;
import ast.types.Type;
import ast.types.VoidType;
import utils.STEntry;
import utils.SemanticError;
import utils.SymbolTable;

import java.util.ArrayList;

public class FunCallNode implements Node {
    private final String id;
    private final ArrayList<ParamNode> paramList;
    private STEntry entry;
    private int nestingLevel;

    public FunCallNode (String id, ArrayList<ParamNode> paramList) {
        this.id = id;
        this.paramList = paramList;
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

        // Check for function id in the symbol table
        entry = symbolTable.lookup(id);

        if (entry != null) {
            for (ParamNode param : paramList) {
                // Check if parameters are declared
                if (symbolTable.lookup(param.getId()) == null)
                    errors.add(new SemanticError("Parameter " + param.getId() + " not defined."));

                // Check parameters semantic
                errors.addAll(param.checkSemantics(symbolTable, nestingLevel));
            }

            // Check if function call has the correct number of parameters
            if (((FunType) entry.getType()).getInputType().size() != paramList.size())
                errors.add(new SemanticError("Function " + id + " called with wrong number of parameters."));
        } else
            errors.add(new SemanticError("Function " + id + " not declared."));

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
        else {
            if (entry.getType() instanceof FunType) {
                // Check if given parameters type match with expected ones
                ArrayList<Type> paramTypes = ((FunType) entry.getType()).getInputType();
                for (int i = 0; i < paramTypes.size(); i++) {
                    if (paramTypes.get(i).getClass() != paramList.get(i).typeCheck().getClass())
                        return new ErrorType("Type error: mismatch between expected and used parameter type in " +
                                "position " + (++i) + " in the invocation of " + id + ".");

                    // Check for type errors in parameters
                    if (paramList.get(i).typeCheck() instanceof ErrorType)
                        return paramList.get(i).typeCheck();
                }
            } else
                return new ErrorType("Type error: wrong usage of variable identifier");
        }

        return ((FunType) entry.getType()).getOutputType();
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

        return string + "Call: " + id + "( Param: " + paramString + ") at nest level " + nestingLevel + "\n";
    }
}
