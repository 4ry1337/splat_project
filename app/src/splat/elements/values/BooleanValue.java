package splat.elements.values;

import splat.elements.Type;
import splat.elements.Value;
import splat.elements.traits.*;

public class BooleanValue extends Value implements AndTrait, OrTrait, NotTrait, EqualTrait {
  private boolean value;

  public BooleanValue(boolean value) {
    this.value = value;
  }

  public boolean getValue() {
    return this.value;
  }

  @Override
  public Type getType() {
    return Type.BOOLEAN;
  }

  @Override
  public String toString() {
    return String.valueOf(value);
  }

  @Override
  public BooleanValue not() {
    return new BooleanValue(!this.value);
  }

  public BooleanValue and(Value rhs) {
    if (!(rhs instanceof BooleanValue)) {
      throw new IllegalArgumentException("Invalid type for logical AND. Expected BooleanValue.");
    }
    BooleanValue rhsBool = (BooleanValue) rhs;
    return new BooleanValue(this.value && rhsBool.value);
  }

  public BooleanValue or(Value rhs) {
    if (!(rhs instanceof BooleanValue)) {
      throw new IllegalArgumentException("Invalid type for logical OR. Expected BooleanValue.");
    }
    BooleanValue rhsBool = (BooleanValue) rhs;
    return new BooleanValue(this.value || rhsBool.value);
  }

  public BooleanValue equalsTo(Value rhs) {
    if (!(rhs instanceof BooleanValue)) {
      throw new IllegalArgumentException("Invalid type for comparison. Expected BooleanValue.");
    }
    BooleanValue rhsBool = (BooleanValue) rhs;
    return new BooleanValue(this.value == rhsBool.value);
  }
}
