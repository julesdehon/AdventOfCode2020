package Day20;

public class ArrayUtils {

  /* Utility functions */

  public static String verticalSlice(String[] arr, int i) {
    StringBuilder result = new StringBuilder();
    for (String line : arr) {
      result.append(line.charAt(i));
    }
    return result.toString();
  }

  // All strings in array must have same length
  public static String[] rotateArray(String[] array) {
    String[] result = new String[array[0].length()];
    for (int i = 0; i < result.length; i++) {
      result[i] = new StringBuilder(verticalSlice(array, i)).reverse().toString();
    }
    return result;
  }

  public static String[] flipArray(String[] array, char plane) {
    String[] result = new String[array.length];
    return switch (plane) {
      case 'V' -> {
        for (int i = 0; i < array.length; i++) {
          result[i] = array[array.length - 1 - i];
        }
        yield result;
      }
      case 'H' -> {
        for (int i = 0; i < array.length; i++) {
          result[i] = new StringBuilder(array[i]).reverse().toString();
        }
        yield result;
      }
      default -> throw new IllegalStateException("Unexpected value: " + plane);
    };
  }

}
