import ast.*;
import parser.*;

import org.antlr.v4.runtime.*;

import java.io.FileInputStream;

public class Main {
   public static void main(String[] args) throws Exception {
      String filename = "lexer_test";
      FileInputStream is = null;

      try {
         is = new FileInputStream(filename);
      } catch (Exception e) {
         System.out.println("Error during the opening of the file.");
         System.exit(1);
      }

      // Initialize parser
      ANTLRInputStream input = new ANTLRInputStream(is);
      SimpLanPlusLexer lexer = new SimpLanPlusLexer(input);
      CommonTokenStream tokens = new CommonTokenStream(lexer);
      SimpLanPlusParser parser = new SimpLanPlusParser(tokens);

      // Adding custom error handler
      SLPErrorHandler handler = new SLPErrorHandler();

      lexer.removeErrorListeners();
      lexer.addErrorListener(handler);

      parser.removeErrorListeners();
      parser.addErrorListener(handler);

      // Run the parser
      parser.prog();

      // Check for lexical and syntax errors
      if (handler.errorNumber() > 0) {
         System.out.print(handler);
         handler.toLog(filename);
      }
   }
}
