package splat.parser.elements.expressions;

import java.util.Map;

import splat.lexer.Token;
import splat.parser.elements.Type;
import splat.parser.elements.declarations.FunctionDeclaration;
import splat.semanticanalyzer.SemanticAnalysisException;

public class LiteralExpression extends Expression {
  private String valueString;
  private Object value;

  public LiteralExpression(Token value) {
    super(value);
    this.valueString = value.getLexeme();
    this.value = value.getValue();
  }

  public Object getValue() {
    return value;
  }

  @Override
  public Type analyzeAndGetType(Map<String, FunctionDeclaration> functionMap,
      Map<String, Type> variableAndParameterMap) throws SemanticAnalysisException {
    if (value instanceof Integer) {
      return Type.INTEGER;
    } else if (value instanceof Boolean) {
      return Type.BOOLEAN;
    } else if (value instanceof String) {
      return Type.STRING;
    } else {
      throw new SemanticAnalysisException("Unsupported literal type: " + value.getClass().getSimpleName(), this);
    }
  }

  @Override
  public String toString() {
    return valueString + " " + value.getClass();
  }
}
