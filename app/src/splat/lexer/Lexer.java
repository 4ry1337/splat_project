package splat.lexer;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import splat.Utils;

public class Lexer {
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
            tokens.add(new Token(source.substring(start_id, curr_id), null, line, column));
            column++;
            break;
          case '-':
            tokens.add(new Token(source.substring(start_id, curr_id), null, line, column));
            column++;
            break;
          case '*':
            tokens.add(new Token(source.substring(start_id, curr_id), null, line, column));
            column++;
            break;
          case '/':
            tokens.add(new Token(source.substring(start_id, curr_id), null, line, column));
            column++;
            break;
          case '%':
            tokens.add(new Token(source.substring(start_id, curr_id), null, line, column));
            column++;
            break;
          case '(':
            tokens.add(new Token(source.substring(start_id, curr_id), null, line, column));
            column++;
            break;
          case ')':
            tokens.add(new Token(source.substring(start_id, curr_id), null, line, column));
            column++;
            break;
          case ',':
            tokens.add(new Token(source.substring(start_id, curr_id), null, line, column));
            column++;
            break;
          case '.':
            tokens.add(new Token(source.substring(start_id, curr_id), null, line, column));
            column++;
            break;
          case ';':
            tokens.add(new Token(source.substring(start_id, curr_id), null, line, column));
            column++;
            break;
          case ':':
            if (curr_id < source.length() && source.charAt(curr_id) == '=') {
              tokens.add(new Token(source.substring(start_id, ++curr_id), null, line, column));
              column += 2;
            } else {
              tokens.add(new Token(source.substring(start_id, curr_id), null, line, column));
              column++;
            }
            break;
          case '=':
            if (curr_id < source.length() && source.charAt(curr_id) == '=') {
              tokens.add(new Token(source.substring(start_id, ++curr_id), null, line, column));
              column += 2;
            } else {
              throw new LexException("Invalid symbol", line, column);
            }
            break;
          case '>':
            if (curr_id < source.length() && source.charAt(curr_id) == '=') {
              tokens.add(new Token(source.substring(start_id, ++curr_id), null, line, column));
              column += 2;
            } else {
              tokens.add(new Token(source.substring(start_id, curr_id), null, line, column));
              column++;
            }
            break;
          case '<':
            if (curr_id < source.length() && source.charAt(curr_id) == '=') {
              tokens.add(new Token(source.substring(start_id, ++curr_id), null, line, column));
              column += 2;
            } else {
              tokens.add(new Token(source.substring(start_id, curr_id), null, line, column));
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
            tokens.add(new Token(source.substring(start_id, curr_id),
                source.substring(start_id + 1, curr_id - 1), line, column));
            column += curr_id - start_id;
            break;
          default:
            if (isDigit(ch)) {
              while (curr_id < source.length() && isDigit(source.charAt(curr_id))) {
                curr_id++;
              }
              tokens.add(new Token(source.substring(start_id, curr_id),
                  source.substring(start_id, curr_id), line, column));
              column += curr_id - start_id;
            } else if (isAlpha(ch)) {
              while (curr_id < source.length() && isAlphaNumeric(source.charAt(curr_id))) {
                curr_id++;
              }
              String lexeme = source.substring(start_id, curr_id);
              if (Utils.isKeyword(lexeme)) {
                tokens.add(new Token(lexeme, lexeme, line, column));
              } else {
                tokens.add(new Token(lexeme, null, line, column));
              }
              column += curr_id - start_id;
            } else {
              throw new LexException("Invalid character: '" + ch + "'", line, column);
            }
        }
      }
    } catch (IOException e) {
      throw new LexException(e.getMessage(), 0, 0);
    }

    // for (Token token : tokens) {
    // System.out.println(token);
    // }

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
