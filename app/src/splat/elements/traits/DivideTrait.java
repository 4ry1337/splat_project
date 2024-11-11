package splat.elements.traits;

import splat.elements.Value;

public interface DivideTrait {
  public abstract Value divide(Value rhs);

  public abstract Value modulus(Value rhs);
}
