package splat.parser.elements.expressions;

import java.util.Map;

import splat.lexer.Token;
import splat.parser.elements.Type;
import splat.parser.elements.declarations.FunctionDeclaration;
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
  public Type analyzeAndGetType(Map<String, FunctionDeclaration> functionMap, Map<String, Type> variableAndParameterMap)
      throws SemanticAnalysisException {
    Type expressionType = expression.analyzeAndGetType(functionMap, variableAndParameterMap);

    switch (operator) {
      case "not":
        if (expressionType != Type.BOOLEAN) {
          throw new SemanticAnalysisException("Logical negation 'not' requires a Boolean operand.", this);
        }
        return Type.BOOLEAN;
      case "-":
        if (expressionType != Type.INTEGER) {
          throw new SemanticAnalysisException("Arithmetic negation '-' requires an Integer operand.", this);
        }
        return Type.INTEGER;
      default:
        throw new SemanticAnalysisException("Unsupported unary operator: " + operator, this);
    }
  }

  @Override
  public String toString() {
    return "(" + operator + " " + expression.toString() + ")";
  }
}
