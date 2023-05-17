package ast.nodes;

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
        return null;
    }
}
