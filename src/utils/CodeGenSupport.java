package utils;

public class CodeGenSupport {
    private static int labelCount = 0;
    private static int functionLabelCount = 0;
    private static String functionsCode = "";

    public static String newLabel() {
        return "label" + labelCount++;
    }

    public static String newFunLabel() {
        return "function" + functionLabelCount++;
    }

    /**
     * Insert function code in a string -> used to put all the functions declaration
     * at the end of the program code.
     *
     * @param code Function code.
     */
    public static void addFunctionCode(String code) {
        functionsCode += code + " \n";
    }

    public static String getFunctionsCode() {
        return functionsCode;
    }
}
