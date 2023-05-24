package ast.nodes;

import ast.types.Type;
import ast.types.VoidType;

import java.util.ArrayList;

public class FunStmNode extends FunCallNode {
    public FunStmNode(String id, ArrayList<ParamNode> paramList) {
        super(id, paramList);
    }

    @Override
    public Type typeCheck() {
        super.typeCheck();

        // Return void type because is a statement and not an expression (CLP project requirement)
        return new VoidType();
    }
}
