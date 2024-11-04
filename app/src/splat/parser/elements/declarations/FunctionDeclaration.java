package splat.parser.elements.declarations;

import java.util.List;

import splat.lexer.Token;
import splat.parser.elements.ReturnType;
import splat.parser.elements.statements.Statement;

public class FunctionDeclaration extends Declaration {
  private String label;
  private List<Parameter> parameters;
  private ReturnType return_type;
  private List<VariableDeclaration> local_variables;
  private List<Statement> statements;

  public FunctionDeclaration(Token labelToken, List<Parameter> parameters, ReturnType return_type,
      List<VariableDeclaration> local_variables, List<Statement> statements) {
    super(labelToken);
    this.label = labelToken.getLexeme();
    this.parameters = parameters;
    this.return_type = return_type;
    this.local_variables = local_variables;
    this.statements = statements;
  }

  public String getLabel() {
    return label;
  }

  public List<Statement> getStatements() {
    return statements;
  }

  public ReturnType getReturnType() {
    return return_type;
  }

  public List<Parameter> getParameters() {
    return parameters;
  }

  public List<VariableDeclaration> getLocalVariables() {
    return local_variables;
  }

  @Override
  public String toString() {
    String result = label + "(";
    for (int i = 0; i < parameters.size(); i++) {
      result += parameters.get(i);
      if (i < parameters.size() - 1) {
        result += ", ";
      }
    }
    result += ") : " + return_type + " is ";
    for (VariableDeclaration localVariable : local_variables) {
      result += localVariable.toString() + " ";
    }
    result += "begin\n";
    for (Statement statement : statements) {
      result += statement.toString() + " ";
    }
    result += "end;";
    return result;
  }
}
