package splat.parser.elements.statements;

import splat.lexer.Token;
import splat.parser.elements.expressions.Expression;

public class AssignmentStatement extends Statement {
	private String label;
	private Expression expression;

	public AssignmentStatement(Token labelToken, Expression expression) {
		super(labelToken);
		this.label = labelToken.getLexeme();
		this.expression = expression;
	}

	public String getVariableName() {
		return label;
	}

	public Expression getExpression() {
		return expression;
	}

	@Override
	public String toString() {
		return label + " := " + expression.toString() + ";";
	}
}
