package splat.elements.statements;

import java.util.Map;

import splat.lexer.Token;
import splat.semanticanalyzer.SemanticAnalysisException;
import splat.elements.Type;
import splat.elements.Value;
import splat.elements.declarations.Function;
import splat.elements.expressions.Expression;
import splat.executor.ExecutionException;
import splat.executor.ReturnFromCall;

public class ExpressionStatement extends Statement {
  private Expression expression;

  public ExpressionStatement(Token token, Expression expression) {
    super(token);
    this.expression = expression;
  }

  public Expression getExpression() {
    return expression;
  }

  @Override
  public void analyze(Map<String, Function> functionMap, Map<String, Type> variableAndParameterMap)
      throws SemanticAnalysisException {
    expression.analyzeAndGetType(functionMap, variableAndParameterMap);
  }

  @Override
  public String toString() {
    return expression.toString() + ";";
  }

  @Override
  public void execute(Map<String, Function> functionMap, Map<String, Value> variableAndParameterMap)
      throws ReturnFromCall, ExecutionException {
    expression.evaluate(functionMap, variableAndParameterMap);
  }
}
