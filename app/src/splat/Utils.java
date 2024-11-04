package splat;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class Utils {
  private static final Set<String> keywords = new HashSet<>(
      Arrays.asList("program", "begin", "end", "and", "or", "not", "String", "Integer", "Boolean", "true", "false",
          "void", "is", "while", "do", "if", "then", "else", "print", "print_line", "return"));
  private static final Set<String> booleans = new HashSet<>(Arrays.asList("true", "false"));
  private static final Set<String> types = new HashSet<>(Arrays.asList("String", "Integer", "Boolean"));
  private static final Set<String> return_types = new HashSet<>(Arrays.asList("String", "Integer", "Boolean", "void"));
  private static final Set<String> binary_operators = new HashSet<>(
      Arrays.asList("and", "or", "+", "-", ">", "<", "==", ">=", "<=", "+", "-", "*", "/", "%"));
  private static final Set<String> unary_operators = new HashSet<>(Arrays.asList("not", "-"));

  public static boolean isKeyword(String keyword) {
    return keywords.contains(keyword);
  }

  public static boolean isType(String type) {
    return types.contains(type);
  }

  public static boolean isBoolean(String lexeme) {
    return booleans.contains(lexeme);
  }

  public static boolean isReturnType(String return_type) {
    return return_types.contains(return_type);
  }

  public static boolean isBinaryOperators(String operator) {
    return binary_operators.contains(operator);
  }

  public static boolean isUnaryOperators(String operator) {
    return unary_operators.contains(operator);
  }

  public static boolean isLabel(String label) {
    return label.matches("[a-zA-Z_][a-zA-Z0-9_]*") && !isKeyword(label);
  }

  public static boolean isLiteral(Object literal) {
    return literal != null;
  }
}
