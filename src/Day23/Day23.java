package Day23;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class Day23 {

  public static void main(String[] args) {
    String input = "193467258";

    // Create cup list
    List<Integer> cupList = input.chars().map(ch -> ch - '0').boxed().collect(Collectors.toList());

    Cups cups = new Cups(cupList);
    cups.move(100);
    System.out.println("The labels on the cups after cup 1 are " + cups);

    // Create big cup list for part 2
    List<Integer> bigCupList = new ArrayList<>(cupList);
    for (int i = 10; i <= 1_000_000; i++) {
      bigCupList.add(i);
    }

    Cups bigCups = new Cups(bigCupList);
    bigCups.move(10_000_000);
    int first = bigCups.getCupAfter(1);
    int second = bigCups.getCupAfter(first);
    System.out.println(
        "The product of the two cup labels immediately clockwise of cup 1 is "
            + (long) first * (long) second);
  }
}
