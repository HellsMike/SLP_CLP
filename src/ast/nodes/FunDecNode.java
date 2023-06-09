package ast.nodes;

import ast.types.ErrorType;
import ast.types.FunType;
import ast.types.Type;
import utils.CodeGenSupport;
import utils.SemanticError;
import utils.SymbolTable;

import java.util.ArrayList;

/**
 * Node for function declaration.
 */
public class FunDecNode  implements Node {
    private final String id;
    private final Type type;
    private final ArrayList<ParamNode> paramList;
    private final BodyNode body;
    private FunType funType;
    private String label;

    public FunDecNode (String id, Type type, ArrayList<ParamNode> paramList, BodyNode body) {
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
            // Create the type class for the function
            ArrayList<Type> inputType = new ArrayList<>();

            for (ParamNode param : paramList)
                inputType.add(param.typeCheck());

            funType = new FunType(inputType, type);
            label = CodeGenSupport.newFunLabel();
            // Add new fun id to symbol table
            symbolTable.add(id, funType, label);
            // Create a new scope
            int funScopeLevel = symbolTable.newScope();

            // Check parameters semantic
            for (ParamNode param : paramList)
                errors.addAll(param.checkSemantics(symbolTable, funScopeLevel));

            // Increment the offset to leave space to RA
            symbolTable.increaseOffset();

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
            Type paramType = param.typeCheck();
            if (paramType instanceof ErrorType)
                return paramType;
        }

        Type bodyType = body.typeCheck();
        Type outType = funType.getOutputType();

        // If body type match with function return type return that type, otherwise return an error
        return bodyType.isEqual(outType) ? bodyType : new ErrorType("Function " + id +
                " expected return type " + outType.getClass() + " instead of " + bodyType.getClass() + ".");
    }

    /**
     * Bytecode generation for interpreter.
     *
     * @return Code.
     */
    @Override
    public String codeGeneration() {
        CodeGenSupport.addFunctionCode(
                        label + ": \n" +
                        "pushr RA \n" +
                        body.codeGeneration() +
                        "popr RA \n" +
                        "addi SP " + paramList.size() + " \n" +
                        "pop \n" +
                        "store FP 0(FP) \n" +
                        "move FP AL \n" +
                        "subi AL 1 \n" +
                        "pop \n" +
                        "rsub RA \n"
        );

        return "push " + label + " \n";
    }

    @Override
    public String toPrint(int tab) {
        StringBuilder paramString = new StringBuilder();

        for (ParamNode param : paramList)
            paramString.append(param.toPrint(0)).append(" ");

        return "  ".repeat(tab) + "Fun " + id + ": " + paramString + "\n" + body.toPrint(tab + 1);
    }
}
