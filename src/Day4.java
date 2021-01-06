import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class Day4 {

  // Task 1 Implementation
  static String[] requiredFields = {"byr", "iyr", "eyr", "hgt", "hcl", "ecl", "pid"};

  public static boolean validPassportPart1(String passport) {
    // Check passport contains each of the required fields
    boolean goodSoFar = true;
    for (String requiredField : requiredFields) {
      goodSoFar &= passport.contains(requiredField);
    }
    return goodSoFar;
  }

  // Task 2 Implementation
  public static boolean validPassportPart2(String passport) {
    if (!validPassportPart1(passport))
      return false;
    String[] fields = passport.split(" "); // split passport into its fields

    // Check each field is valid using rules provided
    for (String field : fields) {
      String[] kvp = field.split(":");
      if (!validField(kvp[0], kvp[1]))
        return false;
    }
    return true;
  }

  private static boolean validField(String key, String val) {
    // Check field is valid according to rules provided in specification
    switch (key) {
      case "byr" -> {
        int byr = Integer.parseInt(val);
        return 1920 <= byr && byr <= 2002;
      }

      case "iyr" -> {
        int iyr = Integer.parseInt(val);
        return 2010 <= iyr && iyr <= 2020;
      }

      case "eyr" -> {
        int eyr = Integer.parseInt(val);
        return 2020 <= eyr && eyr <= 2030;
      }

      case "hgt" -> {
          Pattern p = Pattern.compile("^([0-9]+)([a-z]{2})$");
          Matcher m = p.matcher(val);
          if (!m.find())
            return false;
          int height = Integer.parseInt(m.group(1));
          String unit = m.group(2);
          if (unit.equals("cm")) return 150 <= height && height <= 193;
          else if (unit.equals("in")) return 59 <= height && height <= 76;
          return false;
        }

      case "hcl" -> {
          Pattern p = Pattern.compile("^#[0-9a-f]{6}$");
          Matcher m = p.matcher(val);
          return m.find();
        }

      case "ecl" -> {
        List<String> possibilities = Arrays.asList("amb", "blu", "brn", "gry", "grn", "hzl", "oth");
        return possibilities.contains(val);
      }

      case "pid" -> {
        Pattern p = Pattern.compile("^[0-9]{9}$");
        Matcher m = p.matcher(val);
        return m.find();
      }

      case "cid" -> {
        return true;
      }

      default -> {
        return false;
      }
    }
  }

  public static void main(String[] args) throws IOException {
    BufferedReader reader = new BufferedReader(new FileReader("src/input/4.txt"));

    // parse all lines, concatenating lines that are not separated by an empty line.
    List<String> rawPassportData = Arrays.stream(
        reader.lines()
            .collect(Collectors.joining("\n"))
            .split("\n\n"))
        .map(s -> s.replace("\n", " "))
        .collect(Collectors.toList());

    // Count number of passports valid according to part 1
    System.out.println(
        "Task 1 valid passports = "
            + rawPassportData.stream().filter(Day4::validPassportPart1).count());

    // Count number of passports valid according to part 2
    System.out.println(
        "Task 2 valid passports = "
            + rawPassportData.stream().filter(Day4::validPassportPart2).count());
  }
}
