public class Pair<K, V> {
  private K type;
  private V val;
  private boolean success;
  private String var;

  public Pair(K type, V val) {
    this.type = type;
    this.val = val;
    this.success = false;
    this.var = "";
  }

  public K getType() {
    return type;
  }

  public V getValue() {
    return val;
  }

  public void setType(K type) {
    this.type = type;
  }

  public void setValue(V val) {
    this.val = val;
  }

  public void setVar(String var) {
    this.var = var;
  }

  @SuppressWarnings("rawtypes")
  public boolean equals(Pair p) {
    return this.type.equals(p.getType()) && this.val.equals(p.getValue());
  }

  public String toString() {
    return "(type = " + type.toString() + ", value = " + val.toString() + ")";
  }

  public void setIsValid(boolean isValid) {
    this.success = isValid;
  }

  public boolean isValid() {
    return success;
  }

  public String getVar() {
    return var;
  }
}