package ast.nodes;

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
        return null;
    }
}
