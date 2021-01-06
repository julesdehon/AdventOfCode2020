package Day12;

// Self-explanatory... contains a char for command and int for argument.
class Instruction {

  private final char command;
  private final int argument;

  public Instruction(char command, int argument) {
    this.command = command;
    this.argument = argument;
  }

  public static Instruction parseInstruction(String rawInstr) {
    char command = rawInstr.charAt(0);
    int arg = Integer.parseInt(rawInstr.substring(1));
    return new Instruction(command, arg);
  }

  public char getCommand() {
    return command;
  }

  public int getArgument() {
    return argument;
  }
}
