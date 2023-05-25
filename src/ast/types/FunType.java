package ast.types;

import java.util.ArrayList;

public class FunType extends Type {
    private final ArrayList<Type> inputType;
    private final Type outputType;

    public FunType(ArrayList<Type> inputType, Type outputType) {
        this.inputType = inputType;
        this.outputType = outputType;
    }

    public Type getOutputType() {
        return outputType;
    }

    public ArrayList<Type> getInputType() {
        return inputType;
    }

    @Override
    public String toPrint(String string) {
        StringBuilder types = new StringBuilder();

        for (Type type : inputType)
            types.append(type).append(" ");

        return string + "Function Type:" + types + "-> " + outputType.toPrint("") + "\n";
    }
}
