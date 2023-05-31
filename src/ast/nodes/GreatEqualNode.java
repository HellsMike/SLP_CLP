package ast.nodes;

import utils.CodGenSupport;

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
        String labelTrue = CodGenSupport.newLabel();
        String labelEnd = CodGenSupport.newLabel();

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
