package splat.executor;

import splat.elements.Value;

public class ReturnFromCall extends Exception {
  private Value returnVal;

  public ReturnFromCall(Value returnVal) {
    this.returnVal = returnVal;
  }

  public Value getReturnVal() {
    return returnVal;
  }
}
