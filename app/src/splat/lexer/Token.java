package splat.lexer;

public class Token {
  private final String lexeme;
  private final Object value;
  private final int line;
  private final int column;

  public Token(String lexeme, Object value, int line, int column) {
    this.lexeme = lexeme;
    this.line = line;
    this.column = column;
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

  public Object getValue() {
    return value;
  }

  @Override
  public String toString() {
    return "Token = { lexeme=" + lexeme + ", line=" + line + ", column=" + column + ", value="
        + value + " }";
  }
}
