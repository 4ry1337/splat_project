package splat.parser.elements.declarations;

import java.util.List;

import splat.Utils;
import splat.lexer.Token;
import splat.parser.elements.statements.Statement;

public class FunctionDeclaration extends Declaration {
	private String label;
	private List<Parameter> parameters;
	private String return_type;
	private List<VariableDeclaration> local_variables;
	private List<Statement> body;

	public FunctionDeclaration(Token labelToken, List<Parameter> parameters, String return_type,
			List<VariableDeclaration> local_variables, List<Statement> body) {
		super(labelToken);
		this.label = labelToken.getLexeme();
		this.parameters = parameters;
		this.return_type = return_type;
		this.local_variables = local_variables;
		this.body = body;
	}

	public String getLabel() {
		return label;
	}

	public List<Statement> getBody() {
		return body;
	}

	public String getReturnType() {
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
		for (Statement statement : body) {
			result += statement.toString() + " ";
		}
		result += "end;";
		return result;
	}
}
