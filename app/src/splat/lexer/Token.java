package splat.lexer;

public class Token {
  private final String lexeme;
  private final Object value;
  private final int line;
  private final int column;
  private final TokenType type;

  public Token(String lexeme, TokenType type, Object value, int line, int column) {
    this.lexeme = lexeme;
    this.line = line;
    this.column = column;
    this.type = type;
    this.value = value;
  }

  public String getLexeme() {
    return lexeme;
  }

  public int getLine() {
    return line;
  }

  public int getColumn() {
    return column;
  }

  public TokenType getType() {
    return type;
  }

  public Object getValue() {
    return value;
  }

  @Override
  public String toString() {
    return "Token = { lexeme=" + lexeme + ", line=" + line + ", column=" + column + ", type=" + type + ", value="
        + value + " }";
  }
}
