package splat.parser.elements.expressions;

import splat.lexer.Token;

public class UnaryOperationExpression extends Expression {
	private String operator;
	private Expression expression;

	public UnaryOperationExpression(Token token, String operator, Expression expression) {
		super(token);
		this.operator = operator;
		this.expression = expression;
	}

	public String getOperator() {
		return operator;
	}

	public Expression getExpression() {
		return expression;
	}

	@Override
	public String toString() {
		return "(" + operator + " " + expression.toString() + ")";
	}
}
