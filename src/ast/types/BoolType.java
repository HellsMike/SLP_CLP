package ast.types;

public class BoolType extends Type {
    @Override
    public String toPrint(int tab) {
        return "  ".repeat(tab) + "Bool ";
    }
}
