package Day24;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.List;
import java.util.stream.Collectors;

public class Day24 {

  public static void main(String[] args) throws FileNotFoundException {
    BufferedReader reader = new BufferedReader(new FileReader("src/input/24.txt"));
    List<TileCoord> toFlip = reader.lines().map(TileCoord::parsePath).collect(Collectors.toList());
    Tiling floor = new Tiling();
    for (TileCoord coord : toFlip) {
      floor.flip(coord);
    }
    System.out.println("After the renovations there are " + floor.countBlackTiles() + " tiles black side up");
    for (int i = 0; i < 100; i++) {
      floor.nextDay();
    }
    System.out.println("After 100 days there are " + floor.countBlackTiles() + " black tiles");
  }

}
