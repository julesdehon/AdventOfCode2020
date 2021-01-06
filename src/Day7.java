import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

public class Day7 {

  public static void main(String[] args) throws FileNotFoundException {
    BufferedReader reader = new BufferedReader(new FileReader("src/input/7.txt"));

    // Create Map from Bag Colour -> (Map Bag Colour -> Num Bags)
    Map<String, Map<String, Integer>> rules =
        reader
            .lines()
            .map(Day7::parseRule)
            .collect(Collectors.toMap(Rule::getContainer, Rule::getContents));

    String query = "shiny gold";

    // Get all bags that contain the queried bag colour
    Set<String> bagsItsIn = getContainersOf(query, rules);
    System.out.println("The " + query + " bag is contained in " + bagsItsIn.size() + " other bags.");

    // Get number of bags that the queried bag colour contains
    int numBagsInside = countBagsInsideOf(query, rules);
    System.out.println("The " + query + " bag has " + numBagsInside + " bags inside it.");
  }

  private static int countBagsInsideOf(String colour, Map<String, Map<String, Integer>> rules) {
    int count = 0;
    Map<String, Integer> contents = rules.get(colour);
    if (contents == null) return 0;
    // Iterate through the bags inside the queried bag, and add to count the number bags of that
    // colour there are inside, + (that number *  the number of bags inside this 'inside' bag)
    for (String bag : contents.keySet()) {
      int numBags = contents.get(bag);
      count += numBags + numBags * countBagsInsideOf(bag, rules);
    }
    return count;
  }

  private static Set<String> getContainersOf(String colour, Map<String, Map<String, Integer>> rules) {
    Set<String> containers = new HashSet<>();
    // Iterate through all known bags, if the current bag contains the queried bag, add it to
    // the set of containers, and then add all bags that contain that bag to the set of containers
    for (String bag : rules.keySet()) {
      if (rules.get(bag).containsKey(colour)) {
        containers.add(bag);
        containers.addAll(getContainersOf(bag, rules));
      }
    }
    return containers;
  }

  // Return a rule object structuring the info found in rawRule
  private static Rule parseRule(String rawRule) {
    String[] container = rawRule.split(" contain ");
    String[] contents = container[1].split(", |\\.");
    Rule rule = new Rule(container[0].replace(" bags", ""));
    for (String numContent : contents) {
      if (numContent.equals("no other bags")) break;
      int num = numContent.charAt(0) - '0';
      int end = numContent.indexOf(" bag");
      String content = numContent.substring(2, end);
      rule.contents.put(content, num);
    }
    return rule;
  }

  private static class Rule {
    private final String container;
    private final Map<String, Integer> contents;

    public Rule(String container) {
      this.container = container;
      this.contents = new HashMap<>();
    }

    public String getContainer() {
      return container;
    }

    public Map<String, Integer> getContents() {
      return contents;
    }
  }
}
