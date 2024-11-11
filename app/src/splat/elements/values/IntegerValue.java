package splat.elements.values;

import splat.elements.Type;
import splat.elements.Value;
import splat.elements.traits.*;

public class IntegerValue extends Value
    implements AddTrait, SubtractTrait, MultiplyTrait, DivideTrait, NotTrait, OrderTrait, EqualTrait {
  private int value;

  public IntegerValue(int value) {
    this.value = value;
  }

  public int getValue() {
    return value;
  }

  @Override
  public Type getType() {
    return Type.INTEGER;
  }

  @Override
  public String toString() {
    return String.valueOf(value);
  }

  public IntegerValue add(Value rhs) {
    if (!(rhs instanceof IntegerValue)) {
      throw new IllegalArgumentException("Invalid type for addition. Expected IntegerValue.");
    }
    IntegerValue rhsInt = (IntegerValue) rhs;
    return new IntegerValue(this.value + rhsInt.value);
  }

  public IntegerValue subtract(Value rhs) {
    if (!(rhs instanceof IntegerValue)) {
      throw new IllegalArgumentException("Invalid type for subtraction. Expected IntegerValue.");
    }
    IntegerValue rhsInt = (IntegerValue) rhs;
    return new IntegerValue(this.value - rhsInt.value);
  }

  public IntegerValue multiply(Value rhs) {
    if (!(rhs instanceof IntegerValue)) {
      throw new IllegalArgumentException("Invalid type for multiplication. Expected IntegerValue.");
    }
    IntegerValue rhsInt = (IntegerValue) rhs;
    return new IntegerValue(this.value * rhsInt.value);
  }

  public IntegerValue divide(Value rhs) {
    if (!(rhs instanceof IntegerValue)) {
      throw new IllegalArgumentException("Invalid type for division. Expected IntegerValue.");
    }
    IntegerValue rhsInt = (IntegerValue) rhs;
    if (rhsInt.value == 0) {
      throw new ArithmeticException("Division by zero.");
    }
    return new IntegerValue(this.value / rhsInt.value);
  }

  public IntegerValue modulus(Value rhs) {
    if (!(rhs instanceof IntegerValue)) {
      throw new IllegalArgumentException("Invalid type for modulus. Expected IntegerValue.");
    }
    IntegerValue rhsInt = (IntegerValue) rhs;
    if (rhsInt.value == 0) {
      throw new ArithmeticException("Modulus by zero.");
    }
    return new IntegerValue(this.value % rhsInt.value);
  }

  public BooleanValue lessThan(Value rhs) {
    if (!(rhs instanceof IntegerValue)) {
      throw new IllegalArgumentException("Invalid type for comparison. Expected IntegerValue.");
    }
    IntegerValue rhsInt = (IntegerValue) rhs;
    return new BooleanValue(this.value < rhsInt.value);
  }

  public BooleanValue greaterThan(Value rhs) {
    if (!(rhs instanceof IntegerValue)) {
      throw new IllegalArgumentException("Invalid type for comparison. Expected IntegerValue.");
    }
    IntegerValue rhsInt = (IntegerValue) rhs;
    return new BooleanValue(this.value > rhsInt.value);
  }

  public BooleanValue equalsTo(Value rhs) {
    if (!(rhs instanceof IntegerValue)) {
      throw new IllegalArgumentException("Invalid type for comparison. Expected IntegerValue.");
    }
    IntegerValue rhsInt = (IntegerValue) rhs;
    return new BooleanValue(this.value == rhsInt.value);
  }

  public IntegerValue not() {
    return new IntegerValue(-this.value);
  }
}
