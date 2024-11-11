package splat.elements.declarations;

import java.util.List;

import splat.elements.ReturnType;
import splat.elements.statements.Statement;
import splat.lexer.Token;

public class Function extends Declaration {
  private String label;
  private List<Parameter> parameters;
  private ReturnType return_type;
  private List<Variable> local_variables;
  private List<Statement> statements;

  public Function(Token labelToken, List<Parameter> parameters, ReturnType return_type,
      List<Variable> local_variables, List<Statement> statements) {
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

  public List<Variable> getLocalVariables() {
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
    for (Variable localVariable : local_variables) {
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
