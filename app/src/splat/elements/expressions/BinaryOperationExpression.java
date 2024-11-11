package splat.elements.expressions;

import java.util.Map;

import splat.lexer.Token;
import splat.elements.Type;
import splat.elements.Value;
import splat.elements.declarations.Function;
import splat.elements.values.*;
import splat.semanticanalyzer.SemanticAnalysisException;
import splat.executor.ExecutionException;

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
  public String toString() {
    return "(" + left.toString() + " " + operator + " " + right.toString() + ")";
  }

  @Override
  public Type analyzeAndGetType(Map<String, Function> functionMap,
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
  public Value evaluate(Map<String, Function> functionMap, Map<String, Value> variableAndParameterMap)
      throws ExecutionException {
    Value leftValue = left.evaluate(functionMap, variableAndParameterMap);
    Value rightValue = right.evaluate(functionMap, variableAndParameterMap);

    switch (operator) {
      case "+":
        return ((IntegerValue) leftValue).add((IntegerValue) rightValue);
      case "-":
        return ((IntegerValue) leftValue).subtract((IntegerValue) rightValue);
      case "*":
        return ((IntegerValue) leftValue).multiply((IntegerValue) rightValue);
      case "/":
        if (((IntegerValue) rightValue).getValue() == 0) {
          throw new ExecutionException("Division by zero", this);
        }
        return ((IntegerValue) leftValue).divide((IntegerValue) rightValue);
      case "%":
        if (((IntegerValue) rightValue).getValue() == 0) {
          throw new ExecutionException("Division by zero", this);
        }
        return ((IntegerValue) leftValue).modulus((IntegerValue) rightValue);
      case ">":
        return ((IntegerValue) leftValue).greaterThan((IntegerValue) rightValue);
      case "<":
        return ((IntegerValue) leftValue).lessThan((IntegerValue) rightValue);
      case ">=":
        return ((IntegerValue) leftValue).greaterThan((IntegerValue) rightValue)
            .or(((IntegerValue) leftValue).equalsTo((IntegerValue) rightValue));
      case "<=":
        return ((IntegerValue) leftValue).lessThan((IntegerValue) rightValue)
            .or(((IntegerValue) leftValue).equalsTo((IntegerValue) rightValue));
      case "and":
        return ((BooleanValue) leftValue).and((BooleanValue) rightValue);
      case "or":
        return ((BooleanValue) leftValue).or((BooleanValue) rightValue);
      case "==":
        if (leftValue instanceof IntegerValue) {
          return ((IntegerValue) leftValue).equalsTo((IntegerValue) rightValue);
        }
        if (leftValue instanceof BooleanValue) {
          return ((BooleanValue) leftValue).equalsTo((BooleanValue) rightValue);
        }
        if (leftValue instanceof StringValue) {
          return ((StringValue) leftValue).equalsTo((StringValue) rightValue);
        }
      default:
        throw new ExecutionException("Invalid operator: " + operator, getLine(), getColumn());
    }
  }
}
