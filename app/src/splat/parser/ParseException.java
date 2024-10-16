package splat.parser;

import splat.SplatException;
import splat.lexer.Token;

public class ParseException extends SplatException {
	public ParseException(String message, Token token) {
		super(message, token.getLine(), token.getColumn());
	}

	public ParseException(String message, int line, int column) {
		super(message, line, column);
	}
}
