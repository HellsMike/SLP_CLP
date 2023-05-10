package ast;

import org.antlr.v4.runtime.*;

import java.io.*;
import java.util.ArrayList;

public class SLPErrorHandler extends BaseErrorListener {
    private ArrayList<String> errorList;

    public SLPErrorHandler() {
        this.errorList = new ArrayList<String>();
    }

    /**
     * Append an error message in errorList reporting the line and the character that generate the error.
     */
    @Override
    public void syntaxError(Recognizer<?, ?> recognizer, Object offendingSymbol, int line, int charPositionInLine,
                            String msg, RecognitionException e) {
        this.errorList.add("Character " + charPositionInLine + " in line " + line + " generate an error: " + msg);
    }

    /**
     * @return string with concatenated error messages.
     */
    @Override
    public String toString() {
        return String.join("\n", errorList);
    }

    /**
     * Insert the error messages in a log file.
     * @param filename
     */
    public void toLog(String filename) throws IOException {
        String logDirPath = System.getProperty("user.dir") + "/log";

        // Create log directory if not exists
        File logDir = new File(logDirPath);
        if (!logDir.exists()) {
            logDir.mkdir();
        }

        BufferedWriter writer = new BufferedWriter(new FileWriter(logDirPath + "/" + filename + ".log"));
        writer.write(this.toString());
        writer.close();
    }

    /**
     * @return Number of error evaluated.
     */
    public int errorNumber() {
        return this.errorList.size();
    }
}
