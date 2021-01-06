package Day24;

import java.util.Objects;

class TileCoord {

  private int x;
  private int y;

  public TileCoord(int x, int y) {
    this.x = x;
    this.y = y;
  }

  public int getX() {
    return x;
  }

  public int getY() {
    return y;
  }

  // Follow the path given by the string, updating x and y coordinates accordingly
  public static TileCoord parsePath(String path) {
    int x = 0;
    int y = 0;
    int i = 0;
    while (i < path.length()) {
      char ch = path.charAt(i);
      switch (ch) {
        case 'e' -> x++;
        case 'w' -> x--;
        case 'n', 's' -> {
          i++;
          // x coordinate change will depend on whether we are at an even or odd y coordinate
          // Look at ascii diagram below to understand why
          if (path.charAt(i) == 'e') {
            x += (y % 2 == 0) ? 0 : 1;
          } else {
            x -= (y % 2 == 0) ? 1 : 0;
          }
          y += ch == 'n' ? 1 : -1; // increment or decrement y depending on if ch is 'n' or 's'
        }
      }
      i++;
    }
    return new TileCoord(x, y);
  }

  // override equals and hashcode so we can keep TileCoord in the Tiling HashSet
  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    TileCoord tileCoord = (TileCoord) o;
    return x == tileCoord.x &&
        y == tileCoord.y;
  }

  @Override
  public int hashCode() {
    return Objects.hash(x, y);
  }
}
