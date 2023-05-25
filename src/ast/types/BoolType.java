package ast.types;

public class BoolType extends Type {
    @Override
    public String toPrint(String string) {
        return string + "Bool ";
    }
}
