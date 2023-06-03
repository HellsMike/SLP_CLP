package main;

import ast.SLPVisitor;
import ast.VMVisitor;
import ast.nodes.Node;
import ast.types.ErrorType;
import evaluator.ExecuteVM;
import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;
import parser.SVMLexer;
import parser.SVMParser;
import parser.SimpLanPlusLexer;
import parser.SimpLanPlusParser;
import utils.SLPErrorHandler;
import utils.SemanticError;
import utils.SymbolTable;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.Scanner;

public class Main {
   public static void main(String[] args) throws Exception {
      while (true) {
         // Getting filename at running time
         Scanner scanner = new Scanner(System.in);
         System.out.println("Insert filename: ");
         String filename = scanner.nextLine();

         CharStream charStream = null;

         try {
            charStream = CharStreams.fromFileName("tests/" + filename);
         } catch (Exception e) {
            System.out.println("Error during the opening of the file.");
            System.exit(1);
         }

         // Initialize parser
         SimpLanPlusLexer lexer = new SimpLanPlusLexer(charStream);
         SimpLanPlusParser parser = new SimpLanPlusParser(new CommonTokenStream(lexer));

         // Adding custom error handler
         SLPErrorHandler handler = new SLPErrorHandler();

         lexer.removeErrorListeners();
         lexer.addErrorListener(handler);

         parser.removeErrorListeners();
         parser.addErrorListener(handler);

         // Initialize visitor
         SLPVisitor visitor = new SLPVisitor();

         // Generate abstract tree
         Node ast = visitor.visit(parser.prog());

         // Check for lexical and syntax errors
         if (handler.errorNumber() > 0) {
            System.out.println(handler);
            handler.toLog(filename);
         } else {
            SymbolTable symbolTable = new SymbolTable();
            ArrayList<SemanticError> errors = ast.checkSemantics(symbolTable, 0);

            // Check for semantic errors
            if (errors.size() > 0) {
               System.out.println("You had " + errors.size() + " errors:");

               for (SemanticError error : errors)
                  System.out.println("\t" + error);
            } else {
               System.out.println("Visualizing AST...");
               System.out.println(ast.toPrint(""));

               // Type checking
               Node type = ast.typeCheck();

               if (type instanceof ErrorType)
                  System.out.println("Type checking is WRONG!");
               else {
                  System.out.print("Type checking OK! ");

                  if (type != null)
                     System.out.println("Type of the program is: " + type.toPrint("") + ".");
                  else
                     System.out.println("Program has no type.");

                  // Code generation
                  String code = ast.codeGeneration();

                  String codeDirPath = System.getProperty("user.dir") + "/tests/asm";

                  // Create code directory if not exists
                  File codeDir = new File(codeDirPath);

                  if (!codeDir.exists())
                     if(!codeDir.mkdir()) {
                        System.out.println("Error during the creation of the code folder.");
                        System.exit(1);
                     }

                  BufferedWriter out = new BufferedWriter(new FileWriter(codeDirPath + "/" + filename + ".asm"));
                  out.write(code);
                  out.close();
                  CharStream codeStream = CharStreams.fromString(code);
                  System.out.println("Code generated! Assembling and running generated code...");
                  SVMLexer lexerSVM = new SVMLexer(codeStream);
                  SVMParser parserSVM = new SVMParser(new CommonTokenStream(lexerSVM));

                  VMVisitor visitorSVM = new VMVisitor();
                  visitorSVM.visit(parserSVM.assembly());

                  System.out.println("Starting Virtual Machine...");
                  ExecuteVM vm = new ExecuteVM(visitorSVM.code);
                  vm.cpu();
               }
            }
         }
      }
   }
}
