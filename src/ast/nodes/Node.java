package ast.nodes;

import ast.types.Type;
import utils.SemanticError;
import utils.SymbolTable;

import java.util.ArrayList;

/**
 * Generic node interface.
 */
public interface Node {
    /**
     * Function invoked to check for semantic errors.
     *
     * @param symbolTable Symbol table to check in.
     * @param nestingLevel Level of scope the node is in.
     * @return List of semantic error.
     */
    ArrayList<SemanticError> checkSemantics(SymbolTable symbolTable, int nestingLevel);

    /**
     * Determines the type of the token.
     *
     * @return Type class of the corresponding node type.
     */
    Type typeCheck();

    /**
     * Bytecode generation for interpreter.
     *
     * @return Code.
     */
    String codeGeneration();

    String toPrint(int tab);
}