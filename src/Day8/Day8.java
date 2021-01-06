package Day8;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class Day8 {

  public static void main(String[] args) throws FileNotFoundException {
    BufferedReader reader = new BufferedReader(new FileReader("src/input/8.txt"));
    // Parse all lines into an Instruction object containing info about that instruction
    List<Instruction> instructions =
        reader.lines().map(Day8::parseInstruction).collect(Collectors.toList());

    // Create new machine with first instruction set to 0
    Machine machine = new Machine(0);
    // Executes all the instructions until either the end is reached, or a loop is found
    executesUntilEnd(machine, instructions);
    System.out.println(
        "The value of the accumulator when an infinite loop is found = " + machine.getAcc());
    machine.reset(); // reset program count and accumulator to 0

    // With any provided input data, this should be true
    if (foundCorruptInstruction(machine, instructions)) {
      System.out.println(
          "The value of the accumulator after the machine executes all instructions following the corrupted instruction fix = "
              + machine.getAcc());
    }
  }

  private static boolean foundCorruptInstruction(Machine machine, List<Instruction> instructions) {
    // Try flipping every nop to jmp and vice versa, then run the program. If it reaches the
    // end of execution, return true. The machine state will be that at the end of the successful
    // execution
    for (Instruction instr : instructions) {
      if (instr.getOp() == Instruction.InstructionType.jmp || instr.getOp() == Instruction.InstructionType.nop) {
        instr.setOp(instr.getOp().flip());
        if (executesUntilEnd(machine, instructions)) return true;
        machine.reset();
        instr.setOp(instr.getOp().flip());
      }
    }
    return false; // No corrupt instruction found
  }

  // Returns true if reached the end of execution, false if loop.
  private static boolean executesUntilEnd(Machine machine, List<Instruction> instructions) {
    Set<Integer> seen = new HashSet<>();

    // Execute all instructions until either end is reached, or a loop is detected
    int curInstr = machine.getPc();
    do {
      seen.add(curInstr);
      curInstr = machine.execute(instructions.get(curInstr));
    } while (!seen.contains(curInstr) && curInstr < instructions.size());
    return curInstr >= instructions.size(); // return true if we reached the end of the instructions
  }

  private static Instruction parseInstruction(String rawInstr) {
    String[] split = rawInstr.split(" ");
    return new Instruction(Instruction.InstructionType.valueOf(split[0]), Integer.parseInt(split[1]));
  }
}
