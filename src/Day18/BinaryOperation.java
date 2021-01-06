package Day18;

// Currently only support addition and multiplication
class BinaryOperation implements Expression {

  private final Expression operand1;
  private final Expression operand2;
  private final Operator operator;

  public BinaryOperation(Expression operand1, Expression operand2, Operator operator) {
    this.operand1 = operand1;
    this.operand2 = operand2;
    this.operator = operator;
  }

  @Override
  public long evaluate() {
    return switch (this.operator) {
      case ADD -> operand1.evaluate() + operand2.evaluate();
      case MULTIPLY -> operand1.evaluate() * operand2.evaluate();
      default -> throw new IllegalArgumentException("Unsupported operator");
    };
  }

  // Class to hold one of 4 possible operators
  enum Operator {
    ADD(2),
    MULTIPLY(1),
    OPEN_BRACKET,
    CLOSE_BRACKET;

    private final int precedence;

    Operator(int precedence) {
      this.precedence = precedence;
    }

    Operator() {
      this.precedence = 0;
    }

    public int getPrecedence() {
      return precedence;
    }

    public static Operator of(String rawOperator) {
      return switch (rawOperator) {
        case "*" -> MULTIPLY;
        case "+" -> ADD;
        case "(" -> OPEN_BRACKET;
        case ")" -> CLOSE_BRACKET;
        default -> throw new IllegalArgumentException();
      };
    }
  }
}
