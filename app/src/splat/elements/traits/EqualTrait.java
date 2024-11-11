package splat.elements.traits;

import splat.elements.Value;
import splat.elements.values.BooleanValue;

public interface EqualTrait {
  public abstract BooleanValue equalsTo(Value rhs);
}
