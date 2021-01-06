package Day14;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.List;
import java.util.stream.Collectors;

public class Day14 {

  public static void main(String[] args) throws FileNotFoundException {
    BufferedReader reader = new BufferedReader(new FileReader("src/input/14.txt"));
    List<String> input = reader.lines().collect(Collectors.toList());
    long part1 = sumOfMemoryValuesAfterInstructions(input, true);
    System.out.println(
        "The sum of all values left in memory after the initialisation program completes on the version 1 decoder\n= "
            + part1);

    long part2 = sumOfMemoryValuesAfterInstructions(input, false);
    System.out.println(
        "The sum of all values left in memory after the initialisation program completes on the version 2 decoder\n= "
            + part2);
  }

  private static long sumOfMemoryValuesAfterInstructions(List<String> input, boolean part1) {
    // Decode the instructions, then sum all values in memory, and return that sum.
    Decoder decoder = new Decoder(part1);
    decoder.decode(input);
    return decoder.getMemoryValues().stream().reduce(0L, Long::sum);
  }
}
