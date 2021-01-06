package Day17;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.List;
import java.util.stream.Collectors;

public class Day17 {

  public static void main(String[] args) throws FileNotFoundException {
    BufferedReader reader = new BufferedReader(new FileReader("src/input/17.txt"));
    List<String> slice = reader.lines().collect(Collectors.toList());

    Space space3D = Space.createSpaceFromRawSlice(slice, false);
    space3D.makeSpace();
    space3D.simulateCycles(6);
    int activeCubes3D = space3D.countActiveCubes();
    System.out.println("In a 3D space model, the number of cubes left in the active state after the sixth cycle = " + activeCubes3D);

    Space space4D = Space.createSpaceFromRawSlice(slice, true);
    space4D.makeSpace();
    space4D.simulateCycles(6);
    int activeCubes4D = space4D.countActiveCubes();
    System.out.println("In a 4D space model, the number of cubes left in the active state after the sixth cycle = " + activeCubes4D);  }
}
