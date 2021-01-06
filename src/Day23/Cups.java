package Day23;

import java.util.List;

class Cups {

  private int[] nextCup; // nextCup[n] = the cup directly clockwise of the cup with label n
  private int current;

  public Cups(List<Integer> cups) {
    // Create an array such that nextCup[n] = the cup directly clockwise of the cup with label n
    this.nextCup = new int[cups.size() + 1];
    for (int i = 0; i < cups.size() - 1; i++) {
      this.nextCup[cups.get(i)] = cups.get(i + 1);
    }
    this.nextCup[cups.get(cups.size() - 1)] = cups.get(0);
    this.current = cups.get(0);
  }

  // Wrapper around other move method, just calls it n times
  public void move(int n) {
    for (int i = 0; i < n; i++) {
      move();
    }
  }

  private void move() {
    // Get the 3 cups following the current cup
    int a = nextCup[current];
    int b = nextCup[a];
    int c = nextCup[b];

    // Find the destination cup - if it's less than 1, wrap around, and if it's one of a,b,c
    // decrement it.
    int dest = current - 1;
    while (dest < 1 || dest == a || dest == b || dest == c) {
      if (dest < 1) {
        dest = nextCup.length - 1;
      } else {
        dest--;
      }
    }
    nextCup[current] = nextCup[c]; // 'Discards' a-c from the list
    int temp = nextCup[dest]; // save next cup after destination
    nextCup[dest] = a; // 'Adds' a-c back into the list just after dest
    nextCup[c] = temp;
    current = nextCup[current]; // update the current cup to the next cup in the list
  }

  public int getCupAfter(int i) {
    return nextCup[i];
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    int cur = nextCup[1];
    do {
      sb.append(cur);
      cur = nextCup[cur];
    } while (cur != 1);
    return sb.toString();
  }
}
