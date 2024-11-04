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
    Type rightType = left.analyzeAndGetType(functionMap, variableAndParameterMap);
    switch (operator) {
      case "+":
      case "-":
      case "*":
      case "/":
      case "%":
        if (leftType != Type.INTEGER && rightType != Type.INTEGER) {
          throw new SemanticAnalysisException("Invalid type.", this);
        }
        return Type.INTEGER;
      case "and":
      case "or":
        if (leftType != Type.INTEGER && rightType != Type.INTEGER) {
          throw new SemanticAnalysisException("Invalid type.", this);
        }
        return Type.INTEGER;
      case ">":
      case "<":
      case ">=":
      case "<=":
        if (leftType != Type.INTEGER || rightType != Type.INTEGER) {
          throw new SemanticAnalysisException("Invalid type.", this);
        }
        return Type.BOOLEAN;
      case "==":
        if (leftType != rightType) {
          throw new SemanticAnalysisException("Equality operator requires operands of the same type.", this);
        }
        return Type.BOOLEAN;
      default:
        throw new SemanticAnalysisException("Unexpected binary operator", this);
    }
  }

  @Override
  public String toString() {
    return "(" + left.toString() + " " + operator + " " + right.toString() + ")";
  }
}
