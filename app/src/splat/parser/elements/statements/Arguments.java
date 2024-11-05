package splat.parser.elements.statements;

import java.util.List;

import splat.parser.elements.expressions.*;

public class Arguments {
  private List<Expression> args;

  public Arguments(List<Expression> args) {
    this.args = args;
  }

  public List<Expression> getArguments() {
    return args;
  }

  @Override
  public String toString() {
    return super.toString();
  }
}
