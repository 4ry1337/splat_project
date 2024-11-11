package splat.elements;

public enum Type {
  INTEGER("Integer"),
  BOOLEAN("Boolean"),
  STRING("String");

  private final String name;

  Type(String name) {
    this.name = name;
  }

  @Override
  public String toString() {
    return name;
  }

  // Helper method to get Type from a string representation
  public static Type fromString(String typeName) {
    switch (typeName) {
      case "Integer":
        return INTEGER;
      case "Boolean":
        return BOOLEAN;
      case "String":
        return STRING;
      default:
        throw new IllegalArgumentException("Unknown type: " + typeName);
    }
  }
}
