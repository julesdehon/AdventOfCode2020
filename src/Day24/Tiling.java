package Day24;

import java.util.HashSet;
import java.util.Set;

/*

 Represents a hexagonal grid with the following coordinate system:

         / \     / \     / \
       /     \ /     \ /     \
      | -1,1  |  0,1  |  1,1  |
      |       |       |       |
     / \     / \     / \     /
   /     \ /     \ /     \ /
  | -1,0  |  0,0  |  1,0  |
  |       |       |       |
   \     / \     / \     / \
     \ /     \ /     \ /     \
      | -1,-1 |  0,-1 |  1,-1 |
      |       |       |       |
       \     / \     / \     /
         \ /     \ /     \ /

 */

class Tiling {

  Set<TileCoord> blacks; // In the set = black, not in the set = white

  public Tiling(Set<TileCoord> blacks) {
    this.blacks = blacks;
  }

  public Tiling() {
    this(new HashSet<>());
  }

  public void flip(TileCoord coord) {
    if (blacks.contains(coord)) {
      blacks.remove(coord);
    } else {
      blacks.add(coord);
    }
  }

  public void nextDay() {
    Tiling copy = new Tiling(new HashSet<>(blacks));
    for (TileCoord black : copy.blacks) {
      int adjacent = copy.countAdjacentBlacks(black);
      if (adjacent == 0 || adjacent > 2) {
        flip(black);
      }
      // We only need to check the white tiles that are adjacent to at least one black tile.
      // (Otherwise there would be infinite white tiles to check)
      for (TileCoord white : copy.getAdjacentWhites(black)) {
        if (copy.countAdjacentBlacks(white) == 2) {
          blacks.add(white);
        }
      }
    }
  }

  private int countAdjacentBlacks(TileCoord coord) {
    int count = 0;
    for (int y = coord.getY() - 1; y <= coord.getY() + 1; y++) {
      for (int x = coord.getX() - 1; x <= coord.getX() + 1; x++) {
        // The coordinates of the surrounding tiles will depend on whether we have an even or odd
        // why coordinate. Have a look at the diagram to see why
        int side = coord.getY() % 2 == 0 ? 1 : -1;
        if ((x == coord.getX() && y == coord.getY()) || (x == coord.getX() + side && (y != coord.getY()))) {
          continue;
        }
        count += blacks.contains(new TileCoord(x, y)) ? 1 : 0;
      }
    }
    return count;
  }

  private Set<TileCoord> getAdjacentWhites(TileCoord coord) {
    Set<TileCoord> result = new HashSet<>();
    // Very similar for loop structure of the above countAdjacentBlacks method
    for (int y = coord.getY() - 1; y <= coord.getY() + 1; y++) {
      for (int x = coord.getX() - 1; x <= coord.getX() + 1; x++) {
        int side = coord.getY() % 2 == 0 ? 1 : -1;
        if ((x == coord.getX() && y == coord.getY()) || (x == coord.getX() + side && (y != coord.getY()))) {
          continue;
        }
        TileCoord cur = new TileCoord(x, y);
        if (!blacks.contains(cur)) {
          result.add(cur);
        }
      }
    }
    return result;
  }

  public int countBlackTiles() {
    return blacks.size();
  }
}
