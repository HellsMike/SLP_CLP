package ast.nodes;

/**
 * Node for arithmetic operator "+".
 */
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
        return left.codeGeneration() +
                "pushr A0 \n" +
                right.codeGeneration() +
                "popr T1 \n" +
                "add A0 T1 \n" +
                "popr A0 \n";
    }
}
