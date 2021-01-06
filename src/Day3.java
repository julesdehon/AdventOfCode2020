import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.List;
import java.util.stream.Collectors;

public class Day3 {

  private static class Slope {
    private final int right;
    private final int down;

    public Slope(int right, int down) {
      this.right = right;
      this.down = down;
    }
  }

  public static long treesEncountered(List<String> map, Slope slope) {
    int columns = map.get(0).length();
    // start in top left corner, having encountered 0 trees
    int x = 0;
    int y = 0;
    int trees = 0;

    while (y < map.size()) {
      // if the current square is a '#', add 1 to trees encountered
      trees += map.get(y).charAt(x % columns) == '#' ? 1 : 0;

      // advance according to given slope
      x += slope.right;
      y += slope.down;
    }
    return trees;
  }

  public static void main(String[] args) throws FileNotFoundException {
    BufferedReader reader = new BufferedReader(new FileReader("src/input/3.txt"));
    List<String> map = reader.lines().collect(Collectors.toList());

    System.out.println(
        "Trees Encountered using right: 3, down: 1 strategy = " + treesEncountered(map, new Slope(3, 1)));

    List<Slope> inputs = List.of(
        new Slope(1, 1),
        new Slope(3, 1),
        new Slope(5, 1),
        new Slope(7, 1),
        new Slope(1, 2));

    // Find number of trees encountered for each slope in 'inputs', then multiply them all together
    long part2Ans = inputs.stream().map(s -> treesEncountered(map, s)).reduce(1L, (i, j) -> i * j);

    System.out.println("Part 2 answer = " + part2Ans);
  }
}
