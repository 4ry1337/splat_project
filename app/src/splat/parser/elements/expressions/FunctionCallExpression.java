package splat.parser.elements.expressions;

import java.util.List;

import splat.lexer.*;

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
}
