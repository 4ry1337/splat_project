package splat.elements.expressions;

import java.util.Map;

import splat.lexer.*;
import splat.semanticanalyzer.SemanticAnalysisException;
import splat.elements.*;
import splat.elements.declarations.*;
import splat.executor.ExecutionException;

public abstract class Expression extends ASTElement {
  public Expression(Token token) {
    super(token);
  }

  public abstract Type analyzeAndGetType(Map<String, Function> functionMap,
      Map<String, Type> variableAndParameterMap) throws SemanticAnalysisException;

  public abstract Value evaluate(Map<String, Function> funcMap,
      Map<String, Value> varAndParamMap) throws ExecutionException;
}
