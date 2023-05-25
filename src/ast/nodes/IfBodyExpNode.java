package ast.nodes;

import ast.types.ErrorType;
import ast.types.Type;
import utils.SemanticError;
import utils.SymbolTable;

import java.util.ArrayList;

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

    @Override
    public String toPrint(String string) {
        StringBuilder str = new StringBuilder(super.toPrint(string));

        return str.toString().contains("do nothing") ? string + exp.toPrint("") + "\t" : str +
                exp.toPrint("") + "\t";
    }
}