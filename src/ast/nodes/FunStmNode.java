package ast.nodes;

import ast.types.Type;
import ast.types.VoidType;

import java.util.ArrayList;

/**
 * Node for function invocation as statement (not evaluation its return value).
 */
public class FunStmNode extends FunCallNode {
    public FunStmNode(String id, ArrayList<Node> argumentList) {
        super(id, argumentList);
    }

    /**
     * Determines the type of the token.
     *
     * @return Type class of the corresponding node type.
     */
    @Override
    public Type typeCheck() {
        super.typeCheck();

        // Return null because is a statement and not an expression (CLP project requirement)
        return null;
    }
}
