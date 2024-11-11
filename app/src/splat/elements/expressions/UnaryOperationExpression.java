package splat.elements.expressions;

import java.util.Map;

import splat.elements.Type;
import splat.elements.Value;
import splat.elements.declarations.Function;
import splat.elements.values.*;
import splat.executor.ExecutionException;
import splat.lexer.Token;
import splat.semanticanalyzer.SemanticAnalysisException;

public class UnaryOperationExpression extends Expression {
  private String operator;
  private Expression expression;

  public UnaryOperationExpression(Token token, String operator, Expression expression) {
    super(token);
    this.operator = operator;
    this.expression = expression;
  }

  public String getOperator() {
    return operator;
  }

  public Expression getExpression() {
    return expression;
  }

  @Override
  public String toString() {
    return "(" + operator + " " + expression.toString() + ")";
  }

  @Override
  public Type analyzeAndGetType(Map<String, Function> functionMap,
      Map<String, Type> variableAndParameterMap) throws SemanticAnalysisException {
    Type exprType = expression.analyzeAndGetType(functionMap, variableAndParameterMap);

    switch (operator) {
      case "not":
        if (exprType == Type.BOOLEAN) {
          return Type.BOOLEAN;
        }
        break;
      case "-":
        if (exprType == Type.INTEGER) {
          return Type.INTEGER;
        }
        break;
      default:
        throw new SemanticAnalysisException("Unknown unary operator: " + operator, this);
    }
    throw new SemanticAnalysisException("Invalid type.", this);
  }

  @Override
  public Value evaluate(Map<String, Function> functionMap, Map<String, Value> variableAndParameterMap)
      throws ExecutionException {
    Value exprValue = expression.evaluate(functionMap, variableAndParameterMap);

    if (operator.equals("not")) {
      return ((BooleanValue) exprValue).not();
    } else {
      return ((IntegerValue) exprValue).not();
    }
  }
}
