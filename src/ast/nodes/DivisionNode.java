package ast.nodes;

public class DivisionNode extends ArithmeticOpNode {
    public DivisionNode(Node left, Node right) {
        super(left, right);
        operation = "Division";
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
