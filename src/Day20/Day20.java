package Day20;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.Arrays;
import java.util.stream.Collectors;

public class Day20 {

  public static void main(String[] args) throws FileNotFoundException {
    BufferedReader reader = new BufferedReader(new FileReader("src/input/20.txt"));
    // Parsing
    String raw = reader.lines().collect(Collectors.joining("\n"));
    String[] tiles = raw.split("\n\n");

    Image image = Image.createImageFromRawTiles(tiles); // Create the image from the tiles
    // Multiply together the ids of all the corner tiles
    long part1 = image.getCorners().stream().map(t -> (long) t.getId()).reduce(1L, (x, y) -> x * y);
    System.out.println("If you multiply together the IDs of the four corner tiles, you get " + part1);
    String[] seaMonster = {"                  # ",
                           "#    ##    ##    ###",
                           " #  #  #  #  #  #   "};
    int monsterCount = image.countSeaMonsters(seaMonster);
    // Water roughness is the number of # in the image that are not a sea monster
    long part2 = countChars(image.stitch(), '#') - monsterCount * countChars(seaMonster, '#');
    System.out.println("The number of # which are not part of a sea monster = " + part2);
  }

  private static long countChars(String[] array, char toCount) {
    return Arrays.stream(array)
        .map(s -> s.chars().filter(ch -> ch == toCount).count())
        .reduce(0L, Long::sum);
  }

}
