import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class Day10 {

  public static void main(String[] args) throws FileNotFoundException {
    BufferedReader reader = new BufferedReader(new FileReader("src/input/10.txt"));

    List<Integer> sortedAdapters =
        reader.lines().map(Integer::parseInt).sorted().collect(Collectors.toList());
    // Add device adapter to end of list of adapters and 0 to beginning
    sortedAdapters.add(sortedAdapters.get(sortedAdapters.size() - 1) + 3);
    sortedAdapters.add(0, 0);

    int oneTimesThreeJoltDiff = getOneTimesThreeJoltDiff(sortedAdapters);
    System.out.println(
        "The number of 1-jolt differences multiplied by the number of 3-jolt differences = "
            + oneTimesThreeJoltDiff);

//    long possibleArrangements = countPossibleArrangementsNaive(sortedAdapters, 0);  // <--- Naive implementation doesn't work with larger data set.
    long possibleArrangements = countAdapterArrangements(sortedAdapters);
    System.out.println(possibleArrangements);
  }

  // PART 1
  private static int getOneTimesThreeJoltDiff(List<Integer> sortedAdapters) {
    // Iterate through the adapters, adding 1 to joltDiffs[n] where n is the jolt difference
    // between two adjacent adapters
    int[] joltDiffs = new int[3];
    // joltDiffs[sortedAdapters.get(0) - 1]++;
    for (int i = 0; i < sortedAdapters.size() - 1; i++) {
      joltDiffs[sortedAdapters.get(i + 1) - sortedAdapters.get(i) - 1]++;
    }

    return joltDiffs[0] * joltDiffs[2];
  }

  /* ----------- */

  // PART 2

  // Better implementation (dynamic programming)
  private static long countAdapterArrangements(List<Integer> sortedAdapters) {
    List<Long> table = tabulateAdapterArrangements(sortedAdapters);
    // Once tabulated, the answer we're looking for is in table[0]
    return table.get(0);
  }

  // Returns a table T such that the value at T[i] refers to the number of ways the adapters
  // after sortedAdapters[i] could be arranged
  private static List<Long> tabulateAdapterArrangements(List<Integer> sortedAdapters) {
    List<Long> table = new ArrayList<>();
    for (int i = 0; i < sortedAdapters.size(); i++) table.add(0l); // Initialise all to 0

    // The last adapter only has 1 possible arrangement - set T[last] = 1
    table.set(sortedAdapters.size() - 1, 1l);

    // Go down through the table - each T[i] = the sum of the possible arrangements from each
    // of the adapters that can be plugged into sortedAdapters[i]
    for (int i = sortedAdapters.size() - 2; i >= 0; i--) {
      int usableAdapters = countUsableAdapters(sortedAdapters, i);
      long count = 0;
      for (int j = 0; j < usableAdapters; j++) {
        count += table.get(j + i + 1);
      }
      table.set(i, count);
    }
    return table;
  }

  // Returns number adapters that follow the adapter at the current index, which could be plugged
  // into the current adapter (i.e. their joltage differs by 3 or less)
  private static int countUsableAdapters(List<Integer> sortedAdapters, int index) {
    int cur = sortedAdapters.get(index);
    int possibilities = 0;
    while (index + possibilities + 1 < sortedAdapters.size()
        && sortedAdapters.get(index + possibilities + 1) - cur <= 3) {
      possibilities++;
    }
    return possibilities;
  }

  // Naive implementation - will not work for a large dataset
  // we require some dynamic programming!!
  private static long countPossibleArrangementsNaive(List<Integer> sortedAdapters, int index) {
    if (index == sortedAdapters.size() - 1) return 1; // When there's only one adapter left, there is only one possible arrangement

    int possibilities = countUsableAdapters(sortedAdapters, index);

    long count = 0;
    for (int i = 0; i < possibilities; i++) {
      count += countPossibleArrangementsNaive(sortedAdapters, index + i + 1);
    }
    return count;
  }
}
