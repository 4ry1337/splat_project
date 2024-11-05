package splat.parser.elements.expressions;

import java.util.List;
import java.util.Map;

import splat.lexer.*;
import splat.parser.elements.ReturnType;
import splat.parser.elements.Type;
import splat.parser.elements.declarations.FunctionDeclaration;
import splat.parser.elements.declarations.Parameter;
import splat.semanticanalyzer.SemanticAnalysisException;

public class FunctionCallExpression extends Expression {
  private String label;
  private List<Expression> arguments;

  public FunctionCallExpression(Token labelToken, List<Expression> arguments) {
    super(labelToken);
    this.label = labelToken.getLexeme();
    this.arguments = arguments;
  }

  public String getLabel() {
    return label;
  }

  public List<Expression> getArguments() {
    return arguments;
  }

  @Override
  public Type analyzeAndGetType(Map<String, FunctionDeclaration> functionMap,
      Map<String, Type> variableAndParameterMap) throws SemanticAnalysisException {

    if (!functionMap.containsKey(label)) {
      throw new SemanticAnalysisException("Function '" + label + "' not defined.", this);
    }

    FunctionDeclaration functionDeclaration = functionMap.get(label);

    List<Parameter> parameters = functionDeclaration.getParameters();

    if (parameters.size() != arguments.size()) {
      throw new SemanticAnalysisException(
          "Function '" + label + "' expects " + parameters.size() + " arguments, but got " + arguments.size() + ".",
          this);
    }

    for (int i = 0; i < arguments.size(); i++) {
      Type paramType = parameters.get(i).getType();

      Type argType = arguments.get(i).analyzeAndGetType(functionMap,
          variableAndParameterMap);

      if (paramType != argType) {
        throw new SemanticAnalysisException("Argument type mismatch for parameter '" + parameters.get(i).getLabel()
            + "'. Expected " + paramType + " but got " + argType + ".", this);
      }
    }

    return functionDeclaration.getReturnType().getUnderlyingType();
  }

  @Override
  public String toString() {
    String result = label + "(";
    for (int i = 0; i < arguments.size(); i++) {
      result += arguments.get(i).toString();
      if (i < arguments.size() - 1) {
        result += ", ";
      }
    }
    result += ")";
    return result;
  }

}
