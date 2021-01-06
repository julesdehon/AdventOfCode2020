import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Deque;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class Day22 {

  public static void main(String[] args) throws IOException {
    BufferedReader reader = new BufferedReader(new FileReader("src/input/22.txt"));
    List<String> lines = reader.lines().collect(Collectors.toList());

    // Parse the input, and make a copy of the initial decks for use in part 1 and 2
    List<Deque<Integer>> decks = fillDecks(lines);
    List<Deque<Integer>> decksCopy = copyDecks(decks);

    Deque<Integer> winner = decksCopy.get(play(decks));
    System.out.println("In the simple version of Crab combat, the winner had a score of " + score(winner));

    Deque<Integer> winner2 = decksCopy.get(playRecursive(decksCopy));
    System.out.println("In the recursive version of Crab combat, the winner had a score of " + score(winner2));
  }

  // Plays a standard game of Crab Combat, updating the state of decks to reflect its final state,
  // and returning 0 if player 1 wins, or 1 if player 2 wins
  private static int play(List<Deque<Integer>> decks) {
    while (!decks.get(0).isEmpty() && !decks.get(1).isEmpty()) {
      int p1 = decks.get(0).pop(); // Get top card in both decks
      int p2 = decks.get(1).pop();
      decks.get(p1 > p2 ? 0 : 1).offer(Integer.max(p1, p2)); // Add cards to bottom of winning deck
      decks.get(p1 > p2 ? 0 : 1).offer(Integer.min(p1, p2));
    }
    return decks.get(0).isEmpty() ? 1 : 0; // Return the winner by checking which deck is empty
  }

  // Plays a recursive game of Crab Combat, updating the state of decks to reflect its final state,
  // and returning 0 if player 1 wins, or 1 if player 2 wins
  private static int playRecursive(List<Deque<Integer>> decks) {
    Set<List<Deque<Integer>>> seen = new HashSet<>(); // To avoid infinite loops
    while (!decks.get(0).isEmpty() && !decks.get(1).isEmpty()) {
      if (seen.contains(decks)) return 0; // If we're in an infinite loop, player 1 wins
      seen.add(copyDecks(decks));
      int p1 = decks.get(0).pop();
      int p2 = decks.get(1).pop();
      int winner;
      if (decks.get(0).size() >= p1 && decks.get(1).size() >= p2) {
        // If each player has enough cards, make appropriate copies and play a sub-game of combat
        winner = playRecursive(copyDecks(decks, p1, p2));
      } else {
        // Otherwise, the winner is found in the standard way
        winner = p1 >= p2 ? 0 : 1;
      }
      decks.get(winner).offer(winner == 0 ? p1 : p2); // Add cards to bottom of winning deck
      decks.get(winner).offer(winner == 0 ? p2 : p1);
    }
    return decks.get(0).isEmpty() ? 1 : 0;
  }

  // Special case of the other copyDecks method, where we want to copy the whole deck
  private static List<Deque<Integer>> copyDecks(List<Deque<Integer>> decks) {
    return copyDecks(decks, decks.get(0).size(), decks.get(1).size());
  }

  // Make a copy of both decks and remove the right amount of cards from the end of them, so that
  // deck1 has cards1 cards left, and deck2 has cards2 left
  private static List<Deque<Integer>> copyDecks(
      List<Deque<Integer>> decks, int cards1, int cards2) {
    Deque<Integer> deck1 = new LinkedList<>(decks.get(0));
    Deque<Integer> deck2 = new LinkedList<>(decks.get(1));
    int n = deck1.size();
    int m = deck2.size();
    for (int i = 0; i < n - cards1; i++) deck1.removeLast();
    for (int i = 0; i < m - cards2; i++) deck2.removeLast();
    return List.of(deck1, deck2);
  }

  // Score a deck according to the specification
  private static long score(Deque<Integer> winner) {
    long score = 0;
    for (int i = winner.size(); i >= 1; i--) {
      score += i * winner.pop();
    }
    return score;
  }

  // Parse the decks
  private static List<Deque<Integer>> fillDecks(List<String> input) {
    List<Deque<Integer>> decks = List.of(new LinkedList<>(), new LinkedList<>());
    int player = 1;
    for (String line : input) {
      if (line.contains("Player")) {
        player = 1 - player; // bit of a messy way to do this but it works..
      } else if (!line.isEmpty()) {
        decks.get(player).offerLast(Integer.parseInt(line));
      }
    }
    return decks;
  }
}
