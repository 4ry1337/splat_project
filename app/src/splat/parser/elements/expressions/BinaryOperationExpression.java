package splat.parser.elements.expressions;

import splat.Utils;
import splat.lexer.Token;

public class BinaryOperationExpression extends Expression {
	private Expression left;
	private Expression right;
	private String operator;

	public BinaryOperationExpression(Token token, Expression left, String operator, Expression right) {
		super(token);
		this.right = right;
		this.left = left;
		this.operator = operator;
	}

	public Expression getLeft() {
		return left;
	}

	public Expression getRight() {
		return right;
	}

	public String getOperator() {
		return operator;
	}

	@Override
	public String toString() {
		return "(" + left.toString() + " " + operator + " " + right.toString() + ")";
	}
}
