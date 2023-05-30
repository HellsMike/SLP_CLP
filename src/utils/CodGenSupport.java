package utils;

public class CodGenSupport {
    private static int labelCount = 0;
    private static int functionLabelCount = 0;

    public static String newLabel() {
        return "label" + labelCount++;
    }

    public static String newFunLabel() {
        return "function" + functionLabelCount++;
    }
}
