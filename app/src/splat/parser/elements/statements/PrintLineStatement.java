package splat.parser.elements.statements;

import splat.lexer.Token;

public class PrintLineStatement extends Statement {
	public PrintLineStatement(Token printLineToken) {
		super(printLineToken);
	}

	@Override
	public String toString() {
		return "print_line;";
	}
}
