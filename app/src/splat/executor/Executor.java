package splat.executor;

import java.util.HashMap;
import java.util.Map;

import splat.elements.*;
import splat.elements.declarations.*;
import splat.elements.statements.Statement;
import splat.elements.values.*;

public class Executor {
  private ProgramAST progAST;

  private Map<String, Function> functionMap;
  private Map<String, Value> progamVariableMap;

  public Executor(ProgramAST progAST) {
    this.progAST = progAST;
  }

  public void runProgram() throws ExecutionException {
    setMaps();

    try {
      for (Statement statement : progAST.getStatements()) {
        statement.execute(functionMap, progamVariableMap);
      }
    } catch (ReturnFromCall e) {
      System.out.println("Internal error!!! The main program body "
          + "cannot have a return statement -- this should have "
          + "been caught during semantic analysis!");

      throw new ExecutionException("Internal error -- fix your "
          + "semantic analyzer!", -1, -1);
    }
  }

  private void setMaps() {
    functionMap = new HashMap<>();
    progamVariableMap = new HashMap<>();
    for (Declaration declaration : progAST.getDeclarations()) {
      if (declaration instanceof Function) {
        Function functionDeclaration = (Function) declaration;
        functionMap.put(functionDeclaration.getLabel(), functionDeclaration);
      } else if (declaration instanceof Variable) {
        Variable variableDeclaration = (Variable) declaration;
        Type type = variableDeclaration.getType();
        Value defaultValue;

        if (type == Type.INTEGER) {
          defaultValue = new IntegerValue(0);
        } else if (type == Type.BOOLEAN) {
          defaultValue = new BooleanValue(false);
        } else {
          defaultValue = new StringValue("");
        }

        progamVariableMap.put(variableDeclaration.getLabel(), defaultValue);
      }
    }
  }
}
