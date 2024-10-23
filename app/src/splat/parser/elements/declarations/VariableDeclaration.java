package splat.parser.elements.declarations;

import splat.lexer.Token;

public class VariableDeclaration extends Declaration {
	private String label;
	private String type;

	public VariableDeclaration(Token labelToken, String type) {
		super(labelToken);
		this.label = labelToken.getLexeme();
		this.type = type;
	}

	public String getType() {
		return type;
	}

	public String getLabel() {
		return label;
	}

	@Override
	public String toString() {
		return label + " : " + type + ";";
	}
}
