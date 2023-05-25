package ast.types;

public class IntType extends Type {
    @Override
    public String toPrint(String string) {
        return string + "Int ";
    }
}
