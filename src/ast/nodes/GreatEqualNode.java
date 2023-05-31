package ast.nodes;

import utils.CodeGenSupport;

/**
 * Node for relational operator ">=".
 */
public class GreatEqualNode extends RelationalOpNode {
    public GreatEqualNode(Node left, Node right) {
        super(left, right);
        operation = "Great-equal";
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
                "bleq A0 T1 " + labelTrue + "\n" +
                "storei A0 0 \n" +
                "b " + labelEnd + "\n" +
                labelTrue + ": \n" +
                "storei A0 1 \n" +
                labelEnd + ": \n";
    }
}
