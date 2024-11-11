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
import splat.executor.ReturnFromCall;
import splat.executor.ExecutionException;

public class WhileStatement extends Statement {
  private Expression condition;
  private List<Statement> body;

  public WhileStatement(Expression condition, List<Statement> body, Token token) {
    super(token);
    this.condition = condition;
    this.body = body;
  }

  public Expression getCondition() {
    return condition;
  }

  public List<Statement> getBody() {
    return body;
  }

  @Override
  public String toString() {
    String result = "while " + condition.toString() + " do ";
    for (Statement statement : body) {
      result += statement.toString() + " ";
    }
    result += "end while;";
    return result;
  }

  @Override
  public void analyze(Map<String, Function> functionMap, Map<String, Type> variableAndParameterMap)
      throws SemanticAnalysisException {
    Type conditionType = condition.analyzeAndGetType(functionMap,
        variableAndParameterMap);

    if (conditionType != Type.BOOLEAN) {
      throw new SemanticAnalysisException("Condition of while loop must be Boolean.", this);
    }

    for (Statement statement : body) {
      statement.analyze(functionMap, variableAndParameterMap);
    }
  }

  @Override
  public void execute(Map<String, Function> functionMap, Map<String, Value> variableAndParameterMap)
      throws ReturnFromCall, ExecutionException {
    while (((BooleanValue) condition.evaluate(functionMap, variableAndParameterMap)).getValue()) {
      for (Statement statement : body) {
        statement.execute(functionMap, variableAndParameterMap);
      }
    }
  }
}
