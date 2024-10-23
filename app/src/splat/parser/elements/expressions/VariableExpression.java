package splat.parser.elements.expressions;

import splat.lexer.Token;

public class VariableExpression extends Expression {
	private String label;

	public VariableExpression(Token labelToken) {
		super(labelToken);
		this.label = labelToken.getLexeme();
	}

	public String getLabel() {
		return label;
	}

	@Override
	public String toString() {
		return label;
	}
}
