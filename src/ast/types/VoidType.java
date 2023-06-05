package ast.types;

public class VoidType extends Type {
    @Override
    public String toPrint(int tab) {
        return "  ".repeat(tab) + "Void ";
    }
}
