import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

public class Day9 {

  public static void main(String[] args) throws FileNotFoundException {
    BufferedReader reader = new BufferedReader(new FileReader("src/input/9.txt"));

    // Convert all strings to integers
    List<Long> data = reader.lines().map(Long::parseLong).collect(Collectors.toList());
    // Get first number that doesn't hold property described in spec
    long firstNotSum = findFirstNotSum(data, 25);
    System.out.println("The first number that doesn't hold the property is " + firstNotSum);

    long encryptionWeakness = findEncryptionWeakness(data, firstNotSum);
    System.out.println("The encryption weakness = " + encryptionWeakness);
  }

  // Iterate through data, finding sums of all contiguous data. If a sum matches 'target',
  // return the sum of the minimum and maximum number in that contiguous data range.
  private static long findEncryptionWeakness(List<Long> data, long target) {
    int first = 0; // Indices of first and last elements in contiguous sum
    int last = first;
    List<Long> summedNumbers = new LinkedList<>();
    long sum = 0;
    while (sum != target) {
      long cur = data.get(last++);
      sum += cur;
      summedNumbers.add(cur);
      if (sum > target) {
        first++;
        last = first;
        sum = 0;
        summedNumbers.clear();
      }
    }
    return summedNumbers.stream().min(Comparator.naturalOrder()).orElse(-1l)
        + summedNumbers.stream().max(Comparator.naturalOrder()).orElse(-1l);
  }

  // Iterate through all numbers in 'data' past index 'preambleSize'
  // return first one that isn't a sum of two of the last 'preambleSize' numbers in 'data'
  private static long findFirstNotSum(List<Long> data, int preambleSize) {
    for (int i = preambleSize; i < data.size(); i++) {
      if (!sumInPrevNumbers(data.get(i), data, preambleSize, i)) {
        return data.get(i);
      }
    }

    return -1; // Should not get here
  }

  // Iterate through all pairs of numbers in the 'preambleSize' numbers preceding index 'i'
  // If they sum to 'target' return true. If no such numbers exist, return false
  private static boolean sumInPrevNumbers(long target, List<Long> data, int preambleSize, int i) {
    for (int p = i - preambleSize; p < i; p++) {
      for (int q = i - preambleSize; q < i; q++) {
        if (p == q) continue;

        if (data.get(p) + data.get(q) == target) return true;
      }
    }
    return false;
  }
}
