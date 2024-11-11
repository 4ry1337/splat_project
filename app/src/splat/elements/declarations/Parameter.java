package splat.elements.declarations;

import splat.elements.Type;
import splat.lexer.Token;

public class Parameter {
  private String label;
  private Type type;

  public Parameter(Token labelToken, Type type) {
    this.label = labelToken.getLexeme();
    this.type = type;
  }

  public String getLabel() {
    return label;
  }

  public Type getType() {
    return type;
  }

  @Override
  public String toString() {
    return label + " : " + type;
  }
}
