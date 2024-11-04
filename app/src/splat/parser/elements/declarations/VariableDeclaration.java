package splat.parser.elements.declarations;

import splat.lexer.Token;
import splat.parser.elements.Type;

public class VariableDeclaration extends Declaration {
  private String label;
  private Type type;

  public VariableDeclaration(Token labelToken, Type type) {
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
