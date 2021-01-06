package Day18;

class Number implements Expression {

  private final int value;

  public Number(int value) {
    this.value = value;
  }

  @Override
  public long evaluate() {
    return value;
  }
}
