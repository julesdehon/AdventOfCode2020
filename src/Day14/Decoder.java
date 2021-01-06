package Day14;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

class Decoder {

  private final Map<Long, Long> memory = new HashMap<>();
  boolean version1;

  public Decoder(boolean version1) {
    this.version1 = version1;
  }

  public void decode(List<String> input) {
    Mask mask = Command.parseCommand(input.get(0)).getMask(); // First command will always set the mask
    // For each command, either set the mask, or update memory using memory allocation command
    // info
    for (int i = 1; i < input.size(); i++) {
      Command command = Command.parseCommand(input.get(i));
      if (command.getCommandType() == Command.CommandType.MASK) {
        mask = command.getMask();
      } else {
        if (version1) {
          // If part1, a simple memory update will suffice
          memory.put(command.getAddr(), mask.applyTo(command.getVal()));
        } else {
          // If part2, we need to update all memory locations as per the specification
          updateAllMemoryLocs(mask, command.getAddr(), command.getVal());
        }
      }
    }
  }

  public Collection<Long> getMemoryValues() {
    return memory.values();
  }

  /* Used for part 2 */

  // Sets every possible memory address referred to by the combination of addr and mask to val.
  private void updateAllMemoryLocs(Mask mask, long addr, long val) {
    for (long possibleAddr :
        getAllPossibleAddresses(mask.getFloats(), addr | mask.getOnes(), 0, 1L << 35)) {
      memory.put(possibleAddr, val);
    }
  }

  // Recursive function to find all memory addresses that need to be updated for a given floatMask
  // and baseAddr. We build up addrSoFar throughout the recursive calls, and measure the depth of
  // recursion is measured by index, which has 1 bit set at the bit position we are currently
  // inspecting
  private static Set<Long> getAllPossibleAddresses(
      long floatMask, long baseAddr, long addrSoFar, long index) {
    Set<Long> addresses = new HashSet<>();
    if (index == 0) {
      addresses.add(addrSoFar);
      return addresses;
    } else {
      // If the mask has an X at the bit we are currently inspecting, get all the next possible
      // addresses where the bit at this position is 1, and all those where it is 0, and add all
      // of them to addresses.
      if ((floatMask & index) != 0) {
        addresses.addAll(
            getAllPossibleAddresses(floatMask, baseAddr, index | addrSoFar, index >> 1));
        addresses.addAll(
            getAllPossibleAddresses(floatMask, baseAddr, ~index & addrSoFar, index >> 1));
      } else {
        // If not, simply find all the possible addresses where the bit we are currently
        // inspecting
        // is the same as that in the baseAddr
        addresses.addAll(
            getAllPossibleAddresses(
                floatMask, baseAddr, (baseAddr & index) | addrSoFar, index >> 1));
      }
    }
    return addresses;
  }
}
