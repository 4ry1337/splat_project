package splat.semanticanalyzer;

import java.util.List;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import splat.elements.*;
import splat.elements.declarations.*;
import splat.elements.statements.*;

public class SemanticAnalyzer {

  private ProgramAST progAST;
  private Map<String, Function> functionMap;
  private Map<String, Type> progamVariableMap;

  public SemanticAnalyzer(ProgramAST progAST) {
    this.progAST = progAST;
  }

  public void analyze() throws SemanticAnalysisException {
    checkNoDuplicateProgamLabels();

    setProgamVariableAndFunctionMaps();

    for (Function functionDeclaration : functionMap.values()) {
      analyzeFunctionDeclaration(functionDeclaration);
    }

    for (Statement statement : progAST.getStatements()) {
      statement.analyze(functionMap, progamVariableMap);
    }
  }

  private void analyzeFunctionDeclaration(Function functionDeclaration) throws SemanticAnalysisException {
    checkNoDuplicateFunctionLabels(functionDeclaration);

    Map<String, Type> variableAndParameterMap = getVariableAndParameterMap(functionDeclaration);

    variableAndParameterMap.put("0", functionDeclaration.getReturnType().getUnderlyingType());

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
      return isExprReturnTerminated(ifStatement.getThenBranch())
          && (ifStatement.getElseBranch() != null && isExprReturnTerminated(ifStatement.getElseBranch()));
    }
    return false;
  }

  private Map<String, Type> getVariableAndParameterMap(Function functionDeclaration) {
    Map<String, Type> variableAndParameterMap = new HashMap<>();

    for (Parameter param : functionDeclaration.getParameters()) {
      variableAndParameterMap.put(param.getLabel(), param.getType());
    }

    for (Variable localVariable : functionDeclaration.getLocalVariables()) {
      variableAndParameterMap.put(localVariable.getLabel(), localVariable.getType());
    }

    return variableAndParameterMap;
  }

  private void checkNoDuplicateFunctionLabels(Function functionDeclaration)
      throws SemanticAnalysisException {
    Set<String> labels = new HashSet<>();

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

    for (Variable localVariable : functionDeclaration.getLocalVariables()) {
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
    functionMap = new HashMap<>();
    progamVariableMap = new HashMap<>();
    for (Declaration declaration : progAST.getDeclarations()) {
      String label = declaration.getLabel();
      if (declaration instanceof Function) {
        Function functionDeclaration = (Function) declaration;
        functionMap.put(label, functionDeclaration);

      } else if (declaration instanceof Variable) {
        Variable variableDeclaration = (Variable) declaration;
        progamVariableMap.put(label, variableDeclaration.getType());
      }
    }
  }
}
