package utils;

public class SemanticError {
    private final String message;

    public SemanticError(String message){
        this.message = message;
    }

    public String toString() {
        return this.message;
    }
}
