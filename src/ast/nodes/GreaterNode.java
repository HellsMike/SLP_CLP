package ast.nodes;

/**
 * Node for relational operator ">".
 */
public class GreaterNode extends RelationalOpNode {
    public GreaterNode(Node left, Node right) {
        super(left, right);
        operation = "Greater";
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
