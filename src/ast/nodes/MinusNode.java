package ast.nodes;

/**
 * Node for arithmetic operator "-".
 */
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
        return left.codeGeneration() +
                "pushr A0 \n" +
                right.codeGeneration() +
                "popr T1 \n" +
                "sub T1 A0 \n" +
                "popr A0 \n";
    }
}
