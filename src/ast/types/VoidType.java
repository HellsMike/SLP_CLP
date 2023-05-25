package ast.types;

public class VoidType extends Type {
    @Override
    public String toPrint(String string) {
        return string + "Void ";
    }
}
