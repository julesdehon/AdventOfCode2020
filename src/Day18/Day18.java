package Day18;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.List;
import java.util.stream.Collectors;

public class Day18 {

  public static void main(String[] args) throws FileNotFoundException {
    BufferedReader reader = new BufferedReader(new FileReader("src/input/18.txt"));
    List<String> expressions = reader.lines().collect(Collectors.toList());

    long part1 =
        expressions.stream()
            .map(s -> Expression.parseExpression(s, false))
            .map(Expression::evaluate)
            .reduce(0L, Long::sum);
    System.out.println("The sum resulting from evaluating each expression = " + part1);

    long part2 =
        expressions.stream()
            .map(s -> Expression.parseExpression(s, true))
            .map(Expression::evaluate)
            .reduce(0L, Long::sum);
    System.out.println(
        "The sum resulting from evaluating each expression in advanced math = " + part2);
  }
}
