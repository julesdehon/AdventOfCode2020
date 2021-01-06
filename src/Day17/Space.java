package Day17;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

// Class to represent our pocket space. Internally it is a map from coordinate to boolean
// true = active, false = inactive
class Space {

  Map<Coordinate, Boolean> cubes;
  boolean is4D;

  private Space(Map<Coordinate, Boolean> cubes, boolean is4D) {
    this.cubes = cubes;
    this.is4D = is4D;
  }

  public void simulateCycles(int n) {
    for (int i = 0; i < n; i++) {
      simulateCycle();
    }
  }

  public void simulateCycle() {
    Coordinate min = getMinCoord();
    Coordinate max = getMaxCoord();

    // Expand the space to consider cubes on the boundary of the current space
    makeSpace();
    // Make a copy of space so that we can perform all of the cube updates simultaneously
    Space copy = new Space(new HashMap<>(cubes), is4D);

    // Iterate through every coordinate currently in the map, count the number of active
    // neighbours, and update the cube state accordingly.
    for (int x = min.getX(); x <= max.getX(); x++) {
      for (int y = min.getY(); y <= max.getY(); y++) {
        for (int z = min.getZ(); z <= max.getZ(); z++) {
          for (int w = min.getW(); w <= max.getW(); w++) {
            Coordinate cur = new Coordinate(x, y, z, w);
            int activeNeighbours = copy.countActiveNeighbours(cur);
            if (cubes.get(cur)) {
              cubes.put(cur, activeNeighbours == 2 || activeNeighbours == 3);
            } else {
              cubes.put(cur, activeNeighbours == 3);
            }
          }
        }
      }
    }
  }

  // Simple enough - returns the number of active cubes in the space
  public int countActiveCubes() {
    return (int) cubes.entrySet().stream().filter(Entry::getValue).count();
  }

  // Simply parses the slice, creating a new 3D or 4D space depending on "is4D"
  public static Space createSpaceFromRawSlice(List<String> slice, boolean is4D) {
    Map<Coordinate, Boolean> result = new HashMap<>();
    for (int y = 0; y < slice.size(); y++) {
      for (int x = 0; x < slice.get(y).length(); x++) {
        result.put(new Coordinate(x, y, 0), slice.get(y).charAt(x) == '#');
      }
    }
    return new Space(result, is4D);
  }

  // Iterate through all neighbours - 26 if 3D space, or 80 if 4D space, and return the number
  // of those neighbours that are active
  private int countActiveNeighbours(Coordinate coord) {
    int count = 0;
    for (int x = coord.getX() - 1; x <= coord.getX() + 1; x++) {
      for (int y = coord.getY() - 1; y <= coord.getY() + 1; y++) {
        for (int z = coord.getZ() - 1; z <= coord.getZ() + 1; z++) {
          // If we're in 4D space, also consider w-dimension neighbours - if not, don't
          for (int w = coord.getW() - (is4D ? 1 : 0); w <= coord.getW() + (is4D ? 1 : 0); w++) {
            Coordinate cur = new Coordinate(x, y, z, w);
            if (!cur.equals(coord)) {
              count += cubes.get(cur) ? 1 : 0;
            }
          }
        }
      }
    }
    return count;
  }

  // Add a layer of inactive cubes at the boundary of the currently considered cubes.
  public void makeSpace() {
    Coordinate min = getMinCoord();
    Coordinate max = getMaxCoord();
    for (int x = min.getX() - 1; x <= max.getX() + 1; x++) {
      for (int y = min.getY() - 1; y <= max.getY() + 1; y++) {
        for (int z = min.getZ() - 1; z <= max.getZ() + 1; z++) {
          // If we're in 4D space, also add a w-dimension layer of cubes
          for (int w = min.getW() - (is4D ? 1 : 0); w <= max.getW() + (is4D ? 1 : 0); w++) {
            Coordinate cur = new Coordinate(x, y, z, w);
            if (!cubes.containsKey(cur)) {
              cubes.put(cur, false);
            }
          }
        }
      }
    }
  }

  // Get the minimum coordinate - useful when iterating through all coordinates in the map.
  private Coordinate getMinCoord() {
    Coordinate min = new Coordinate(Integer.MAX_VALUE, Integer.MAX_VALUE, Integer.MAX_VALUE);
    for (Coordinate coord : cubes.keySet()) {
      min = coord.smallerThan(min) ? coord : min;
    }
    return min;
  }

  // Get the maximum coordinate - useful when iterating through all coordinates in the map.
  private Coordinate getMaxCoord() {
    Coordinate max = new Coordinate(Integer.MIN_VALUE, Integer.MIN_VALUE, Integer.MIN_VALUE);
    for (Coordinate coord : cubes.keySet()) {
      max = !coord.smallerThan(max) ? coord : max;
    }
    return max;
  }
}
