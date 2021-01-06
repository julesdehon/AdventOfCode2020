package Day18;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

// Expression can either be a number or a binary operation
interface Expression {

  long evaluate();

  // Where most of the logic happens. Using a variant of Shunting Yard algorithm, tokenise the
  // input string, and then convert it into an Expression object using an operator and operand
  // stack.
  // https://en.wikipedia.org/wiki/Shunting-yard_algorithm for info on the algorithm
  static Expression parseExpression(String rawExpression, boolean withPrecedence) {
    Stack<BinaryOperation.Operator> operators = new Stack<>();
    Stack<Expression> operands = new Stack<>();

    for (String token : tokenise(rawExpression)) {
      // Create all previously seen binary expressions with higher precedence than the current
      // operator, and push them all to the operands stack
      switch (token) {
        case "+", "*" -> {
          BinaryOperation.Operator curOperator = BinaryOperation.Operator.of(token);
          while (!operators.empty() && operators.peek() != BinaryOperation.Operator.OPEN_BRACKET
              && (!withPrecedence || operators.peek().getPrecedence() > curOperator
              .getPrecedence())) {
            Expression e1 = operands.pop();
            Expression e2 = operands.pop();
            operands.push(new BinaryOperation(e1, e2, operators.pop()));
          }
          operators.push(curOperator);
        }
        case "(" -> operators.push(BinaryOperation.Operator.OPEN_BRACKET);
        case ")" -> {
          while (!operators.empty() && operators.peek() != BinaryOperation.Operator.OPEN_BRACKET) {
            Expression e1 = operands.pop();
            Expression e2 = operands.pop();
            operands.push(new BinaryOperation(e1, e2, operators.pop()));
          }
          operators.pop(); // Discard the matching close bracket
        }
        default -> operands.push(new Number(Integer.parseInt(token)));
      }
    }

    while (!operators.empty()) {
      Expression e1 = operands.pop();
      Expression e2 = operands.pop();
      operands.push(new BinaryOperation(e1, e2, operators.pop()));
    }

    return operands.pop();
  }

  // Simple regex based tokeniser. Splits the string into its constituent numbers and operators
  // So "1 + (2 * 10)" becomes ["1", "+", "(", "2", "*", "10", ")"]
  private static List<String> tokenise(String str) {
    List<String> result = new ArrayList<>();
    Pattern p = Pattern.compile("\\d+|[+*()]");
    Matcher m = p.matcher(str);
    while(m.find()) {
      result.add(m.group());
    }
    return result;
  }
}
