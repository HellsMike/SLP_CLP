package ast.nodes;

import ast.types.ErrorType;
import ast.types.Type;
import utils.SemanticError;
import utils.SymbolTable;

import java.util.ArrayList;

/**
 * Node for if body with statements and expression.
 */
public class IfBodyExpNode extends IfBodyStmNode {
    private final Node exp;
    public IfBodyExpNode(ArrayList<Node> statementList, Node exp) {
        super(statementList);
        this.exp = exp;
    }

    /**
     * Function invoked to check for semantic errors.
     *
     * @param symbolTable  Symbol table to check in.
     * @param nestingLevel Level of scope the node is in.
     * @return List of semantic error.
     */
    @Override
    public ArrayList<SemanticError> checkSemantics (SymbolTable symbolTable, int nestingLevel) {
        // Check for statements semantic errors
        ArrayList<SemanticError> errors = new ArrayList<>(super.checkSemantics(symbolTable, nestingLevel));
        // Check for expression semantic errors
        errors.addAll(exp.checkSemantics(symbolTable, nestingLevel));

        return errors;
    }

    /**
     * Determines the type of the token.
     *
     * @return Type class of the corresponding node type.
     */
    @Override
    public Type typeCheck() {
        return super.typeCheck() instanceof ErrorType ? super.typeCheck() : exp.typeCheck();
    }

    /**
     * Bytecode generation for interpreter.
     *
     * @return Code.
     */
    @Override
    public String codeGeneration() {
        String statementsCode = super.codeGeneration();

        return statementsCode +
                exp.codeGeneration();
    }

    @Override
    public String toPrint(int tab) {
        StringBuilder str = new StringBuilder(super.toPrint(tab));

        return str.toString().contains("do nothing") ? exp.toPrint(tab) : str +
                exp.toPrint(tab);
    }
}
