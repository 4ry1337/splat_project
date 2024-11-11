package splat.elements.declarations;

import splat.elements.Type;
import splat.lexer.Token;

public class Variable extends Declaration {
  private String label;
  private Type type;

  public Variable(Token labelToken, Type type) {
    super(labelToken);
    this.label = labelToken.getLexeme();
    this.type = type;
  }

  public Type getType() {
    return type;
  }

  public String getLabel() {
    return label;
  }

  @Override
  public String toString() {
    return label + " : " + type + ";";
  }
}
