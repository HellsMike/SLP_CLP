package ast.nodes;

import utils.CodGenSupport;

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
        String labelTrue = CodGenSupport.newLabel();
        String labelEnd = CodGenSupport.newLabel();
        String labelEnd2 = CodGenSupport.newLabel();

        return left.codeGeneration() +
                "pushr A0 \n" +
                right.codeGeneration() +
                "popr T1 \n" +
                "bleq A0 T1 " + labelTrue + "\n" +
                "storei A0 0\n" +
                "b " + labelEnd + "\n" +
                labelTrue + ":\n" +
                "beq A0 T1 " + labelEnd2 + "\n" +
                "storei A0 1\n" +
                "b " + labelEnd + "\n" +
                labelEnd2 + ":\n" +
                "storei A0 0\n" +
                labelEnd + ":\n";
    }
    }
