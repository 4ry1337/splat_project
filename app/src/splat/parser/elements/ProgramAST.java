package splat.parser.elements;

import java.util.List;

import splat.lexer.*;
import splat.parser.elements.declarations.*;
import splat.parser.elements.expressions.*;
import splat.parser.elements.statements.*;

public class ProgramAST extends ASTElement {
	private List<Declaration> declarations;
	private List<Statement> statements;

	public ProgramAST(List<Declaration> declarations, List<Statement> statements, Token token) {
		super(token);
		this.declarations = declarations;
		this.statements = statements;
	}

	public List<Declaration> getDeclarations() {
		return declarations;
	}

	public List<Statement> getStatements() {
		return statements;
	}

	@Override
	public String toString() {
		String result = "program\n";
		for (Declaration decl : declarations) {
			result += "   " + decl + "\n";
		}
		result += "begin \n";
		for (Statement stmt : statements) {
			result += "   " + stmt + "\n";
		}
		result += "end;";
		return result;
	}
}
