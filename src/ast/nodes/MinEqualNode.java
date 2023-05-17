package ast.nodes;

public class MinEqualNode extends RelationalOpNode {
    public MinEqualNode(Node left, Node right) {
        super(left, right);
        operation = "Minor-equal";
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
