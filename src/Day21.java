import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.stream.Collectors;

public class Day21 {

  public static void main(String[] args) throws FileNotFoundException {
    BufferedReader reader = new BufferedReader(new FileReader("src/input/21.txt"));
    List<String> lines = reader.lines().collect(Collectors.toList());
    Map<String, Set<String>> allergenMap = buildAllergenMap(lines);
    List<String> allIngredients = getAllIngredients(lines);
    int nonAllergenicIngredients = countNonAllergenicIngredients(allergenMap, allIngredients);
    System.out.println("Ingredients that can't possibly contain any of the allergens appear " +
 nonAllergenicIngredients + " times");

    String dangerousIngredients = dangerousIngredients(allergenMap);
    System.out.println("My canonical dangerous ingredient list is " + dangerousIngredients);
  }

  // Part 1 - heavy lifting already done when creating the allergen map
  private static int countNonAllergenicIngredients(
      Map<String, Set<String>> allergenMap, List<String> allIngredients) {

    // Just count how many ingredients are left in the list after we remove any ingredients
    // that appear in the allergen map
    List<String> unusedIngredients = new ArrayList<>(allIngredients);
    for (String allergen : allergenMap.keySet()) {
      unusedIngredients.removeAll(allergenMap.get(allergen));
    }

    return unusedIngredients.size();
  }

  // Part 2 - note this destroys the allergenMap so don't try using it after
  private static String dangerousIngredients(Map<String, Set<String>> allergenMap) {
    Map<String, String> uniqueAllergenMap = new HashMap<>();

    // Find allergen which only maps to a single ingredient - add that pair to the unique allergen
    // map, then remove that ingredient from all other allergens and repeat.
    while (!allergenMap.isEmpty()) {
      for (String allergen : allergenMap.keySet()) {
        if (allergenMap.get(allergen).size() == 1) {
          String ingredient = allergenMap.get(allergen).iterator().next();
          uniqueAllergenMap.put(allergen, ingredient);
          for (String allergen2 : allergenMap.keySet()) {
            allergenMap.get(allergen2).remove(ingredient);
          }
          allergenMap.remove(allergen);
          break;
        }
      }
    }

    // Order, join and return the list.
    return uniqueAllergenMap.entrySet().stream()
        .sorted(Entry.comparingByKey())
        .map(Entry::getValue)
        .collect(Collectors.joining(","));
  }

  // Builds the allergen map - does the parsing and some logic!!
  private static Map<String, Set<String>> buildAllergenMap(List<String> rawInput) {
    Map<String, Set<String>> result = new HashMap<>();
    for (String s : rawInput) {
      int split = s.indexOf('(');
      Set<String> ingredients = new HashSet<>(Arrays.asList(s.substring(0, split).split(" ")));
      String[] allergens = s.substring(split + 10, s.length() - 1).split(", ");
      for (String allergen : allergens) {
        // If it's the first time we encounter this allergen, create a new entry in the map with
        // the allergen and ingredients. Otherwise, do a set union - The corresponding ingredient
        // for that allergen will appear in *all* entries for that allergen
        if (!result.containsKey(allergen)) {
          result.put(allergen, new HashSet<>(ingredients));
        } else {
          result.get(allergen).retainAll(ingredients);
        }
      }
    }
    return result;
  }

  // Just makes a list of all the ingredients - will appear more than once if they appear more
  // than once in the input
  private static List<String> getAllIngredients(List<String> rawInput) {
    List<String> result = new ArrayList<>();
    for (String s : rawInput) {
      int split = s.indexOf('(');
      result.addAll(Arrays.asList(s.substring(0, split).split(" ")));
    }
    return result;
  }
}
