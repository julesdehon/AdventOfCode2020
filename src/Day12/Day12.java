package Day12;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.List;
import java.util.stream.Collectors;

public class Day12 {

  public static void main(String[] args) throws FileNotFoundException {
    BufferedReader reader = new BufferedReader(new FileReader("src/input/12.txt"));
    List<Instruction> instructions =
        reader.lines().map(Instruction::parseInstruction).collect(Collectors.toList());
    Ship ship1 = new Ship();
    System.out.println(
        "Final manhattan distance using initial interpretation of instructions = "
            + finalManhattanDistance(ship1, instructions, true));

    Ship ship2 = new Ship();
    System.out.println(
        "Final manhattan distance using revised interpretation of instructions = "
            + finalManhattanDistance(ship2, instructions, false));
  }

  // Execute all instructions, then return manhattan distance of ship.
  private static int finalManhattanDistance(
      Ship ship, List<Instruction> instructions, boolean part1) {
    for (Instruction instr : instructions) {
      ship.execute(instr, part1);
    }
    return Math.abs(ship.getX()) + Math.abs(ship.getY());
  }
}
