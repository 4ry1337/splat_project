package splat.parser.elements.expressions;

import splat.lexer.Token;

public class LiteralExpression extends Expression {
	private String valueString;
	private Object value;

	public LiteralExpression(Token value) {
		super(value);
		this.value = value.getValue();
		this.valueString = value.getLexeme();
	}

	public Object getValue() {
		return value;
	}

	@Override
	public String toString() {
		return valueString;
	}
}
