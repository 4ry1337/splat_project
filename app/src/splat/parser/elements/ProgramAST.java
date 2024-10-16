package splat.parser.elements;

import java.util.List;

import splat.lexer.Token;

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

	public String toString() {
		String result = "program \n";
		for (Declaration decl : declarations) {
			result = result + "   " + decl + "\n";
		}
		result = result + "begin \n";
		for (Statement stmt : statements) {
			result = result + "   " + stmt + "\n";
		}
		result = result + "end;";

		return result;
	}
}
