package ast.nodes;

import utils.CodGenSupport;

/**
 * Node for relational operator "<".
 */
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
        String labelFalse = CodGenSupport.newLabel();
        String labelEnd = CodGenSupport.newLabel();

        return left.codeGeneration() +
                "pushr A0 \n" +
                right.codeGeneration() +
                "popr T1 \n" +
                // Check if right is <= of left
                "bleq A0 T1 " + labelTrue + "\n" +
                labelFalse + ":\n" +
                "storei A0 0\n" +
                "b " + labelEnd + "\n" +
                labelTrue + ":\n" +
                // Check if right is equal to left
                "beq A0 T1 " + labelFalse + "\n" +
                "storei A0 1\n" +
                "b " + labelEnd + "\n" +
                labelEnd + ":\n";
    }
    }
