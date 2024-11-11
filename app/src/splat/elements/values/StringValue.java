package splat.elements.values;

import splat.elements.Type;
import splat.elements.Value;
import splat.elements.traits.AddTrait;
import splat.elements.traits.EqualTrait;

public class StringValue extends Value implements EqualTrait {
  private String value;

  // Constructor to initialize the string value
  public StringValue(String value) {
    this.value = value;
  }

  public String getValue() {
    return value;
  }

  @Override
  public String toString() {
    return value;
  }

  @Override
  public Type getType() {
    return Type.STRING;
  }

  // @Override
  // public StringValue add(Value rhs) {
  // if (!(rhs instanceof StringValue)) {
  // throw new IllegalArgumentException("Invalid type for addition. Expected
  // StringValue.");
  // }
  // StringValue rhsStr = (StringValue) rhs;
  // return new StringValue(this.value + rhsStr.value);
  // }

  public BooleanValue equalsTo(Value rhs) {
    if (!(rhs instanceof StringValue)) {
      throw new IllegalArgumentException("Invalid type for comparison. Expected StringValue.");
    }
    StringValue rhsStr = (StringValue) rhs;
    return new BooleanValue(this.value.equals(rhsStr.value));
  }
}
