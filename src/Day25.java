public class Day25 {

  public static void main(String[] args) {
    long cardKey = 6929599;
    long doorKey = 2448427;
    int cardLoopSize = findLoopSize(cardKey, 7);
    long encryptionKey = transform(cardLoopSize, doorKey);
    System.out.println("The handshake is trying to establish the encryption key, " + encryptionKey);
  }

  private static int findLoopSize(long target, long subjectNumber) {
    int count = 0;
    long value = 1;
    while (value != target) {
      value *= subjectNumber;
      value %= 20201227;
      count++;
    }
    return count;
  }

  private static long transform(long loopSize, long subjectNumber) {
    long value = 1;
    for (int i = 0; i < loopSize; i++) {
      value *= subjectNumber;
      value %= 20201227;
    }
    return value;
  }
}
