package Day20;

import java.util.Objects;

// Border class - particularly useful to have a custom equals() to find matching borders, which
// doesn't discriminate on orientation
class Border {

  private String border;
  private String reversedBorder;

  public Border(String border) {
    this.border = border;
    this.reversedBorder = new StringBuilder(border).reverse().toString();
  }

  public String getNormalBorder() {
    return border;
  }

  public void reverse() {
    String temp = border;
    border = reversedBorder;
    reversedBorder = temp;
  }

  @Override
  // Either the border or the reversedBorder can match
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    Border otherBorder = (Border) o;
    return border.equals(otherBorder.border) || reversedBorder.equals(otherBorder.border);
  }

  @Override
  public int hashCode() {
    return Objects.hash(border) + Objects.hash(reversedBorder);
  }
}
