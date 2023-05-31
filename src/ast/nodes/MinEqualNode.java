package ast.nodes;

import utils.CodeGenSupport;

/**
 * Node for relational operator "<=".
 */
public class MinEqualNode extends RelationalOpNode {
    public MinEqualNode(Node left, Node right) {
        super(left, right);
        operation = "Minor-equal";
    }

    /**
     * Bytecode generation for interpreter.
     *
     * @return Code.
     */
    @Override
    public String codeGeneration() {
        String labelTrue = CodeGenSupport.newLabel();
        String labelEnd = CodeGenSupport.newLabel();

        return left.codeGeneration() +
                "pushr A0 \n" +
                right.codeGeneration() +
                "popr T1 \n" +
                "bleq T1 A0 " + labelTrue + "\n" +
                "storei A0 0 \n" +
                "b " + labelEnd + "\n" +
                labelTrue + ": \n" +
                "storei A0 1 \n" +
                labelEnd + ": \n";
    }
}
