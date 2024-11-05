package splat.parser.elements.statements;

import java.util.Map;

import splat.lexer.Token;
import splat.parser.elements.Type;
import splat.parser.elements.declarations.FunctionDeclaration;
import splat.parser.elements.expressions.Expression;
import splat.semanticanalyzer.SemanticAnalysisException;

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
  public void analyze(Map<String, FunctionDeclaration> functionMap, Map<String, Type> variableAndParameterMap)
      throws SemanticAnalysisException {
    if (!variableAndParameterMap.containsKey(label)) {
      throw new SemanticAnalysisException("Variable '" + label + "' not defined.", this);
    }

    Type variableType = variableAndParameterMap.get(label);
    Type expressionType = expression.analyzeAndGetType(functionMap, variableAndParameterMap);

    if (variableType != expressionType) {
      throw new SemanticAnalysisException(
          "Type mismatch: Cannot assign " + expressionType + " to variable of type " + variableType + ".", this);
    }
  }

  @Override
  public String toString() {
    return label + " := " + expression.toString() + ";";
  }
}
