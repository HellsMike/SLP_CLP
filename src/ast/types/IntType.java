package ast.types;

public class IntType extends Type {
    @Override
    public String toPrint(int tab) {
        return "  ".repeat(tab) + "Int ";
    }
}
