package Day14;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

// Simple class to contain command information - can either be mask set or memory allocation
// So one of 'mask' or 'addr & val' will always be null
class Command {

  private final CommandType commandType;
  private Mask mask;
  private long addr;
  private long val;

  public Command(Mask mask) {
    commandType = CommandType.MASK;
    this.mask = mask;
  }

  public Command(long addr, long val) {
    commandType = CommandType.MEMORY;
    this.addr = addr;
    this.val = val;
  }

  public CommandType getCommandType() {
    return commandType;
  }

  public Mask getMask() {
    return mask;
  }

  public long getAddr() {
    return addr;
  }

  public long getVal() {
    return val;
  }

  public static Command parseCommand(String rawCommand) {
    if (rawCommand.contains("mask")) {
      return new Command(new Mask(rawCommand.substring(7)));
    } else {
      Pattern p = Pattern.compile("\\[([0-9]+)] = ([0-9]+)");
      Matcher m = p.matcher(rawCommand);
      m.find();
      long addr = Long.parseLong(m.group(1));
      long val = Long.parseLong(m.group(2));
      return new Command(addr, val);
    }
  }

  public enum CommandType {
    MASK,
    MEMORY
  }
}
