package Day14;

class Mask {

  private final long ones; // Contains a 1 at every bit position the mask has a 1
  private final long zeros; // Contains a 1 at every bit position the mask has a 0
  private final long floats; // Contains a 1 at every bit position the mask has an X

  public Mask(String mask) {
    this.ones = Long.parseLong(mask.replace("X", "0"), 2);
    this.zeros = Long.parseLong(mask.replace("X", "1"), 2);
    this.floats = Long.parseLong(mask.replace("1", "0").replace("X", "1"), 2);
  }

  public long getOnes() {
    return ones;
  }

  public long getFloats() {
    return floats;
  }

  // Used in part 1 only - applies the part1 version of the mask to a value
  public long applyTo(long n) {
    return (n | ones) & zeros;
  }
}
