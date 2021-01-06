package Day16;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.stream.Collectors;

public class Day16 {

  public static void main(String[] args) throws IOException {
    BufferedReader reader = new BufferedReader(new FileReader("src/input/16.txt"));

    /* Parsing input */

    List<Field> fields = new ArrayList<>();
    List<Integer> myTicket;
    List<List<Integer>> nearbyTickets = new ArrayList<>();

    // Populate field list
    String curLine;
    while (!(curLine = reader.readLine()).isEmpty()) {
      fields.add(Field.parseField(curLine));
    }
    reader.readLine(); // skip "your ticket:"
    myTicket = parseTicket(reader.readLine()); // parse my ticket

    reader.readLine(); // skip newline
    reader.readLine(); // skip "nearby tickets:"

    // Populate nearby tickets list
    while ((curLine = reader.readLine()) != null) {
      nearbyTickets.add(parseTicket(curLine));
    }

    /* logic */

    List<List<Integer>> validTickets = new ArrayList<>();
    int part1 = calcTicketScanningErrorRate(fields, nearbyTickets, validTickets);
    System.out.println("Ticket scanning error rate = " + part1);

    long part2 = multiplyDepartureFields(fields, myTicket, validTickets);
    System.out.println("What you get when you multiply the six departure values together = " + part2);
  }

  /* Part 1 */

  // Find every value that is valid for no fields, and return the sum of those values
  // Also populates the validTickets list with all tickets that do not hold any of the above values
  private static int calcTicketScanningErrorRate(
      List<Field> fields, List<List<Integer>> nearbyTickets, List<List<Integer>> validTickets) {
    int sum = 0;
    // Iterate through each ticket
    for (List<Integer> ticket : nearbyTickets) {
      boolean allValid = true;
      // Iterate through each value in the ticket, checking that it is valid for at least one field
      for (Integer value : ticket) {
        boolean foundValid = false;
        for (Field field : fields) {
          if (field.valid(value)) {
            foundValid = true;
            break;
          }
        }
        // If the value is valid for no fields, add it to the sum
        if (!foundValid) {
          sum += value;
          allValid = false;
        }
      }
      if (allValid) validTickets.add(ticket);
    }
    return sum;
  }

  /* Part 2 */

  // Work out which index in the ticket value list corresponds to which field, and find the product
  // of the six tickets values for fields that contain the word "departure"
  private static long multiplyDepartureFields(
      List<Field> fields, List<Integer> myTicket, List<List<Integer>> nearbyTickets) {
    Map<Field, List<Integer>> fieldMap = new HashMap<>();

    nearbyTickets.add(myTicket);
    // Iterate through all fields - for each index into the ticket value list, check if the value
    // for each ticket in the ticket list at that index is valid. If yes, add that index as a
    // possible index for the current field in the fieldMap
    for (Field field : fields) {
      for (int i = 0; i < nearbyTickets.get(0).size(); i++) {
        boolean allValid = true;
        for (List<Integer> ticket : nearbyTickets) {
          if (!field.valid(ticket.get(i))) {
            allValid = false;
            break;
          }
        }
        if (allValid) {
          if (!fieldMap.containsKey(field)) fieldMap.put(field, new ArrayList<>());
          fieldMap.get(field).add(i);
        }
      }
    }

    Map<Field, Integer> uniqueFieldMap = getUniqueFieldMap(fieldMap);

    return uniqueFieldMap.entrySet().stream()
        .filter(e -> e.getKey().name.contains("departure"))
        .map(Entry::getValue)
        .map(myTicket::get)
        .map(Long::valueOf)
        .reduce(1L, (x, y) -> x * y);
  }

  // Reduce the list of possible indices for each field in the fieldMap to a single index, such
  // that it is unique to that field. Not a perfect solution, but seems to work with the input
  // they give - they made it quite easy
  private static Map<Field, Integer> getUniqueFieldMap(Map<Field, List<Integer>> fieldMap) {
    Map<Field, Integer> result = new HashMap<>();
    // Iterate through the fields, in order of increasing size of their list of possible indices
    // (so that the field which only has one possible index is seen first), and then iterate through
    // the list of possible indices for that field, and add the first possible index that is not yet
    // in the unique field map.
    for (int i = 1; i <= fieldMap.size(); i++) {
      for (Entry<Field, List<Integer>> fieldEntry : fieldMap.entrySet()) {
        if (fieldEntry.getValue().size() == i) {
          for (int possibleIndex : fieldEntry.getValue()) {
            if (!result.containsValue(possibleIndex)) {
              result.put(fieldEntry.getKey(), possibleIndex);
            }
          }
        }
      }
    }
    return result;
  }

  /* Utility function */
  // Simply converts a string of comma-separated ints into a List<Integer> representing the ticket
  private static List<Integer> parseTicket(String rawTicket) {
    return Arrays.stream(rawTicket.split(",")).map(Integer::parseInt).collect(Collectors.toList());
  }
}
