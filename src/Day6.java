import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Day6 {

  private static List<AnswerGroup> parseAnswers(BufferedReader reader) throws IOException {
    List<AnswerGroup> rawAnswers = new ArrayList<>();

    // Iterate through lines, concatenating those not separated by an empty line, and adding 1 to
    // peopleCount for each line in that group of answers
    String line;
    String curLine = "";
    int peopleCount = 0;
    while ((line = reader.readLine()) != null) {
      if (line.isEmpty()) {
        rawAnswers.add(new AnswerGroup(curLine, peopleCount));
        peopleCount = 0;
        curLine = "";
        continue;
      }
      curLine += line;
      peopleCount++;
    }
    // Store concatenation of answers and line count in rawAnswers list
    rawAnswers.add(new AnswerGroup(curLine, peopleCount)); // Add the last line!
    return rawAnswers;
  }

  private static int getCountAnyone(String answers) {
    // Set freq[question] = true if anyone in the group answered yes to it
    boolean[] freq = new boolean[26];
    for (int i = 0; i < answers.length(); i++) {
      freq[answers.charAt(i) - 'a'] = true;
    }
    int count = 0;
    // Count number of answers that anyone answered yes to
    for (boolean b : freq) count += b ? 1 : 0;
    return count;
  }

  private static int getCountEveryone(AnswerGroup answers) {
    // Set freq[question] to the number of people that answered yes to that question
    int[] freq = new int[26];
    for (int i = 0; i < answers.answers.length(); i++) {
      freq[answers.answers.charAt(i) - 'a'] += 1;
    }
    int count = 0;
    // Count number of answers that everyone in the group answered yes to
    for (int i : freq) count += i == answers.numPeople ? 1 : 0;
    return count;
  }

  private static class AnswerGroup {
    private String answers;
    private int numPeople;

    public AnswerGroup(String answers, int numPeople) {
      this.answers = answers;
      this.numPeople = numPeople;
    }
  }

  public static void main(String[] args) throws IOException {
    BufferedReader reader = new BufferedReader(new FileReader("src/input/6.txt"));

    // Get information about each group's answers (their answers and num people in the group)
    List<AnswerGroup> rawAnswers = parseAnswers(reader);

    // For each group, count number of answers that anyone answered yes to, then sum all those numbers
    int sumCountAnyone =
        rawAnswers.stream().map(a -> getCountAnyone(a.answers)).reduce(0, Integer::sum);
    System.out.println("Sum of all questions anyone answered yes to = " + sumCountAnyone);

    // For each group, count number of answers that everyone answered yes to, then sum all those numbers
    int sumCountEveryone = rawAnswers.stream().map(Day6::getCountEveryone).reduce(0, Integer::sum);
    System.out.println("Sum of all questions everyone answered yes to = " + sumCountEveryone);
  }
}
