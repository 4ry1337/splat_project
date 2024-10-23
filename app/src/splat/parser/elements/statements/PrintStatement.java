package splat.parser.elements.statements;

import splat.lexer.Token;
import splat.parser.elements.expressions.Expression;

public class PrintStatement extends Statement {
	private Expression expression;

	public PrintStatement(Token printToken, Expression expression) {
		super(printToken);
		this.expression = expression;
	}

	public Expression getExpression() {
		return expression;
	}

	public void setExpression(Expression expression) {
		this.expression = expression;
	}

	@Override
	public String toString() {
		return "print " + expression.toString() + ";";
	}
}
