package splat.elements.statements;

import java.util.Map;

import splat.lexer.Token;
import splat.elements.*;
import splat.semanticanalyzer.SemanticAnalysisException;
import splat.elements.declarations.Function;
import splat.executor.*;

public abstract class Statement extends ASTElement {
  public Statement(Token token) {
    super(token);
  }

  /**
   * This will be needed for Phase 3 - this abstract method will need to be
   * implemented by every Statement subclass. This method essentially does
   * semantic analysis on the statement, and all sub-expressions that might
   * make up the statement. funcMap and varAndParamMap are needed for
   * performing semantic analysis and type retrieval for the
   * sub-expressions.
   */
  public abstract void analyze(Map<String, Function> functionMap,
      Map<String, Type> variableAndParameterMap) throws SemanticAnalysisException;

  /**
   * This will be needed for Phase 4 - this abstract method will need to be
   * implemented by every Statement subclass. This method is used to
   * execute each statement, which may result in output to the console, or
   * updating the varAndParamMap. Both of the given maps may be needed for
   * evaluating any sub-expressions in the statement.
   */
  public abstract void execute(Map<String, Function> functionMap, Map<String, Value> variableAndParameterMap)
      throws ReturnFromCall, ExecutionException;
}
