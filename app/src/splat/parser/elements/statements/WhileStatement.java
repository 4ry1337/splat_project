package splat.parser.elements.statements;

import java.util.List;

import splat.lexer.*;
import splat.parser.elements.expressions.*;

public class WhileStatement extends Statement {
	private Expression condition;
	private List<Statement> body;

	public WhileStatement(Expression condition, List<Statement> body, Token token) {
		super(token);
		this.condition = condition;
		this.body = body;
	}

	public Expression getCondition() {
		return condition;
	}

	public List<Statement> getBody() {
		return body;
	}

	@Override
	public String toString() {
		String result = "while " + condition.toString() + " do ";
		for (Statement statement : body) {
			result += statement.toString() + " ";
		}
		result += "end while;";
		return result;
	}
}
