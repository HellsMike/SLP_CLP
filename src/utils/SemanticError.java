package utils;

public class SemanticError {
    private String message;

    public SemanticError(String _message){
        this.message = _message;
    }

    public String toString() {
        return this.message;
    }
}
