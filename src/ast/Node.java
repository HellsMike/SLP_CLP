package ast;

import java.util.ArrayList;

import ast.types.Type;
import utils.*;

public interface Node {
    /**
     * Function invoked to check for semantic errors.
     * @param symbolTable Symbol table to check in.
     * @param nestingLevel Level of scope the node is in.
     * @return List of semantic error.
     */
    ArrayList<SemanticError> checkSemantics(SymbolTable symbolTable, int nestingLevel);

    /**
     * Type checking process.
     * @return Type class of the corresponding node type.
     */
    Type typeCheck();

    /**
     * Bytecode generation for interpreter.
     * @return Code.
     */
    String codeGeneration();

    String toPrint(String string);
}