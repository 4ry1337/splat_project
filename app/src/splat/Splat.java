package splat;

import java.io.File;
import java.util.List;

import splat.lexer.*;
import splat.parser.*;
import splat.parser.elements.*;
import splat.semanticanalyzer.SemanticAnalyzer;

public class Splat {
  private File progFile;

  public Splat(File progFile) {
    this.progFile = progFile;
  }

  public void processFileAndExecute() throws SplatException {
    // Step 1. Tokenize
    Lexer lexer = new Lexer(progFile);
    List<Token> tokens = lexer.tokenize();

    // for (Token token : tokens) {
    // System.out.println(token);
    // }

    // Step 2. Parse
    Parser parser = new Parser(tokens);
    ProgramAST progAST = parser.parse();

    // Step 3. Semantic Analysis
    SemanticAnalyzer analyzer = new SemanticAnalyzer(progAST);
    analyzer.analyze();

    // Step 4. Executor
    // Executor executor = new Executor(progAST);
    // executor.runProgram();

    // THE END!
  }
}
