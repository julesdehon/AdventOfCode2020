package Day12;

class Ship {

  private int x; // Ship's x coord
  private int y; // Ship's y coord
  private Direction dir = Direction.E; // Ship's current direction (used in part 1)
  private int wayPointX = 10; // Waypoint's x coord (used in part 2)
  private int wayPointY = 1; // Waypoint's y coord (used in part 2)

  public int getX() {
    return x;
  }

  public int getY() {
    return y;
  }

  public void execute(Instruction instr, boolean part1) {
    if (!part1) {
      executeWaypointInstr(instr);
      return;
    }
    switch (instr.getCommand()) {
      case 'F' -> advance(instr.getArgument());
      case 'R' -> rotr(instr.getArgument());
      case 'L' -> rotl(instr.getArgument());
      default -> advance(Direction.valueOf(String.valueOf(instr.getCommand())),
          instr.getArgument());
    }
  }

  public void executeWaypointInstr(Instruction instr) {
    switch (instr.getCommand()) {
      case 'N' -> wayPointY += instr.getArgument();
      case 'E' -> wayPointX += instr.getArgument();
      case 'S' -> wayPointY -= instr.getArgument();
      case 'W' -> wayPointX -= instr.getArgument();
      // Rotate waypoint left (ldegs / 90) times
      case 'L' -> {
        int ldegs = instr.getArgument();
        assert (ldegs % 90 == 0);
        while (ldegs != 0) {
          rotWPL();
          ldegs -= 90;
        }
      }
      // Rotate waypoint right (rdegs / 90) times
      case 'R' -> {
        int rdegs = instr.getArgument();
        assert (rdegs % 90 == 0);
        while (rdegs != 0) {
          rotWPR();
          rdegs -= 90;
        }
      }
      case 'F' -> {
        x += wayPointX * instr.getArgument();
        y += wayPointY * instr.getArgument();
      }
    }
  }

  private void advance(int distance) {
    x += dir.x * distance;
    y += dir.y * distance;
  }

  private void advance(Direction dir, int distance) {
    x += dir.x * distance;
    y += dir.y * distance;
  }

  // Rotate ship direction right using some smart use of enum constructors and modulo operation.
  private void rotr(int angle) {
    assert (angle % 90 == 0);
    Direction[] directions = Direction.values();
    dir = directions[(dir.ordinal() + (angle / 90)) % 4];
  }

  // Rotate ship direction left using some smart use of enum constructors and modulo operation.
  private void rotl(int angle) {
    assert (angle % 90 == 0);
    Direction[] directions = Direction.values();
    dir = directions[(((dir.ordinal() - (angle / 90)) % 4) + 4) % 4];
  }

  // Rotate waypoint by 90degs left with some trig
  private void rotWPL() {
    int temp = wayPointX;
    wayPointX = -wayPointY;
    wayPointY = temp;
  }

  // Rotate waypoint by 90degs right with some trig
  private void rotWPR() {
    int temp = wayPointY;
    wayPointY = -wayPointX;
    wayPointX = temp;
  }

  private enum Direction {
    N(0, 1),
    E(1, 0),
    S(0, -1),
    W(-1, 0);

    private final int x;
    private final int y;

    Direction(int x, int y) {
      this.x = x;
      this.y = y;
    }
  }

}
