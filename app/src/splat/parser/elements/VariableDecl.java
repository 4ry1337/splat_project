package splat.parser.elements;

import splat.lexer.Token;

public class VariableDecl extends Declaration {
	private String label;
	private String type;

	// Need to add extra arguments for setting fields in the constructor
	public VariableDecl(Token label, Token type) {
		super(token);
	}

	public String getLabel() {
		return label;
	}

	public String getType() {
		return type;
	}

	public String toString() {
		return label + " " + type;
	}
}
