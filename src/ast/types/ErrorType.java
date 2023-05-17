package ast.types;

public class ErrorType extends Type {
    @Override
    public String toString(String string) {
        return string + "Error\n";
    }
}
