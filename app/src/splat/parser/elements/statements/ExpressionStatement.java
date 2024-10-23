package splat.parser.elements.statements;

import splat.lexer.Token;
import splat.parser.elements.expressions.Expression;

public class ExpressionStatement extends Statement {
	private Expression expression;

	public ExpressionStatement(Token token, Expression expression) {
		super(token);
		this.expression = expression;
	}

	public Expression getExpression() {
		return expression;
	}

	@Override
	public String toString() {
		return expression.toString() + ";";
	}
}
