package utils;

public class SemanticError {
    private String message;

    public SemanticError(String message){
        this.message = message;
    }

    public String toString() {
        return this.message;
    }
}
