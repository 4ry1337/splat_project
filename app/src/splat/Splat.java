package splat;

import java.io.File;
import java.util.List;

import splat.elements.*;
import splat.lexer.*;
import splat.parser.*;
import splat.semanticanalyzer.*;
import splat.executor.*;

public class Splat {
  private File progFile;

  public Splat(File progFile) {
    this.progFile = progFile;
  }

  public void processFileAndExecute() throws SplatException {
    // Step 1. Tokenize
    Lexer lexer = new Lexer(progFile);
    List<Token> tokens = lexer.tokenize();

    // Step 2. Parse
    Parser parser = new Parser(tokens);
    ProgramAST progam = parser.parse();

    // Step 3. Semantic Analysis
    SemanticAnalyzer analyzer = new SemanticAnalyzer(progam);
    analyzer.analyze();

    // Step 4. Executor
    Executor executor = new Executor(progam);
    executor.runProgram();

    // THE END!
  }
}
