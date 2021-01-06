package Day8;

class Machine {

  private int acc; // accumulator
  private int pc; // program counter

  public Machine(int firstInstr) {
    this.acc = 0;
    this.pc = firstInstr;
  }

  public void reset() {
    this.acc = 0;
    this.pc = 0;
  }

  // Execute provided instruction, updating machine state accordingly
  public int execute(Instruction instr) {
    int result = -1;
    switch (instr.getOp()) {
      case acc -> {
        this.acc += instr.getArg();
        this.pc++;
        result = this.pc;
      }
      case jmp -> {
        this.pc += instr.getArg();
        result = this.pc;
      }
      case nop -> {
        this.pc += 1;
        result = this.pc;
      }
    }
    return result;
  }

  public int getAcc() {
    return acc;
  }

  public int getPc() {
    return pc;
  }
}
