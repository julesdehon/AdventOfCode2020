import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

// This one seemed a little too easy...
// Could maybe have tried to speed it up, but still only takes a few
// seconds to run... could be worse!

public class Day15 {

  public static void main(String[] args) throws IOException {
    BufferedReader reader = new BufferedReader(new FileReader("src/input/15.txt"));
    List<Integer> startingNumbers =
        Arrays.stream(reader.readLine().split(","))
            .map(Integer::parseInt)
            .collect(Collectors.toList());

    int part1 = nthNumberSpoken(startingNumbers, 2020);
    System.out.println("The 2020th number spoken was " + part1);

    int part2 = nthNumberSpoken(startingNumbers, 30000000);
    System.out.println("The 30000000th number spoken was " + part2);
  }

  private static int nthNumberSpoken(List<Integer> startingNumbers, int n) {
    // Map each number that has been spoken to the last turn it was spoken on
    Map<Integer, Integer> lastTimeSpoken = new HashMap<>();
    int lastSpoken = -1;
    for (int i = 0; i < n; i++) {
      // For the starting numbers - just populate the map.
      if (i < startingNumbers.size()) {
        if (i > 0) {
          lastTimeSpoken.put(lastSpoken, i);
        }
        lastSpoken = startingNumbers.get(i);
        continue;
      }

      if (lastTimeSpoken.containsKey(lastSpoken)) {
        // If the last number had been spoken before, say the difference between the last turn
        // and the one before
        int temp = i - lastTimeSpoken.get(lastSpoken);
        lastTimeSpoken.put(lastSpoken, i); // Add the last spoken word to the map
        lastSpoken = temp;
      } else {
        // Otherwise say 0, and add the last spoken word to map
        lastTimeSpoken.put(lastSpoken, i);
        lastSpoken = 0;
      }

    }
    return lastSpoken;
  }
}
