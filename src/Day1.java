import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.List;
import java.util.stream.Collectors;

public class Day1 {

  public static int twoSumTo2020(List<Integer> numbers) {
    for (int num1 : numbers) {
      for (int num2 : numbers) {
        if (num1 + num2 == 2020) {
          return num1 * num2;
        }
      }
    }
    return -1;
  }

  public static int threeSumTo2020(List<Integer> numbers) {
    for (int num1 : numbers) {
      for (int num2 : numbers) {
        for (int num3 : numbers) {
          if (num1 + num2 + num3 == 2020) {
            return num1 * num2 * num3;
          }
        }
      }
    }
    return -1;
  }

  public static void main(String[] args) throws FileNotFoundException {
    BufferedReader reader = new BufferedReader(new FileReader("src/input/1.txt"));
    List<Integer> nums = reader.lines().map(Integer::parseInt).collect(Collectors.toList());
    System.out.println("Part 1 = " + twoSumTo2020(nums));
    System.out.println("Part 2 = " + threeSumTo2020(nums));
  }
}