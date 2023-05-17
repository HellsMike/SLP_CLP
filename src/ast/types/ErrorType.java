package ast.types;

public class ErrorType extends Type {
    private String message;

    public ErrorType(String message) {
        this.message = message;
    }

    @Override
    public String toString(String string) {
        return string + "Error: " + message + "\n";
    }
}
