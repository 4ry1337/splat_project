package splat.elements.statements;

import java.util.Map;

import splat.elements.Type;
import splat.elements.Value;
import splat.elements.declarations.Function;
import splat.lexer.Token;
import splat.semanticanalyzer.SemanticAnalysisException;

public class PrintLineStatement extends Statement {
  public PrintLineStatement(Token printLineToken) {
    super(printLineToken);
  }

  @Override
  public String toString() {
    return "print_line;";
  }

  @Override
  public void analyze(Map<String, Function> functionMap, Map<String, Type> variableAndParameterMap)
      throws SemanticAnalysisException {
  }

  @Override
  public void execute(Map<String, Function> functionMap, Map<String, Value> variableAndParameterMap) {
    System.out.println();
  }
}
