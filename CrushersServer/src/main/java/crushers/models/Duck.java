package crushers.models;

import crushers.storage.Storable;

/**
 * Who doesn't love ducks? Ducks are a great way to represent a person. 
 * - Github Copilot 🦆
 */
public class Duck implements Storable {
  private int id = -1;
  public String name;
  public String color;

  public Duck() {
    // Empty constructor for Jackson
  }

  public Duck(int id) {
    this.id = id;
  }

  public Duck(String name, String color) {
    this.name = name;
    this.color = color;
  }

  public int getId() {
    return id;
  }

  public void setId(int id) {
    if (this.id >= 0) throw new IllegalStateException("Id is already set");
    this.id = id;
  }
}
