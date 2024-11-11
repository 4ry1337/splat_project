package splat.elements.statements;

import java.util.Map;

import splat.lexer.Token;
import splat.semanticanalyzer.SemanticAnalysisException;
import splat.elements.Type;
import splat.elements.Value;
import splat.elements.declarations.Function;
import splat.elements.expressions.Expression;
import splat.executor.ExecutionException;

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
  public String toString() {
    return "print " + expression.toString() + ";";
  }

  @Override
  public void analyze(Map<String, Function> functionMap, Map<String, Type> variableAndParameterMap)
      throws SemanticAnalysisException {
    expression.analyzeAndGetType(functionMap, variableAndParameterMap);
  }

  @Override
  public void execute(Map<String, Function> functionMap, Map<String, Value> variableAndParameterMap)
      throws ExecutionException {
    Value expressionValue = expression.evaluate(functionMap, variableAndParameterMap);
    System.out.print(expressionValue.toString());
  }
}
