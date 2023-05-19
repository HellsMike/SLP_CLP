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
    public String toString(String string) {
        StringBuilder funType = new StringBuilder();
        for(Type type : inputType)
            funType.append(type).append(" ");
        return string + "Function Type:" + funType.toString() + "-> " + outputType + "\n";
    }
}
