package splat.elements.statements;

import java.util.Map;

import splat.lexer.Token;
import splat.semanticanalyzer.SemanticAnalysisException;
import splat.elements.Type;
import splat.elements.Value;
import splat.elements.declarations.Function;
import splat.elements.expressions.Expression;
import splat.executor.ReturnFromCall;
import splat.executor.ExecutionException;

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
  public String toString() {
    return "return" + (expression != null ? " " + expression.toString() : "") + ";";
  }

  @Override
  public void analyze(Map<String, Function> functionMap, Map<String, Type> variableAndParameterMap)
      throws SemanticAnalysisException {
    if (!variableAndParameterMap.containsKey("0")) {
      throw new SemanticAnalysisException("Return statements are not allowed in the main program body.", this);
    }

    Type returnType = variableAndParameterMap.get("0");

    if (expression == null) {
      if (returnType != null) {
        throw new SemanticAnalysisException("Return statement missing an expression in a non-void function.", this);
      }
    } else {
      Type exprType = expression.analyzeAndGetType(functionMap, variableAndParameterMap);
      if (returnType == null || returnType != exprType) {
        throw new SemanticAnalysisException(
            "Return expression type mismatch: expected " + returnType + ", but got " + exprType + ".", this);
      }
    }
  }

  @Override
  public void execute(Map<String, Function> functionMap, Map<String, Value> variableAndParameterMap)
      throws ReturnFromCall, ExecutionException {
    Value returnValue = null;
    if (expression != null) {
      returnValue = expression.evaluate(functionMap, variableAndParameterMap);
    }
    throw new ReturnFromCall(returnValue);
  }
}
