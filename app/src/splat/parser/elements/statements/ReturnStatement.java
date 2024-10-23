package splat.parser.elements.statements;

import splat.lexer.Token;
import splat.parser.elements.expressions.Expression;

public class ReturnStatement extends Statement {
	private Expression expression;

	public ReturnStatement(Token returnToken, Expression expression) {
		super(returnToken);
		this.expression = expression;
	}

	public Expression getExpression() {
		return expression;
	}

	@Override
	public String toString() {
		return "return" + (expression != null ? " " + expression.toString() : "") + ";";
	}
}
