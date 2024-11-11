package splat.elements.expressions;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import splat.lexer.*;
import splat.elements.*;
import splat.elements.declarations.*;
import splat.elements.statements.Statement;
import splat.elements.values.*;
import splat.executor.*;
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

  @Override
  public Type analyzeAndGetType(Map<String, Function> functionMap,
      Map<String, Type> variableAndParameterMap) throws SemanticAnalysisException {
    if (!functionMap.containsKey(label)) {
      throw new SemanticAnalysisException("Function '" + label + "' not defined.", this);
    }

    Function function = functionMap.get(label);

    List<Parameter> parameters = function.getParameters();

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

    return function.getReturnType().getUnderlyingType();
  }

  @Override
  public Value evaluate(Map<String, Function> functionMap, Map<String, Value> variableAndParameterMap)
      throws ExecutionException {
    Function function = functionMap.get(label);

    Map<String, Value> localMap = new HashMap<>();
    List<Parameter> parameters = function.getParameters();

    for (int i = 0; i < parameters.size(); i++) {
      Value argValue = arguments.get(i).evaluate(functionMap, variableAndParameterMap);
      localMap.put(parameters.get(i).getLabel(), argValue);
    }

    for (Variable localVar : function.getLocalVariables()) {
      Type type = localVar.getType();
      Value defaultValue;

      if (type == Type.INTEGER) {
        defaultValue = new IntegerValue(0);
      } else if (type == Type.BOOLEAN) {
        defaultValue = new BooleanValue(false);
      } else {
        defaultValue = new StringValue("");
      }

      localMap.put(localVar.getLabel(), defaultValue);
    }

    try {
      for (Statement statements : function.getStatements()) {
        statements.execute(functionMap, localMap);
      }
    } catch (ReturnFromCall returnException) {
      return returnException.getReturnVal();
    }

    return null;
  }
}
