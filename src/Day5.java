import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class Day5 {

  public static int getSeatID(String pass) {
    // Extract different parts from the seat number
    String passRow = pass.substring(0, 7);
    String passColumn = pass.substring(7);

    // Convert each part into a binary number
    String rowBinary = passRow.replace('F', '0').replace('B', '1');
    String columnBinary = passColumn.replace('L', '0').replace('R', '1');

    // Get seat ID from binary representation using rule provided in specification
    return 8 * Integer.parseInt(rowBinary, 2) + Integer.parseInt(columnBinary, 2);
  }

    public static void main(String[] args) throws FileNotFoundException {
      BufferedReader reader = new BufferedReader(new FileReader("src/input/5.txt"));
      List<String> rawSeats = reader.lines().collect(Collectors.toList());

      // Get seat ID for each line using 'getSeatID()', then find the maximum
      int maxID = rawSeats.stream().map(Day5::getSeatID).max(Comparator.naturalOrder()).orElse(0);
      System.out.println("Max Seat ID = " + maxID);

      // Order all the seat IDs
      List<Integer> orderedIDs = rawSeats.stream().map(Day5::getSeatID).sorted().collect(Collectors.toList());

      // Iterate through sorted seat ID list, and find first missing seat
      int mySeat = -1;
      for (int i = 0; i < orderedIDs.size() - 1; i++) {
        if (orderedIDs.get(i + 1) == orderedIDs.get(i) + 2) {
          mySeat = orderedIDs.get(i) + 1;
        }
      }
    System.out.println("My Seat ID = " + mySeat);
    }

}
