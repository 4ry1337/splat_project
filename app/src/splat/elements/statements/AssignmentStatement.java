package splat.elements.statements;

import java.util.Map;

import splat.lexer.Token;
import splat.semanticanalyzer.SemanticAnalysisException;
import splat.elements.Type;
import splat.elements.Value;
import splat.elements.declarations.Function;
import splat.elements.expressions.Expression;
import splat.executor.ExecutionException;
import splat.executor.ReturnFromCall;

public class AssignmentStatement extends Statement {
  private String label;
  private Expression expression;

  public AssignmentStatement(Token labelToken, Expression expression) {
    super(labelToken);
    this.label = labelToken.getLexeme();
    this.expression = expression;
  }

  public String getVariableName() {
    return label;
  }

  public Expression getExpression() {
    return expression;
  }

  @Override
  public String toString() {
    return label + " := " + expression.toString() + ";";
  }

  @Override
  public void analyze(Map<String, Function> functionMap, Map<String, Type> variableAndParameterMap)
      throws SemanticAnalysisException {
    if (!variableAndParameterMap.containsKey(label)) {
      throw new SemanticAnalysisException("Variable '" + label + "' not defined.",
          this);
    }
    Type variableType = variableAndParameterMap.get(label);

    Type expressionType = expression.analyzeAndGetType(functionMap,
        variableAndParameterMap);

    if (variableType != expressionType) {
      throw new SemanticAnalysisException(
          "Type mismatch: Cannot assign " + expressionType + " to variable of type " +
              variableType + ".",
          this);
    }
  }

  @Override
  public void execute(Map<String, Function> functionMap,
      Map<String, Value> variableAndParameterMap)
      throws ReturnFromCall, ExecutionException {
    Value expressionValue = expression.evaluate(functionMap, variableAndParameterMap);
    variableAndParameterMap.put(label, expressionValue);
  }
}
