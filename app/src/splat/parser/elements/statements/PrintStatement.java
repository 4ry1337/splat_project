package splat.parser.elements.statements;

import java.util.Map;

import splat.lexer.Token;
import splat.parser.elements.Type;
import splat.parser.elements.declarations.FunctionDeclaration;
import splat.parser.elements.expressions.Expression;
import splat.semanticanalyzer.SemanticAnalysisException;

public class PrintStatement extends Statement {
  private Expression expression;

  public PrintStatement(Token printToken, Expression expression) {
    super(printToken);
    this.expression = expression;
  }

  public Expression getExpression() {
    return expression;
  }

  public void setExpression(Expression expression) {
    this.expression = expression;
  }

  @Override
  public void analyze(Map<String, FunctionDeclaration> functionMap, Map<String, Type> variableAndParameterMap)
      throws SemanticAnalysisException {
    expression.analyzeAndGetType(functionMap, variableAndParameterMap);
  }

  @Override
  public String toString() {
    return "print " + expression.toString() + ";";
  }
}
