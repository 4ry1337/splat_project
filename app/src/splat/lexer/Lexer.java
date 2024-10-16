package splat.lexer;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Lexer {
  private static final Map<String, TokenType> keywords = new HashMap<>();

  static {
    keywords.put("program", TokenType.PROGRAM);
    keywords.put("begin", TokenType.BEGIN);
    keywords.put("end", TokenType.END);
    keywords.put("and", TokenType.AND);
    keywords.put("or", TokenType.OR);
    keywords.put("not", TokenType.NOT);
    keywords.put("String", TokenType.STRING);
    keywords.put("Integer", TokenType.INTEGER);
    keywords.put("Boolean", TokenType.BOOLEAN);
    keywords.put("true", TokenType.TRUE);
    keywords.put("false", TokenType.FALSE);
    keywords.put("void", TokenType.VOID);
    keywords.put("is", TokenType.IS);
    keywords.put("while", TokenType.WHILE);
    keywords.put("do", TokenType.DO);
    keywords.put("if", TokenType.IF);
    keywords.put("then", TokenType.THEN);
    keywords.put("else", TokenType.ELSE);
    keywords.put("print", TokenType.PRINT);
    keywords.put("print_line", TokenType.PRINT_LINE);
    keywords.put("return", TokenType.RETURN);
  }

  private final File file;
  private final List<Token> tokens;

  public Lexer(File progFile) {
    file = progFile;
    tokens = new ArrayList<>();
  }

  public List<Token> tokenize() throws LexException {
    byte[] bytes = null;
    try {
      bytes = Files.readAllBytes(file.toPath());
      String source = new String(bytes, Charset.defaultCharset());

      StringBuilder tokenBuilder = new StringBuilder();
      int line = 1, column = 1, start_id = 0, curr_id = 0;

      while (curr_id < source.length()) {
        start_id = curr_id;
        char ch = source.charAt(curr_id++);

        switch (ch) {
          case ' ':
          case '\t':
          case '\r':
            column++;
            break;
          case '\n':
            column = 1;
            line++;
            break;
          case '+':
            tokens.add(new Token(source.substring(start_id, curr_id), TokenType.PLUS, null, line, column));
            column++;
            break;
          case '-':
            tokens.add(new Token(source.substring(start_id, curr_id), TokenType.MINUS, null, line, column));
            column++;
            break;
          case '*':
            tokens.add(new Token(source.substring(start_id, curr_id), TokenType.STAR, null, line, column));
            column++;
            break;
          case '/':
            tokens.add(new Token(source.substring(start_id, curr_id), TokenType.SLASH, null, line, column));
            column++;
            break;
          case '%':
            tokens.add(new Token(source.substring(start_id, curr_id), TokenType.MOD, null, line, column));
            column++;
            break;
          case '(':
            tokens.add(new Token(source.substring(start_id, curr_id), TokenType.LEFT_PAREN, null, line, column));
            column++;
            break;
          case ')':
            tokens.add(new Token(source.substring(start_id, curr_id), TokenType.RIGHT_PAREN, null, line, column));
            column++;
            break;
          case ',':
            tokens.add(new Token(source.substring(start_id, curr_id), TokenType.COMMA, null, line, column));
            column++;
            break;
          case '.':
            tokens.add(new Token(source.substring(start_id, curr_id), TokenType.DOT, null, line, column));
            column++;
            break;
          case ';':
            tokens.add(new Token(source.substring(start_id, curr_id), TokenType.SEMICOLON, null, line, column));
            column++;
            break;
          case ':':
            if (curr_id < source.length() && source.charAt(curr_id) == '=') {
              tokens.add(new Token(source.substring(start_id, ++curr_id), TokenType.ASSIGNMENT, null, line, column));
              column += 2;
            } else {
              tokens.add(new Token(source.substring(start_id, curr_id), TokenType.COLON, null, line, column));
              column++;
            }
            break;
          case '=':
            if (curr_id < source.length() && source.charAt(curr_id) == '=') {
              tokens.add(new Token(source.substring(start_id, ++curr_id), TokenType.EQUAL, null, line, column));
              column += 2;
            } else {
              throw new LexException("Invalid symbol", line, column);
            }
            break;
          case '>':
            if (curr_id < source.length() && source.charAt(curr_id) == '=') {
              tokens.add(new Token(source.substring(start_id, ++curr_id), TokenType.GREATER_EQUAL, null, line, column));
              column += 2;
            } else {
              tokens.add(new Token(source.substring(start_id, curr_id), TokenType.GREATER, null, line, column));
              column++;
            }
            break;
          case '<':
            if (curr_id < source.length() && source.charAt(curr_id) == '=') {
              tokens.add(new Token(source.substring(start_id, ++curr_id), TokenType.LESS_EQUAL, null, line, column));
              column += 2;
            } else {
              tokens.add(new Token(source.substring(start_id, curr_id), TokenType.LESS, null, line, column));
            }
            break;
          case '"':
            while (curr_id < source.length() && source.charAt(curr_id) != '"') {
              if (source.charAt(curr_id) == '\n' || source.charAt(curr_id) == '\r') {
                throw new LexException("Unterminated string 1", line, column);
              }
              curr_id++;
            }
            if (curr_id >= source.length() || source.charAt(curr_id) != '"') {
              throw new LexException("Unterminated string 2", line, column);
            }
            curr_id++;
            tokens.add(new Token(source.substring(start_id, curr_id), TokenType.STRING,
                source.substring(start_id + 1, curr_id - 1), line, column));
            column += curr_id - start_id;
            break;
          default:
            if (isDigit(ch)) {
              while (curr_id < source.length() && isDigit(source.charAt(curr_id))) {
                curr_id++;
              }
              tokens.add(new Token(source.substring(start_id, curr_id), TokenType.INTEGER,
                  source.substring(start_id, curr_id), line, column));
              column += curr_id - start_id;
            } else if (isAlpha(ch)) {
              while (curr_id < source.length() && isAlphaNumeric(source.charAt(curr_id))) {
                curr_id++;
              }
              String lexeme = source.substring(start_id, curr_id);
              TokenType type = keywords.getOrDefault(lexeme, TokenType.LABEL);
              tokens.add(new Token(lexeme, type, lexeme, line, column));
              column += curr_id - start_id;
            } else {
              throw new LexException("Invalid character: '" + ch + "'", line, column);
            }
        }
      }
    } catch (IOException e) {
      throw new LexException(e.getMessage(), 0, 0);
    }

    return tokens;
  }

  private boolean isAlphaNumeric(char c) {
    return isAlpha(c) || isDigit(c);
  }

  private boolean isAlpha(char ch) {
    return (ch >= 'a' && ch <= 'z') || (ch >= 'A' && ch <= 'Z') || ch == '_';
  }

  private boolean isDigit(char c) {
    return c >= '0' && c <= '9';
  }
}
