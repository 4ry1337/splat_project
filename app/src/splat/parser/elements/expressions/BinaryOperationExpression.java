package splat.parser.elements.expressions;

import java.util.Map;

import splat.lexer.Token;
import splat.parser.elements.Type;
import splat.parser.elements.declarations.FunctionDeclaration;
import splat.semanticanalyzer.SemanticAnalysisException;

public class BinaryOperationExpression extends Expression {
  private Expression left;
  private Expression right;
  private String operator;

  public BinaryOperationExpression(Token token, Expression left, String operator, Expression right) {
    super(token);
    this.right = right;
    this.left = left;
    this.operator = operator;
  }

  public Expression getLeft() {
    return left;
  }

  public Expression getRight() {
    return right;
  }

  public String getOperator() {
    return operator;
  }

  @Override
  public Type analyzeAndGetType(Map<String, FunctionDeclaration> functionMap,
      Map<String, Type> variableAndParameterMap) throws SemanticAnalysisException {
    Type leftType = left.analyzeAndGetType(functionMap, variableAndParameterMap);
    Type rightType = right.analyzeAndGetType(functionMap,
        variableAndParameterMap);

    switch (operator) {
      case "+":
      case "-":
      case "*":
      case "/":
      case "%":
        if (leftType == Type.INTEGER && rightType == Type.INTEGER) {
          return Type.INTEGER;
        }
        break;
      case "and":
      case "or":
        if (leftType == Type.BOOLEAN && rightType == Type.BOOLEAN) {
          return Type.BOOLEAN;
        }
        break;
      case ">":
      case "<":
      case ">=":
      case "<=":
        if (leftType == Type.INTEGER && rightType == Type.INTEGER) {
          return Type.BOOLEAN;
        }
        break;
      case "==":
        if (leftType == rightType) {
          return Type.BOOLEAN;
        }
        break;
      default:
        throw new SemanticAnalysisException("Unexpected binary operator", this);
    }
    throw new SemanticAnalysisException("Invalid type.", this);
  }

  @Override
  public String toString() {
    return "(" + left.toString() + " " + operator + " " + right.toString() + ")";
  }
}
