package splat.parser.elements.statements;

import java.util.List;
import java.util.Map;

import splat.lexer.Token;
import splat.parser.elements.Type;
import splat.parser.elements.declarations.FunctionDeclaration;
import splat.parser.elements.expressions.Expression;
import splat.semanticanalyzer.SemanticAnalysisException;

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
  public void analyze(Map<String, FunctionDeclaration> functionMap, Map<String, Type> variableAndParameterMap)
      throws SemanticAnalysisException {
    Type conditionType = condition.analyzeAndGetType(functionMap, variableAndParameterMap);
    if (conditionType != Type.BOOLEAN) {
      throw new SemanticAnalysisException("If statement condition must be of type Boolean.", this);
    }
    for (Statement statement : thenBranch) {
      statement.analyze(functionMap, variableAndParameterMap);
    }
    if (elseBranch != null) {
      for (Statement statement : elseBranch) {
        statement.analyze(functionMap, variableAndParameterMap);
      }
    }
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
}
