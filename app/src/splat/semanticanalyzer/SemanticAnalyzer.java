package splat.semanticanalyzer;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import splat.parser.elements.ProgramAST;
import splat.parser.elements.declarations.Declaration;
import splat.parser.elements.declarations.VariableDeclaration;
import splat.parser.elements.declarations.FunctionDeclaration;
import splat.parser.elements.declarations.Parameter;
import splat.parser.elements.statements.Statement;
import splat.parser.elements.Type;

public class SemanticAnalyzer {
  private ProgramAST progamAST;

  private Map<String, FunctionDeclaration> functionMap;
  private Map<String, Type> variableMap;

  public SemanticAnalyzer(ProgramAST progamAST) {
    this.progamAST = progamAST;
    this.functionMap = new HashMap<>();
    this.variableMap = new HashMap<>();
  }

  public void analyze() throws SemanticAnalysisException {
    checkNoDuplicateProgamLabels();
    setProgamVariableAndFunctionMaps();
    for (FunctionDeclaration functionDeclrDeclaration : functionMap.values()) {
      analyzeFunctionDeclaration(functionDeclrDeclaration);
    }
    for (Statement statement : progamAST.getStatements()) {
      statement.analyze(functionMap, variableMap);
    }
  }

  private void analyzeFunctionDeclaration(FunctionDeclaration functionDeclaration) throws SemanticAnalysisException {
    checkNoDuplicateFunctionLabels(functionDeclaration);
    Map<String, Type> variableAndParameterMap = getVariableAndParameterMap(functionDeclaration);
    for (Statement statement : functionDeclaration.getStatements()) {
      statement.analyze(functionMap, variableAndParameterMap);
    }
  }

  private Map<String, Type> getVariableAndParameterMap(FunctionDeclaration functionDeclaration) {
    Map<String, Type> variableAndParameterMap = new HashMap<>();
    for (Parameter parameter : functionDeclaration.getParameters()) {
      variableAndParameterMap.put(parameter.getLabel(), parameter.getType());
    }
    for (VariableDeclaration variableDeclaration : functionDeclaration.getLocalVariables()) {
      variableAndParameterMap.put(variableDeclaration.getLabel(), variableDeclaration.getType());
    }
    return variableAndParameterMap;
  }

  private void checkNoDuplicateFunctionLabels(FunctionDeclaration functionDeclaration)
      throws SemanticAnalysisException {
    Set<String> labels = new HashSet<String>();
    for (Declaration declaration : progamAST.getDeclarations()) {
      String label = declaration.getLabel();
      if (labels.contains(label)) {
        throw new SemanticAnalysisException("Cannot have duplicate label '"
            + label + "' in program", declaration);
      } else {
        labels.add(label);
      }
    }
  }

  private void checkNoDuplicateProgamLabels() throws SemanticAnalysisException {
    Set<String> labels = new HashSet<String>();
    for (Declaration declaration : progamAST.getDeclarations()) {
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
    for (Declaration declaration : progamAST.getDeclarations()) {
      String label = declaration.getLabel();
      if (declaration instanceof FunctionDeclaration) {
        FunctionDeclaration funcDecl = (FunctionDeclaration) declaration;
        functionMap.put(label, funcDecl);
      } else if (declaration instanceof VariableDeclaration) {
        VariableDeclaration varDecl = (VariableDeclaration) declaration;
        variableMap.put(label, varDecl.getType());
      }
    }
  }
}
