package splat.parser.elements.declarations;

import splat.Utils;
import splat.lexer.Token;

public class Parameter {
	private String label;
	private String type;

	public Parameter(Token labelToken, String type) {
		this.label = labelToken.getLexeme();
		this.type = type;
	}

	public String getName() {
		return label;
	}

	public String getType() {
		return type;
	}

	@Override
	public String toString() {
		return label + " : " + type;
	}
}
