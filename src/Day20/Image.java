package Day20;

import static Day20.ArrayUtils.flipArray;
import static Day20.ArrayUtils.rotateArray;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

// Image class - internally, a 2D Tile array
class Image {

  Tile[][] image;

  private Image(Tile[][] image) {
    this.image = image;
  }

  public List<Tile> getCorners() {
    return Arrays.asList(image[0][0], image[0][image.length - 1], image[image.length - 1][0],
        image[image.length - 1][image.length - 1]);
  }

  // This is where the meat is - creating the image
  public static Image createImageFromRawTiles(String[] rawTiles) {
    List<Tile> tiles = Arrays.stream(rawTiles).map(Tile::parseTile).collect(Collectors.toList());
    // Create and populate borderMap. Maps from each border -> a list of tiles it belongs to
    Map<Border, List<Tile>> borderMap = new HashMap<>();
    for (Tile tile : tiles) {
      for (Border border : tile.getBorders()) {
        if (!borderMap.containsKey(border)) {
          borderMap.put(border, new ArrayList<>());
        }
        borderMap.get(border).add(tile);
      }
    }
    // The corners are those tiles where two of its borders only belong to 1 tile.
    List<Tile> corners =
        tiles.stream()
            .filter(t -> isCornerTile(t, borderMap))
            .collect(Collectors.toList());
    // Get the two borders that only belong to 1 tile for an arbitrary corner tile
    Border[] topLeftCornerBorders = getCornerBorders(corners.get(0), borderMap);
    // Rotate the tile so that its corner borders are in the top and left positions
    corners.get(0).makeTopLeft(topLeftCornerBorders[0], topLeftCornerBorders[1]);
    int imageSize = (int) Math.sqrt(tiles.size());
    Tile[][] result = new Tile[imageSize][imageSize];
    result[0][0] = corners.get(0); // Set the top left tile of the image to this tile
    for (int i = 0; i < result.length; i++) {
      // For every row but the first, get the matching tile for the bottom border of the
      // tile at index 0 of the row above, then rotate/flip the tile (.match(toMa..)), and place
      // it at index 0 of the current row
      if (i != 0) {
        Border toMatch = result[i - 1][0].getBottomBorder(); // The bottom border of the tile just above
        List<Tile> possibleTiles = borderMap.get(toMatch); // Get the matching tile for that border
        Tile otherTile = possibleTiles.get(possibleTiles.get(0) == result[i - 1][0] ? 1 : 0);
        otherTile.match(toMatch, 'T'); // Rotate/flip tile to match
        result[i][0] = otherTile;
      }
      // For every other tile in that row, match it to the tile directly to the left of it,
      // using a similar process as above
      for (int j = 1; j < result[i].length; j++) {
        Border toMatch = result[i][j - 1].getRightBorder();
        List<Tile> possibleTiles = borderMap.get(toMatch);
        Tile otherTile = possibleTiles.get(possibleTiles.get(0) == result[i][j - 1] ? 1 : 0);
        otherTile.match(toMatch, 'L');
        result[i][j] = otherTile;
      }
    }
    return new Image(result);
  }

  // Count the number of times the sea monster (in a given orientation) is found in the image
  public int countSeaMonstersAux(String[] seaMonster) {
    // Make regex pattern out of each line of the seaMonster
    Pattern[] ps = new Pattern[seaMonster.length];
    for (int i = 0; i < seaMonster.length; i++) {
      ps[i] = Pattern.compile("(?=(" + seaMonster[i].replace(" ", ".") + ")).");
    }
    String[] stitchedImage = stitch(); // Lose the borders of the image
    int count = 0;
    for (int i = 0; i < stitchedImage.length - seaMonster.length + 1; i++) {
      Matcher firstMatcher = ps[0].matcher(stitchedImage[i]);
      while (firstMatcher.find()) { // Find occurrences of first line of seaMonster
        int begin = firstMatcher.start();
        boolean found = true;
        // For each subsequent line in the image, check if there is a match for each subsequent
        // line of the seaMonster, which begins at the same index as the top match.
        for (int j = 1; j < seaMonster.length; j++) {
          Matcher nextMatcher = ps[j].matcher(stitchedImage[i + j]);
          boolean foundOne = false;
          while (nextMatcher.find()) {
            if (nextMatcher.start() == begin) {
              foundOne = true;
              break;
            }
          }
          if (!foundOne) {
            found = false;
            break;
          }
        }
        if (found) {
          count++;
        }
      }
    }
    return count;
  }

  // Count the number of times the seaMonster (in any orientation) is found in the image
  // Simply rotate/flips the sea monster into all its possible orientations, and calls
  // countSeaMonstersAux on it.
  public int countSeaMonsters(String[] seaMonster) {
    // Loop for each rotation
    for (int i = 0; i < 4; i++) {
      int tried = countSeaMonstersAux(seaMonster);
      if (tried != 0) {
        return tried;
      }

      // flip it horizontally, and see if it occurs in the image
      seaMonster = flipArray(seaMonster, 'H');
      tried = countSeaMonstersAux(seaMonster);
      if (tried != 0) {
        return tried;
      }

      // flip it back, then flip it horizontally and check again
      seaMonster = flipArray(flipArray(seaMonster, 'H'), 'V');
      tried = countSeaMonstersAux(seaMonster);
      if (tried != 0) {
        return tried;
      }

      // flip it back, and then rotate it. It'll get checked again at the start of the next loop
      seaMonster = flipArray(seaMonster, 'V');
      seaMonster = rotateArray(seaMonster);
    }
    return -1; // If it never occurs, return -1 (this tells us there's been a problem somewhere)
  }

  // Lose the borders, and stitch all the tiles together to form a single String array
  public String[] stitch() {
    String[] result = new String[image.length * image[0][0].getImageSize()];
    Arrays.fill(result, "");
    for (int i = 0; i < image.length; i++) {
      for (int j = 0; j < image[i].length; j++) {
        for (int k = 0; k < image[i][j].getImageSize(); k++) {
          result[i * image[i][j].getImageSize() + k] += image[i][j].getImageSlice(k);
        }
      }
    }
    return result;
  }

  // Find the borders of the tile that are unique to that tile - these must be the corner
  // borders
  private static Border[] getCornerBorders(Tile tile, Map<Border, List<Tile>> borderMap) {
    Border[] result = new Border[2];
    for (Border border : tile.getBorders()) {
      if (borderMap.get(border).size() == 1) {
        result[result[0] == null ? 0 : 1] = border;
      }
    }
    return result;
  }

  // If there are two borders that are unique to that tile, then it is a corner tile.
  private static boolean isCornerTile(Tile tile, Map<Border, List<Tile>> borderMap) {
    int n = 0;
    for (Border border : tile.getBorders()) {
      if (borderMap.get(border).size() == 1) {
        n++;
      }
    }
    return n == 2;
  }
}
