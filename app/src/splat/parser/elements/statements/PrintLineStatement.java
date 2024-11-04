package splat.parser.elements.statements;

import java.util.Map;

import splat.lexer.Token;
import splat.parser.elements.Type;
import splat.parser.elements.declarations.FunctionDeclaration;
import splat.semanticanalyzer.SemanticAnalysisException;

public class PrintLineStatement extends Statement {
  public PrintLineStatement(Token printLineToken) {
    super(printLineToken);
  }

  @Override
  public void analyze(Map<String, FunctionDeclaration> functionMap, Map<String, Type> variableAndParameterMap)
      throws SemanticAnalysisException {
  }

  @Override
  public String toString() {
    return "print_line;";
  }
}
