package splat.parser;

import java.util.ArrayList;
import java.util.List;

import splat.Utils;
import splat.lexer.*;
import splat.parser.elements.*;
import splat.parser.elements.declarations.*;
import splat.parser.elements.statements.*;
import splat.parser.elements.expressions.*;

public class Parser {
  private List<Token> tokens;

  public Parser(List<Token> tokens) {
    this.tokens = tokens;
  }

  private Token checkNext(String expected_token) throws ParseException {
    Token token = tokens.remove(0);

    if (!token.getLexeme().equals(expected_token)) {
      throw new ParseException("Expected '" + expected_token + "', got '"
          + token.getLexeme() + "'.", token);
    }

    return token;
  }

  private boolean peekNext(String expected_value) {
    return tokens.get(0).getLexeme().equals(expected_value);
  }

  private boolean peekTwoAhead(String expected_value) {
    return tokens.get(1).getLexeme().equals(expected_value);
  }

  /*
   * <program> ::= program <decls> begin <stmts> end ;
   */
  public ProgramAST parse() throws ParseException {
    try {
      Token startToken = tokens.get(0);
      checkNext("program");
      List<Declaration> declarations = parseDeclarations();
      checkNext("begin");
      List<Statement> statements = parseStatements();
      checkNext("end");
      checkNext(";");
      ProgramAST programAST = new ProgramAST(declarations, statements, startToken);
      // System.out.println(programAST);
      return programAST;
    } catch (IndexOutOfBoundsException ex) {
      throw new ParseException("Unexpectedly reached the end of file.", -1, -1);
    }
  }

  /*
   * <decls> ::= ( <decl> )*
   */
  private List<Declaration> parseDeclarations() throws ParseException {
    List<Declaration> declarations = new ArrayList<Declaration>();

    while (!peekNext("begin")) {
      declarations.add(parseDeclaration());
    }

    return declarations;
  }

  /*
   * <decl> ::= <var-decl> | <func-decl>
   */
  private Declaration parseDeclaration() throws ParseException {
    if (peekTwoAhead(":")) {
      return parseVariableDeclaration();
    } else if (peekTwoAhead("(")) {
      return parseFunctionDeclaration();
    } else {
      Token token = tokens.get(0);
      throw new ParseException("Declaration expected", token);
    }
  }

  /*
   * <var-decl> ::= <label> : <type> ;
   */
  private VariableDeclaration parseVariableDeclaration() throws ParseException {
    Token labelToken = tokens.remove(0);
    if (!Utils.isLabel(labelToken.getLexeme())) {
      throw new ParseException("Invalid variable label: " + labelToken.getLexeme(), labelToken);
    }
    checkNext(":");
    Token typeToken = tokens.remove(0);
    if (!Utils.isType(typeToken.getLexeme())) {
      throw new ParseException("Unexpected variable type: " + typeToken.getLexeme(), typeToken);
    }
    checkNext(";");
    return new VariableDeclaration(labelToken, Type.fromString(typeToken.getLexeme()));
  }

  /*
   * <func-decl> ::= <label> ( <params> ) : <ret-type>
   * is <loc-var-decls>
   * begin <stmts> end ;
   */
  private FunctionDeclaration parseFunctionDeclaration() throws ParseException {
    Token labelToken = tokens.remove(0);
    if (!Utils.isLabel(labelToken.getLexeme())) {
      throw new ParseException("Invalid function label: " + labelToken.getLexeme(), labelToken);
    }
    checkNext("(");
    List<Parameter> parameters = parseParameters();
    checkNext(")");
    checkNext(":");
    Token returnTypeToken = tokens.remove(0);
    ReturnType returnType = ReturnType.fromString(returnTypeToken.getLexeme());
    checkNext("is");
    List<VariableDeclaration> local_variable_declarations = parseLocalVariableDeclarations();
    checkNext("begin");
    List<Statement> body = parseStatements();
    checkNext("end");
    checkNext(";");
    return new FunctionDeclaration(labelToken, parameters, returnType, local_variable_declarations,
        body);
  }

  /*
   * <params> ::= <param> ( <param> )* | null
   */
  private List<Parameter> parseParameters() throws ParseException {
    List<Parameter> parameters = new ArrayList<>();
    if (!peekNext(")")) {
      parameters.add(parseParameter());
      while (peekNext(",")) {
        checkNext(",");
        parameters.add(parseParameter());
      }
    }
    return parameters;
  }

  /*
   * <param> ::= <label> : <type>
   */
  private Parameter parseParameter() throws ParseException {
    Token labelToken = tokens.remove(0);
    if (!Utils.isLabel(labelToken.getLexeme())) {
      throw new ParseException("Invalid parameter label: " + labelToken.getLexeme(), labelToken);
    }
    checkNext(":");
    Token typeToken = tokens.remove(0);
    if (!Utils.isType(typeToken.getLexeme())) {
      throw new ParseException("Unexpected parameter type: " + typeToken.getLexeme(), typeToken);
    }
    return new Parameter(labelToken, Type.fromString(typeToken.getLexeme()));
  }

  /*
   * <local-var-decls> ::= ( <var-decl> )*
   */
  private List<VariableDeclaration> parseLocalVariableDeclarations() throws ParseException {
    List<VariableDeclaration> local_variable_declarations = new ArrayList<>();
    while (!peekNext("begin")) {
      local_variable_declarations.add(parseVariableDeclaration());
    }
    return local_variable_declarations;
  }

  /*
   * <expr> ::= ( <expr> <bin-op> <expr> )
   * | ( <unary-op> <expr> )
   * | <label> ( <args> )
   * | <label>
   * | <literal>
   */
  private Expression parseExpression() throws ParseException {
    if (peekNext("(")) {
      if (Utils.isUnaryOperators(tokens.get(1).getLexeme())) {
        return parseUnaryOperationExpression();
      } else {
        return parseBinaryOperationExpression();
      }
    }
    if (peekTwoAhead("(")) {
      return parseFunctionCallExpression();
    }
    Token token = tokens.get(0);
    if (Utils.isLiteral(token.getValue())) {
      return parseLiteralExpression();
    }
    if (Utils.isLabel(token.getLexeme())) {
      return parseVariableExpression();
    }
    throw new ParseException("Invalid expression.", tokens.get(0));
  }

  /*
   * <expr> ::= ( <expr> <bin-op> <expr> )
   */
  private BinaryOperationExpression parseBinaryOperationExpression() throws ParseException {
    Token token = checkNext("(");

    Expression left = parseExpression();

    Token operatorToken = tokens.remove(0);
    if (!Utils.isBinaryOperators(operatorToken.getLexeme())) {
      throw new ParseException("Unexpected operator: " + operatorToken.getLexeme(), operatorToken);
    }

    Expression right = parseExpression();

    checkNext(")");

    return new BinaryOperationExpression(token, left, operatorToken.getLexeme(), right);
  }

  /*
   * <expr> ::= ( <unary-op> <expr> )
   */
  private UnaryOperationExpression parseUnaryOperationExpression() throws ParseException {
    Token token = checkNext("(");

    Token operatorToken = tokens.remove(0);
    if (!Utils.isUnaryOperators(operatorToken.getLexeme())) {
      throw new ParseException("Unexpected operator: " + operatorToken.getLexeme(), operatorToken);
    }

    Expression expression = parseExpression();

    checkNext(")");

    return new UnaryOperationExpression(token, operatorToken.getLexeme(), expression);
  }

  /*
   * <expr> ::= <label>
   */
  private VariableExpression parseVariableExpression() throws ParseException {
    Token labelToken = tokens.remove(0);
    if (!Utils.isLabel(labelToken.getLexeme())) {
      throw new ParseException("Unexpected variable: " + labelToken.getLexeme(), labelToken);
    }
    return new VariableExpression(labelToken);
  }

  /*
   * <expr> ::= <label> ( <args> );
   */
  private FunctionCallExpression parseFunctionCallExpression() throws ParseException {
    Token labelToken = tokens.remove(0);
    if (!Utils.isLabel(labelToken.getLexeme())) {
      throw new ParseException("Invalid label", labelToken);
    }
    checkNext("(");
    List<Expression> arguments = parseArguments();
    checkNext(")");
    return new FunctionCallExpression(labelToken, arguments);
  }

  /*
   * <args> ::= <expr> ( , <expr> )* | ɛ
   */
  private List<Expression> parseArguments() throws ParseException {
    List<Expression> arguments = new ArrayList<>();
    if (!peekNext(")")) {
      arguments.add(parseExpression());
      while (peekNext(",")) {
        checkNext(",");
        arguments.add(parseExpression());
      }
    }
    return arguments;
  }

  /*
   * <expr> ::= <literal>
   */
  private LiteralExpression parseLiteralExpression() throws ParseException {
    Token literalToken = tokens.remove(0);
    if (!Utils.isLiteral(literalToken.getValue())) {
      throw new ParseException("Invalid literal: " + literalToken.getLexeme(), literalToken);
    }
    return new LiteralExpression(literalToken);
  }

  /*
   * <stmts> ::= ( <stmt> )*
   */
  private List<Statement> parseStatements() throws ParseException {
    List<Statement> statements = new ArrayList<>();
    while (!peekNext("end") && !peekNext("else")) {
      statements.add(parseStatement());
    }
    return statements;
  }

  /*
   * <stmt> ::= <label> := <expr> ;
   * | while <expr> do <stmts> end while ;
   * | if <expr> then <stmts> else <stmts> end if ;
   * | if <expr> then <stmts> end if ;
   * | <label> ( <args> ) ;
   * | print <expr> ;
   * | print_line ;
   * | return <expr> ;
   * | return ;
   */
  private Statement parseStatement() throws ParseException {
    if (peekNext("while")) {
      return parseWhileStatement();
    } else if (peekNext("if")) {
      return parseIfStatement();
    } else if (peekNext("print")) {
      return parsePrintStatement();
    } else if (peekNext("print_line")) {
      return parsePrintLineStatement();
    } else if (peekNext("return")) {
      return parseReturnStatement();
    } else {
      if (peekTwoAhead(":=")) {
        return parseAssignmentStatement();
      } else if (peekTwoAhead("(")) {
        return parseFunctionCallStatement();
      }
    }
    throw new ParseException("Unexpected statement", tokens.get(0));
  }

  /*
   * <stmt> ::= <label> := <expr> ;
   */
  private AssignmentStatement parseAssignmentStatement() throws ParseException {
    Token labelToken = tokens.remove(0);
    if (!Utils.isLabel(labelToken.getLexeme())) {
      throw new ParseException("Invalid label", labelToken);
    }
    checkNext(":=");
    Expression expression = parseExpression();
    checkNext(";");
    return new AssignmentStatement(labelToken, expression);
  }

  /*
   * <stmt> ::= while <expr> do <stmts> end while ;
   */
  private WhileStatement parseWhileStatement() throws ParseException {
    Token whileToken = checkNext("while");
    Expression condition = parseExpression();
    checkNext("do");
    List<Statement> body = parseStatements();
    checkNext("end");
    checkNext("while");
    checkNext(";");
    return new WhileStatement(condition, body, whileToken);
  }

  /*
   * <stmt> ::= if <expr> then <stmts> else <stmts> end if ;
   * | if <expr> then <stmts> end if ;
   */
  private IfStatement parseIfStatement() throws ParseException {
    Token ifToken = checkNext("if");
    Expression condition = parseExpression();
    checkNext("then");
    List<Statement> thenBranch = parseStatements();
    List<Statement> elseBranch = null;
    if (peekNext("else")) {
      checkNext("else");
      elseBranch = parseStatements();
    }
    checkNext("end");
    checkNext("if");
    checkNext(";");
    return new IfStatement(condition, thenBranch, elseBranch, ifToken);
  }

  /*
   * <stmt> ::= <label> ( <args> );
   * ExpressionStatement evaluates expression result, in
   * this case I use it to store function call expression to
   * statement in future can be used
   */
  private ExpressionStatement parseFunctionCallStatement() throws ParseException {
    Token labelToken = tokens.get(0);
    if (!Utils.isLabel(labelToken.getLexeme())) {
      throw new ParseException("Invalid label", labelToken);
    }
    Expression expression = parseFunctionCallExpression();
    checkNext(";");
    return new ExpressionStatement(labelToken, expression);
  }

  /*
   * <stmt> ::= print <expr> ;
   */
  private PrintStatement parsePrintStatement() throws ParseException {
    Token printToken = checkNext("print");
    Expression expression = parseExpression();
    checkNext(";");
    return new PrintStatement(printToken, expression);
  }

  /*
   * <stmt> ::= print_line ;
   */
  private PrintLineStatement parsePrintLineStatement() throws ParseException {
    Token printLineToken = checkNext("print_line");
    checkNext(";");
    return new PrintLineStatement(printLineToken);
  }

  /*
   * <stmt> ::= return <expr> ; | return ;
   */
  private ReturnStatement parseReturnStatement() throws ParseException {
    Token returnToken = checkNext("return");
    Expression expression = null;
    if (!peekNext(";")) {
      expression = parseExpression();
    }
    checkNext(";");
    return new ReturnStatement(returnToken, expression);
  }
}
