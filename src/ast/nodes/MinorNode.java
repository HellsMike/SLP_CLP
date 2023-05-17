package ast.nodes;

public class MinorNode extends RelationalOpNode {
    public MinorNode(Node left, Node right) {
        super(left, right);
        operation = "Minor";
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
