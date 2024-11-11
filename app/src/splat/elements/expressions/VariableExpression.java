package splat.elements.expressions;

import java.util.Map;

import splat.elements.Type;
import splat.elements.Value;
import splat.elements.declarations.Function;
import splat.lexer.Token;
import splat.semanticanalyzer.SemanticAnalysisException;

public class VariableExpression extends Expression {
  private String label;

  public VariableExpression(Token labelToken) {
    super(labelToken);
    this.label = labelToken.getLexeme();
  }

  public String getLabel() {
    return label;
  }

  @Override
  public String toString() {
    return label;
  }

  @Override
  public Type analyzeAndGetType(Map<String, Function> functionMap,
      Map<String, Type> variableAndParameterMap)
      throws SemanticAnalysisException {

    if (!variableAndParameterMap.containsKey(label)) {
      throw new SemanticAnalysisException("Variable '" + label + "' not defined.", this);
    }

    return variableAndParameterMap.get(label);
  }

  @Override
  public Value evaluate(Map<String, Function> functionMap, Map<String, Value> variableAndParameterMap) {
    return variableAndParameterMap.get(label);
  }
}
