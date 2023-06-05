package ast.types;

public class ErrorType extends Type {
    private final String message;

    public ErrorType(String message) {
        this.message = message;
        System.out.println(message);
    }

    @Override
    public String toPrint(int tab) {
        return "  ".repeat(tab) + "Error: " + message + "\n";
    }
}
