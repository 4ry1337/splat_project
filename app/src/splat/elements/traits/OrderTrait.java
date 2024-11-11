package splat.elements.traits;

import splat.elements.Value;
import splat.elements.values.BooleanValue;

public interface OrderTrait {
  public abstract BooleanValue lessThan(Value rhs);

  public abstract BooleanValue greaterThan(Value rhs);
}
