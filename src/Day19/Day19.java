package Day19;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.List;
import java.util.stream.Collectors;

public class Day19 {

  public static void main(String[] args) throws FileNotFoundException {
    BufferedReader reader = new BufferedReader(new FileReader("src/input/19.txt"));

    // Take all lines until the first empty line
    List<String> rawRules =
        reader.lines().takeWhile(s -> !s.isEmpty()).collect(Collectors.toList());
    RuleSet rules = RuleSet.parseRules(rawRules);
    // Take the rest of the lines
    List<String> messages = reader.lines().collect(Collectors.toList());

    // Count all the messages that match rule 0
    long part1 = messages.stream().filter(m -> rules.matches(List.of(0), m)).count();
    System.out.println("Messages that match rule 0 without looped rules = " + part1);

    // update rule 8 and 11
    rules.setRule("8: 42 | 42 8");
    rules.setRule("11: 42 31 | 42 11 31");

    // Count all the messages that match rule 0
    long part2 = messages.stream().filter(m -> rules.matches(List.of(0), m)).count();
    System.out.println("Messages that match rule 0 with looped rules = " + part2);
  }
}
