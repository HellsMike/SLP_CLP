package ast.nodes;

public class PlusNode extends ArithmeticOpNode {
    public PlusNode(Node left, Node right) {
        super(left, right);
        operation = "Addition";
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
