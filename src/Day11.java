import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class Day11 {

  public static void main(String[] args) throws FileNotFoundException {
    BufferedReader reader = new BufferedReader(new FileReader("src/input/11.txt"));

    List<String> seats = reader.lines().collect(Collectors.toList());
    int occupiedSeats = countOccupiedSeatsAfterStabilisation(seats, true);
    System.out.println("Using our initial model, once equilibrium is reached, " + occupiedSeats + " are occupied");

    int occupiedSeats2 = countOccupiedSeatsAfterStabilisation(seats, false);
    System.out.println("Using our refined model, once equilibrium is reached, " + occupiedSeats2 + " are occupied");
  }

  private static int countOccupiedSeatsAfterStabilisation(List<String> seats, boolean isPart1) {
    // Repeatedly simulate arrivals until we reach an equilibrium
    List<String> prev = seats;
    List<String> next = simulateArrival(prev, isPart1);
    while (!prev.equals(next)) {
      prev = next;
      next = simulateArrival(prev, isPart1);
    }

    // Once equilibrium reached, count the number of '#' in all strings using this *lovely* 1-liner
    return next.stream()
        .map(s -> s.chars().filter(c -> c == '#').count())
        .reduce(0l, Long::sum)
        .intValue();
  }

  // Simulates an arrival to the given seats, using different policies/models depending
  // on if we're in part 1 or 2
  private static List<String> simulateArrival(List<String> seats, boolean isPart1) {
    List<String> next = new ArrayList<>(); // Make a deep copy of seats

    // Iterate through every seat
    for (int y = 0; y < seats.size(); y++) {
      StringBuilder curLine = new StringBuilder();
      for (int x = 0; x < seats.get(0).length(); x++) {
        // Count occupied seats differently depending on part 1 or 2
        int occupiedSeats;
        if (isPart1) occupiedSeats = countOccupiedAdjacentSeats(seats, x, y);
        else occupiedSeats = countOccupiedSeatsInSight(seats, x, y);
        // Seat will become occupied or free depending on its current state, and the number
        // of occupied seats around it
        switch (seats.get(y).charAt(x)) {
          case 'L':
            curLine.append(occupiedSeats == 0 ? '#' : 'L');
            break;
          case '#':
            int maxOccupiedSeats = isPart1 ? 4 : 5;
            curLine.append(occupiedSeats >= maxOccupiedSeats ? 'L' : '#');
            break;
          default:
            curLine.append(seats.get(y).charAt(x));
            break;
        }
      }
      next.add(curLine.toString());
    }
    return next;
  }

  // Returns number of directions in which an arrivee would 'see' an occupied seat.
  private static int countOccupiedSeatsInSight(List<String> seats, int x, int y) {
    int count = 0;
    // Each (i, j) pair defines a direction (up, right, down, diagonally up/right etc.)
    for (int i = -1; i <= 1; i++) {
      for (int j = -1; j <= 1; j++) {
        if (i == 0 && j == 0) continue;
        int distance = 1;
        // Continue along the (i, j) direction until we reach a '#' or an 'L'. If it's a '#',
        // increment count
        while (isInBounds(seats, x + i * distance, y + j * distance)) {
          char c = seats.get(y + j * distance).charAt(x + i * distance);
          if (c == '#') {
            count++;
            break;
          } else if (c == 'L') {
            break;
          }
          distance++;
        }
      }
    }
    return count;
  }

  // Fairly self-explanatory - returns number of occupied seats immediately adjacent to the
  // seat at coordinates (x, y)
  private static int countOccupiedAdjacentSeats(List<String> seats, int x, int y) {
    int count = 0;
    for (int i = x - 1; i <= x + 1; i++) {
      for (int j = y - 1; j <= y + 1; j++) {
        if (i == x && j == y) continue;
        if (isInBounds(seats, i, j)) {
          count += seats.get(j).charAt(i) == '#' ? 1 : 0;
        }
      }
    }
    return count;
  }

  // The coordinates x and y are within the bounds of seats
  private static boolean isInBounds(List<String> seats, int x, int y) {
    return 0 <= x && x < seats.get(0).length() && 0 <= y && y < seats.size();
  }
}
