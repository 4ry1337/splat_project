package splat.elements;

public enum ReturnType {
  INTEGER(Type.INTEGER),
  BOOLEAN(Type.BOOLEAN),
  STRING(Type.STRING),
  VOID(null);

  private final Type underlyingType;

  ReturnType(Type underlyingType) {
    this.underlyingType = underlyingType;
  }

  public Type getUnderlyingType() {
    return underlyingType;
  }

  @Override
  public String toString() {
    return this == VOID ? "void" : underlyingType.toString();
  }

  // Helper method to get ReturnType from a string representation
  public static ReturnType fromString(String returnTypeName) {
    switch (returnTypeName) {
      case "Integer":
        return INTEGER;
      case "Boolean":
        return BOOLEAN;
      case "String":
        return STRING;
      case "void":
        return VOID;
      default:
        throw new IllegalArgumentException("Unknown return type: " + returnTypeName);
    }
  }
}
