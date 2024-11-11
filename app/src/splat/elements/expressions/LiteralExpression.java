package splat.elements.expressions;

import java.util.Map;

import splat.elements.Type;
import splat.elements.Value;
import splat.elements.declarations.Function;
import splat.lexer.Token;

public class LiteralExpression extends Expression {
  private String valueString;
  private Value value;

  public LiteralExpression(Token value) {
    super(value);
    this.valueString = value.getLexeme();
    this.value = value.getValue();
  }

  public Value getValue() {
    return value;
  }

  @Override
  public String toString() {
    return valueString + " " + value.getClass();
  }

  @Override
  public Type analyzeAndGetType(Map<String, Function> functionMap,
      Map<String, Type> variableAndParameterMap) {
    return value.getType();
  }

  @Override
  public Value evaluate(Map<String, Function> functionMap, Map<String, Value> variableAndParameterMap) {
    return value;
  }
}
