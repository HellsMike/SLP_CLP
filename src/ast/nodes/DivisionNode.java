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
        return left.codeGeneration() +
                "pushr A0 \n" +
                right.codeGeneration() +
                "popr T1 \n" +
                "div T1 A0 \n" +
                "popr A0 \n";
    }
}
