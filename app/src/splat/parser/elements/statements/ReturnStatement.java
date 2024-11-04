package splat.parser.elements.statements;

import java.util.Map;

import splat.lexer.Token;
import splat.parser.elements.Type;
import splat.parser.elements.declarations.FunctionDeclaration;
import splat.parser.elements.expressions.Expression;
import splat.semanticanalyzer.SemanticAnalysisException;

public class ReturnStatement extends Statement {
  private Expression expression;

  public ReturnStatement(Token returnToken, Expression expression) {
    super(returnToken);
    this.expression = expression;
  }

  public Expression getExpression() {
    return expression;
  }

  @Override
  public void analyze(Map<String, FunctionDeclaration> functionMap, Map<String, Type> variableAndParameterMap)
      throws SemanticAnalysisException {
    Type expectedReturnType = variableAndParameterMap.get("0result");

    if (expression != null) {
      Type exprType = expression.analyzeAndGetType(functionMap, variableAndParameterMap);

      if (expectedReturnType == null || exprType != expectedReturnType) {
        throw new SemanticAnalysisException(
            "Return type mismatch: Expected " + expectedReturnType + " but got " + exprType + ".", this);
      }
    } else {
      if (expectedReturnType != null) {
        throw new SemanticAnalysisException(
            "Return statement missing an expression; expected return type is " + expectedReturnType + ".", this);
      }
    }
  }

  @Override
  public String toString() {
    return "return" + (expression != null ? " " + expression.toString() : "") + ";";
  }
}
