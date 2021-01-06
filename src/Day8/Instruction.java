package Day8;

class Instruction {

  private InstructionType op;
  private final int arg;

  public Instruction(InstructionType op, int arg) {
    this.op = op;
    this.arg = arg;
  }

  public InstructionType getOp() {
    return op;
  }

  public int getArg() {
    return arg;
  }

  public void setOp(InstructionType op) {
    this.op = op;
  }

  public enum InstructionType {
    acc,
    jmp,
    nop;

    // Method to flip a jmp instruction to nop and vice versa, useful for part 2
    public InstructionType flip() {
      if (this == jmp) return nop;
      else if (this == nop) return jmp;
      else return this;
    }
  }
}
