import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class Day13 {

  // Simply holds the bus ID, along with it's position in the bus ID list..
  private static class IdOffset {
    private final int id;
    private final int offset;

    public IdOffset(int id, int offset) {
      this.id = id;
      this.offset = offset;
    }

    public int getId() {
      return id;
    }
  }

  public static void main(String[] args) throws IOException {
    BufferedReader reader = new BufferedReader(new FileReader("src/input/13.txt"));
    int earliest = Integer.parseInt(reader.readLine()); // Earliest you can be at the station
    List<IdOffset> offsetIds = makeIdOffsetList(reader.readLine().split(","));
    List<Integer> ids = offsetIds.stream().map(IdOffset::getId).collect(Collectors.toList());

    int part1 = earliestMultipliedByWaitTime(earliest, ids);
    System.out.println(
        "The ID of the earliest bus you can take to the airport multiplied by the number of minutes you'll need to wait for that bus = "
            + part1);

    long part2 = earliestOffsetTime(offsetIds);
    System.out.println(
        "The earliest timestamp such that all of the listed bus IDs depart at offsets matching their positions in the list = "
            + part2);
  }

  // Slightly modified version of the naive implementation
  // property p = target % offsetIds.get(curIndex).id == 0
  // If we know p holds for each id in range 0..i, then we know the next time it will hold
  // is when the coefficient of the firstId is incremented by id[1] * id[2] * ... * id[i]
  private static long earliestOffsetTime(List<IdOffset> offsetIds) {
    int curIndex = 1;
    long coeff = 1;
    int firstId = offsetIds.get(0).id;
    long factor = 1;
    while (curIndex != offsetIds.size()) {
      long target = firstId * coeff + offsetIds.get(curIndex).offset;
      if (target % offsetIds.get(curIndex).id == 0) {
        factor *= offsetIds.get(curIndex).id;
        curIndex++;
      } else {
        coeff += factor;
      }
    }
    return coeff * firstId;
  }

  // Does not work for large input - takes too long
  private static long earliestOffsetTimeNaive(List<IdOffset> offsetIds) {
    int curIndex = 1;
    int coeff = 1;
    int firstId = offsetIds.get(0).id;
    // Iterate through multiples of if first ID, until we find one such that
    // each ID after the first matches the property described in the spec
    while (curIndex != offsetIds.size()) {
      long target = firstId * coeff + offsetIds.get(curIndex).offset;
      if (target % offsetIds.get(curIndex).id == 0) {
        curIndex++;
      } else {
        curIndex = 1;
        coeff++;
      }
    }
    return coeff * firstId;
  }

  // Creates list of IdOffsets, such that each IdOffset contains an id, and the position of
  // that id in the original id list, discarding any 'x's
  private static List<IdOffset> makeIdOffsetList(String[] ids) {
    List<IdOffset> result = new ArrayList<>();
    for (int i = 0; i < ids.length; i++) {
      String cur = ids[i];
      if (cur.equals("x")) continue;
      result.add(new IdOffset(Integer.parseInt(cur), i));
    }
    return result;
  }

  private static int earliestMultipliedByWaitTime(int earliest, List<Integer> ids) {
    // Find the first departure after 'earliest' for each id
    List<Integer> firstDepartures =
        ids.stream().map(id -> firstMultipleOfIAfterN(earliest, id)).collect(Collectors.toList());
    // Find minimum time and id
    int earliestBusTime = Integer.MAX_VALUE;
    int earliestBusId = -1;
    for (int i = 0; i < ids.size(); i++) {
      if (firstDepartures.get(i) < earliestBusTime) {
        earliestBusTime = firstDepartures.get(i);
        earliestBusId = ids.get(i);
      }
    }
    return (earliestBusTime - earliest) * earliestBusId;
  }

  // Self-explanatory...
  private static int firstMultipleOfIAfterN(int n, int i) {
    return (n / i + 1) * i;
  }
}
