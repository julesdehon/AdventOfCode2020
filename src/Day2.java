import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class Day2 {

  enum Policy {
    SLED_RENTAL_PLACE_DOWN_THE_STREET,
    NORTH_POLE_TOBOGGAN_RENTAL_SHOP
  }

  // 'char' occurs in 'password' between 'min' and 'max' times.
  public static boolean isValidPolicy1(int min, int max, char match, String password) {
    long count = password.chars().filter(ch -> ch == match).count();
    return min <= count && count <= max;
  }

  // 'char' occurs in 'password' either at 'pos1' xor at 'pos2'
  public static boolean isValidPolicy2(int pos1, int pos2, char match, String password) {
    return password.charAt(pos1 - 1) == match ^ password.charAt(pos2 - 1) == match;
  }

  public static boolean parseInput(String input, Policy policy) {
    // Do some regex to get the right values for the input
    Pattern p = Pattern.compile("([0-9]+)-([0-9]+) ([a-z]): ([a-z]*)");
    Matcher m = p.matcher(input);
    m.find();

    int min = Integer.parseInt(m.group(1));
    int max = Integer.parseInt(m.group(2));
    char match = m.group(3).charAt(0);
    String password = m.group(4);

    // Check the input holds according to the given policy
    if (policy == Policy.SLED_RENTAL_PLACE_DOWN_THE_STREET) {
      return isValidPolicy1(min, max, match, password);
    }
    else {
      return isValidPolicy2(min, max, match, password);
    }
  }

  public static void main(String[] args) throws FileNotFoundException {
    BufferedReader reader = new BufferedReader(new FileReader("src/input/2.txt"));
    List<String> lines = reader.lines().collect(Collectors.toList());
    System.out.println("Using \"Down the road policy\": " +
        lines
            .stream()
            .filter(s -> parseInput(s, Policy.SLED_RENTAL_PLACE_DOWN_THE_STREET)) // reduce passwords to those where the given policy holds
            .count()); // count number of passwords where the given policy holds
    System.out.println("Using \"Official Toboggan Corporate Policy\": " +
        lines
            .stream()
            .filter(s -> parseInput(s, Policy.NORTH_POLE_TOBOGGAN_RENTAL_SHOP))
            .count());
  }
}
