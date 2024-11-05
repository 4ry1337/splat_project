package splat.parser.elements.statements;

import java.util.List;
import java.util.Map;

import splat.lexer.Token;
import splat.parser.elements.Type;
import splat.parser.elements.declarations.FunctionDeclaration;
import splat.parser.elements.expressions.Expression;
import splat.semanticanalyzer.SemanticAnalysisException;

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
  public void analyze(Map<String, FunctionDeclaration> functionMap, Map<String, Type> variableAndParameterMap)
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
  public String toString() {
    String result = "while " + condition.toString() + " do ";
    for (Statement statement : body) {
      result += statement.toString() + " ";
    }
    result += "end while;";
    return result;
  }
}
