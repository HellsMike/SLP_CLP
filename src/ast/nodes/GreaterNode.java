package ast.nodes;

import utils.CodGenSupport;

/**
 * Node for relational operator ">".
 */
public class GreaterNode extends RelationalOpNode {
    public GreaterNode(Node left, Node right) {
        super(left, right);
        operation = "Greater";
    }

    /**
     * Bytecode generation for interpreter.
     *
     * @return Code.
     */
    @Override
    public String codeGeneration() {
        String labelFalse = CodGenSupport.newLabel();
        String labelEnd = CodGenSupport.newLabel();

        return left.codeGeneration() +
                "pushr A0 \n" +
                right.codeGeneration() +
                "popr T1 \n" +
                "bleq T1 A0 " + labelFalse + "\n" +
                "storei A0 1 \n" +
                "b " + labelEnd + "\n" +
                labelFalse + ": \n" +
                "storei A0 0 \n" +
                labelEnd + ": \n";
    }
}
