package splat.elements.statements;

import java.util.List;
import java.util.Map;

import splat.lexer.Token;
import splat.semanticanalyzer.SemanticAnalysisException;
import splat.elements.Type;
import splat.elements.Value;
import splat.elements.declarations.Function;
import splat.elements.expressions.Expression;
import splat.elements.values.BooleanValue;
import splat.executor.ExecutionException;
import splat.executor.ReturnFromCall;

public class IfStatement extends Statement {
  private Expression condition;
  private List<Statement> thenBranch;
  private List<Statement> elseBranch;

  public IfStatement(Expression condition, List<Statement> thenBranch, List<Statement> elseBranch, Token token) {
    super(token);
    this.condition = condition;
    this.thenBranch = thenBranch;
    this.elseBranch = elseBranch;
  }

  public Expression getCondition() {
    return condition;
  }

  public List<Statement> getThenBranch() {
    return thenBranch;
  }

  public List<Statement> getElseBranch() {
    return elseBranch;
  }

  @Override
  public String toString() {
    String result = "if " + condition.toString() + " then ";
    for (Statement statement : thenBranch) {
      result += statement.toString() + " ";
    }
    if (elseBranch != null && !elseBranch.isEmpty()) {
      result += "else ";
      for (Statement statement : elseBranch) {
        result += statement.toString() + " ";
      }
    }
    result += "end if;";
    return result;
  }

  @Override
  public void analyze(Map<String, Function> functionMap, Map<String, Type> variableAndParameterMap)
      throws SemanticAnalysisException {
    Type conditionType = condition.analyzeAndGetType(functionMap,
        variableAndParameterMap);
    if (conditionType != Type.BOOLEAN) {
      throw new SemanticAnalysisException("If statement condition must be Boolean",
          getLine(), getColumn());
    }

    for (Statement stmt : thenBranch) {
      stmt.analyze(functionMap, variableAndParameterMap);
    }

    if (elseBranch != null) {
      for (Statement stmt : elseBranch) {
        stmt.analyze(functionMap, variableAndParameterMap);
      }
    }
  }

  @Override
  public void execute(Map<String, Function> functionMap, Map<String, Value> variableAndParameterMap)
      throws ReturnFromCall, ExecutionException {
    BooleanValue conditionValue = (BooleanValue) condition.evaluate(functionMap, variableAndParameterMap);

    if (conditionValue.getValue()) {
      for (Statement statements : thenBranch) {
        statements.execute(functionMap, variableAndParameterMap);
      }
    } else if (elseBranch != null) {
      for (Statement statements : elseBranch) {
        statements.execute(functionMap, variableAndParameterMap);
      }
    }
  }
}
