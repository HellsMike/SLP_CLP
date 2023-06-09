package ast.nodes;

import ast.types.ErrorType;
import ast.types.FunType;
import ast.types.Type;
import utils.STEntry;
import utils.SemanticError;
import utils.SymbolTable;

import java.util.ArrayList;

/**
 * Node for function invocation as expression (evaluating its return value).
 */
public class FunCallNode implements Node {
    private final String id;
    private final ArrayList<Node> argumentList;
    private STEntry entry;
    private int nestingUsage;

    public FunCallNode (String id, ArrayList<Node> argumentList) {
        this.id = id;
        this.argumentList = argumentList;
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

        // Check for function id in the symbol table
        entry = symbolTable.lookup(id);

        if (entry != null) {
            if (entry.getType() instanceof FunType) {
                // Check arguments semantic
                for (Node argument : argumentList)
                    errors.addAll(argument.checkSemantics(symbolTable, nestingLevel));

                // Check if function call has the correct number of parameters
                if (((FunType) entry.getType()).getInputType().size() != argumentList.size())
                    errors.add(new SemanticError("Function " + id + " called with wrong number of parameters."));
            } else
                errors.add(new SemanticError("Wrong usage of variable identifier."));
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
                ArrayList<Type> argumentTypes = ((FunType) entry.getType()).getInputType();
                for (int i = 0; i < argumentTypes.size(); i++) {
                    Type argType = argumentList.get(i).typeCheck();

                    if (!argumentTypes.get(i).isEqual(argType))
                        return new ErrorType("Type error: mismatch between expected and used parameter type in " +
                                "position " + (i + 1) + " in the invocation of " + id + ".");

                    // Check for type errors in parameters
                    if (argType instanceof ErrorType)
                        return argType;
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
        StringBuilder argumentsCode = new StringBuilder();

        for (Node argument : argumentList)
            argumentsCode.append(argument.codeGeneration()).append("pushr A0 \n");

        return "pushr FP \n" +
                "move SP FP \n" +
                "addi FP 1 \n" +
                "move AL T1 \n" +
                "store T1 0(T1) \n".repeat(Math.max(0, nestingUsage - entry.getNesting())) +
                "pushr T1 \n" +
                argumentsCode +
                "move FP AL \n" +
                "subi AL 1 \n" +
                "jsub " + entry.getLabel() + "\n";
    }

    @Override
    public String toPrint(int tab) {
        StringBuilder paramString = new StringBuilder();

        for (Node argument : argumentList)
            paramString.append(argument.toPrint(tab + 1));

        return "  ".repeat(tab) + "Call " + id + " (\n" + paramString + "  ".repeat(tab) + ")\n";
    }
}
