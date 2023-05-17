package ast.nodes;

public class MinusNode extends ArithmeticOpNode {
    public MinusNode(Node left, Node right) {
        super(left, right);
        operation = "Subtraction";
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
