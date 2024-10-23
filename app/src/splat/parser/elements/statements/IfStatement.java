package splat.parser.elements.statements;

import java.util.List;

import splat.lexer.Token;
import splat.parser.elements.expressions.Expression;

public class IfStatement extends Statement {
	private Expression condition;
	private List<Statement> thenBranch;
	private List<Statement> elseBranch;

	public IfStatement(Expression condition, List<Statement> thenBranch, List<Statement> elseBranch, Token token) {
		super(token);
		this.condition = condition;
		this.thenBranch = thenBranch;
		this.elseBranch = elseBranch;
	}

	public Expression getCondition() {
		return condition;
	}

	public List<Statement> getThenBranch() {
		return thenBranch;
	}

	public List<Statement> getElseBranch() {
		return elseBranch;
	}

	@Override
	public String toString() {
		String result = "if " + condition.toString() + " then ";
		for (Statement statement : thenBranch) {
			result += statement.toString() + " ";
		}
		if (elseBranch != null && !elseBranch.isEmpty()) {
			result += "else ";
			for (Statement statement : elseBranch) {
				result += statement.toString() + " ";
			}
		}
		result += "end if;";
		return result;
	}
}
