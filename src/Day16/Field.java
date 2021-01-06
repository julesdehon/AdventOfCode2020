package Day16;

import java.util.ArrayList;
import java.util.List;

// Simple class to represent a field - has a name and a number of ranges. Can query to see if
// a value is valid for this field - this just checks if it lies in any of its ranges.
class Field {

  String name;
  List<Range> ranges;

  public Field(String name, List<Range> ranges) {
    this.name = name;
    this.ranges = ranges;
  }

  public boolean valid(int n) {
    for (Range range : ranges) {
      if (range.liesWithin(n)) {
        return true;
      }
    }
    return false;
  }

  public static Field parseField(String rawField) {
    String[] name = rawField.split(": ");
    String[] rawRanges = name[1].split(" or ");
    List<Range> ranges = new ArrayList<>();
    for (String rawRange : rawRanges) {
      String[] minMax = rawRange.split("-");
      ranges.add(new Range(Integer.parseInt(minMax[0]), Integer.parseInt(minMax[1])));
    }
    return new Field(name[0], ranges);
  }

  // Simple class to hold a range, and see whether a value lies within that range
  public static class Range {
    int minimum;
    int maximum;

    public Range(int minimum, int maximum) {
      this.minimum = minimum;
      this.maximum = maximum;
    }

    public boolean liesWithin(int n) {
      return minimum <= n && n <= maximum;
    }
  }
}
