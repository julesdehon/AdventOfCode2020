package Day20;

import static Day20.ArrayUtils.flipArray;
import static Day20.ArrayUtils.rotateArray;
import static Day20.ArrayUtils.verticalSlice;

import java.util.Arrays;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

// Class to represent a tile of the image
class Tile {

  private final int id;
  private final Border[] borders; // 0->top, 1->right, 2->bottom, 3->left
  private String[] image;

  public Tile(int id, Border[] borders, String[] image) {
    this.id = id;
    this.borders = borders;
    this.image = image;
  }

  // Convert raw tile to tile object
  public static Tile parseTile(String input) {
    String[] splitInput = input.split("\n");
    Pattern p = Pattern.compile("Tile (\\d+):");
    Matcher m = p.matcher(splitInput[0]);
    m.find();
    String[] tile = Arrays.copyOfRange(splitInput, 1, splitInput.length);
    return new Tile(Integer.parseInt(m.group(1)), extractBorder(tile), extractImage(tile));
  }

  public int getId() {
    return id;
  }

  public Set<Border> getBorders() {
    return Arrays.stream(borders).collect(Collectors.toSet());
  }

  public Border getRightBorder() {
    return borders[1];
  }

  public Border getBottomBorder() {
    return borders[2];
  }

  public int getImageSize() {
    return image.length;
  }

  public String getImageSlice(int i) {
    return image[i];
  }

  // The tile must have borders b1 and b2, and they must be adjacent. Rotates the tile
  // so that b1 and b2 are the top and left borders (not necessarily in that order)
  public void makeTopLeft(Border b1, Border b2) {
    for (int i = 0; i < borders.length; i++) {
      if (borders[0] == b1 && borders[3] == b2 || borders[0] == b2 && borders[3] == b1) {
        break;
      }
      rotateRight();
    }
  }

  // Rotates the tile until either the top or left border (depending on 'side') matches border
  public void match(Border border, char side) {
    while (!borders[side == 'L' ? 3 : 0].equals(border)) {
      rotateRight();
    }
    if (!borders[side == 'L' ? 3 : 0].getNormalBorder().equals(border.getNormalBorder())) {
      flip((side == 'L') ? 'V' : 'H');
    }
  }

  // Rotates the tile right
  private void rotateRight() {
    // Handle borders
    Border temp = borders[3];
    System.arraycopy(borders, 0, borders, 1, 3);
    borders[0] = temp;
    borders[2].reverse();
    borders[0].reverse();
    // Handle image
    image = rotateArray(image);
  }

  // Flips the tile in the given plane
  private void flip(char plane) {
    // Handle borders
    switch (plane) {
      case 'V' -> {
        Border temp = borders[0];
        borders[0] = borders[2];
        borders[2] = temp;
        borders[1].reverse();
        borders[3].reverse();
      }
      case 'H' -> {
        Border temp = borders[1];
        borders[1] = borders[3];
        borders[3] = temp;
        borders[0].reverse();
        borders[2].reverse();
      }
    }
    // Handle image
    image = flipArray(image, plane);
  }

  // Used for parsing - gets all the outer edges/borders of the raw tile array and returns
  // them in a length 4 Border array
  private static Border[] extractBorder(String[] tile) {
    Border[] result = new Border[4];
    result[0] = new Border(tile[0]); // Top
    result[1] = new Border(verticalSlice(tile, tile[0].length() - 1)); // Right
    result[2] = new Border((tile[tile.length - 1])); // Bottom
    result[3] = new Border(verticalSlice(tile, 0)); // Left
    return result;
  }

  // Also used for parsing - returns the contents of the raw tile array without the outer
  // edges/borders (which are extracted by extractBorder)
  private static String[] extractImage(String[] tile) {
    String[] result = new String[tile.length - 2];
    for (int i = 1; i < tile.length - 1; i++) {
      result[i - 1] = tile[i].substring(1, tile[i].length() - 1);
    }
    return result;
  }
}
