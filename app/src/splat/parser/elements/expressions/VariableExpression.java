package splat.parser.elements.expressions;

import java.util.Map;

import splat.lexer.Token;
import splat.parser.elements.Type;
import splat.parser.elements.declarations.FunctionDeclaration;
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
  public Type analyzeAndGetType(Map<String, FunctionDeclaration> functionMap, Map<String, Type> variableAndParameterMap)
      throws SemanticAnalysisException {
    if (!variableAndParameterMap.containsKey(label)) {
      throw new SemanticAnalysisException("Variable '" + label + "' is not defined.", this);
    }
    return variableAndParameterMap.get(label);
  }

  @Override
  public String toString() {
    return label;
  }
}
