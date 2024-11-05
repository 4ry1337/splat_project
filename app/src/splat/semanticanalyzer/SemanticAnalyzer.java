package splat.semanticanalyzer;

import java.util.List;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import splat.parser.elements.ProgramAST;
import splat.parser.elements.ReturnType;
import splat.parser.elements.Type;
import splat.parser.elements.declarations.Declaration;
import splat.parser.elements.declarations.FunctionDeclaration;
import splat.parser.elements.declarations.Parameter;
import splat.parser.elements.declarations.VariableDeclaration;
import splat.parser.elements.statements.IfStatement;
import splat.parser.elements.statements.ReturnStatement;
import splat.parser.elements.statements.Statement;

public class SemanticAnalyzer {

  private ProgramAST progAST;
  private Map<String, FunctionDeclaration> functionMap = new HashMap<>();
  private Map<String, Type> progamVariableMap = new HashMap<>();

  public SemanticAnalyzer(ProgramAST progAST) {
    this.progAST = progAST;
  }

  public void analyze() throws SemanticAnalysisException {
    // Checks to make sure we don't use the same labels more than once
    // for our program functions and variables
    checkNoDuplicateProgamLabels();

    // This sets the maps that will be needed later when we need to
    // typecheck variable references and function calls in the
    // program body
    setProgamVariableAndFunctionMaps();

    // Perform semantic analysis on the functions
    for (FunctionDeclaration functionDeclaration : functionMap.values()) {
      analyzeFunctionDeclaration(functionDeclaration);
    }

    // Perform semantic analysis on the program body
    for (Statement statement : progAST.getStatements()) {
      statement.analyze(functionMap, progamVariableMap);
    }
  }

  private void analyzeFunctionDeclaration(FunctionDeclaration functionDeclaration) throws SemanticAnalysisException {
    // Checks to make sure we don't use the same labels more than once
    // among our function parameters, local variables, and function names
    checkNoDuplicateFunctionLabels(functionDeclaration);

    // Get the types of the parameters and local variables
    Map<String, Type> variableAndParameterMap = getVariableAndParameterMap(functionDeclaration);

    // Add the function's return type to the map under a unique key
    variableAndParameterMap.put("0", functionDeclaration.getReturnType().getUnderlyingType());

    // Perform semantic analysis on the function body
    List<Statement> body = functionDeclaration.getStatements();

    if (functionDeclaration.getReturnType() != ReturnType.VOID && !isExprReturnTerminated(body)) {
      throw new SemanticAnalysisException(
          "Non-void function '" + functionDeclaration.getLabel() + "' missing return statement.",
          functionDeclaration.getLine(), functionDeclaration.getColumn());
    }

    for (Statement statement : body) {
      statement.analyze(functionMap, variableAndParameterMap);
    }
  }

  private boolean isExprReturnTerminated(List<Statement> statements) {
    if (statements.isEmpty())
      return false;

    Statement lastStatement = statements.get(statements.size() - 1);
    if (lastStatement instanceof ReturnStatement && ((ReturnStatement) lastStatement).getExpression() != null) {
      return true;
    } else if (lastStatement instanceof IfStatement) {
      IfStatement ifStatement = (IfStatement) lastStatement;
      // Ensure both branches are expr-return-terminated
      return isExprReturnTerminated(ifStatement.getThenBranch())
          && (ifStatement.getElseBranch() != null && isExprReturnTerminated(ifStatement.getElseBranch()));
    }
    return false;
  }

  private Map<String, Type> getVariableAndParameterMap(FunctionDeclaration functionDeclaration) {
    Map<String, Type> variableAndParameterMap = new HashMap<>();

    // Add parameters
    for (Parameter param : functionDeclaration.getParameters()) {
      variableAndParameterMap.put(param.getLabel(), param.getType());
    }

    // Add local variables
    for (VariableDeclaration localVariable : functionDeclaration.getLocalVariables()) {
      variableAndParameterMap.put(localVariable.getLabel(), localVariable.getType());
    }

    return variableAndParameterMap;
  }

  private void checkNoDuplicateFunctionLabels(FunctionDeclaration functionDeclaration)
      throws SemanticAnalysisException {
    Set<String> labels = new HashSet<>();

    // Check parameters
    for (Parameter parameter : functionDeclaration.getParameters()) {
      if (labels.contains(parameter.getLabel())) {
        throw new SemanticAnalysisException("Duplicate parameter label '" + parameter.getLabel()
            + "' in function '" + functionDeclaration.getLabel() + "'", functionDeclaration);
      }
      if (functionMap.containsKey(parameter.getLabel())) {
        throw new SemanticAnalysisException("Parameter label '" + parameter.getLabel() + "' in function '"
            + functionDeclaration.getLabel() + "' conflicts with a global function name", functionDeclaration);
      }
      labels.add(parameter.getLabel());
    }

    // Check local variables
    for (VariableDeclaration localVariable : functionDeclaration.getLocalVariables()) {
      if (labels.contains(localVariable.getLabel())) {
        throw new SemanticAnalysisException("Duplicate local variable label '" + localVariable.getLabel()
            + "' in function '" + functionDeclaration.getLabel() + "'", functionDeclaration);
      }
      if (functionMap.containsKey(localVariable.getLabel())) {
        throw new SemanticAnalysisException("Local Variable label '" + localVariable.getLabel() + "' in function '"
            + functionDeclaration.getLabel() + "' conflicts with a global function name", functionDeclaration);
      }
      labels.add(localVariable.getLabel());
    }

    // Check function label
    if (labels.contains(functionDeclaration.getLabel())) {
      throw new SemanticAnalysisException("Function label conflicts with a parameter or local variable in function '"
          + functionDeclaration.getLabel() + "'", functionDeclaration);
    }
  }

  private void checkNoDuplicateProgamLabels() throws SemanticAnalysisException {
    Set<String> labels = new HashSet<String>();

    for (Declaration declaration : progAST.getDeclarations()) {
      String label = declaration.getLabel();
      if (labels.contains(label)) {
        throw new SemanticAnalysisException("Cannot have duplicate label '"
            + label + "' in program", declaration);
      } else {
        labels.add(label);
      }

    }
  }

  private void setProgamVariableAndFunctionMaps() {
    for (Declaration declaration : progAST.getDeclarations()) {
      String label = declaration.getLabel();
      if (declaration instanceof FunctionDeclaration) {
        FunctionDeclaration functionDeclaration = (FunctionDeclaration) declaration;
        functionMap.put(label, functionDeclaration);

      } else if (declaration instanceof VariableDeclaration) {
        VariableDeclaration variableDeclaration = (VariableDeclaration) declaration;
        progamVariableMap.put(label, variableDeclaration.getType());
      }
    }
  }
}
