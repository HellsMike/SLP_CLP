package ast.nodes;

/**
 * Node for arithmetic operator "*".
 */
public class MultiplicationNode extends ArithmeticOpNode {
    public MultiplicationNode(Node left, Node right) {
        super(left, right);
        operation = "Multiplication";
    }

    /**
     * Bytecode generation for interpreter.
     *
     * @return Code.
     */
    @Override
    public String codeGeneration() {
        return left.codeGeneration() +
                "pushr A0 \n" +
                right.codeGeneration() +
                "popr T1 \n" +
                "mul A0 T1 \n" +
                "popr A0 \n";
    }
}
