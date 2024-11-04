package splat.parser.elements.declarations;

import splat.lexer.Token;
import splat.parser.elements.ASTElement;

public abstract class Declaration extends ASTElement {
  public Declaration(Token token) {
    super(token);
  }

  public abstract String getLabel();
}
