import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

/**
 * 
 * Class: Entry
 *
 * Created by Ross Miller on 4/20/10
 * 
 * The class manages an Entry object, which is just a string combine with a list of paragraph
 *    number and line numbers where this word has appeared
 * 
 * VARIABLES
 * ---------
 * private List<Integer[]> occurences
 *    A list of 2D arrays where the first element is a paragraph number, and the second is a line number
 * 
 * private String name
 *    The String that this Entry is keeping track of
 * 
 * METHODS
 * -------
 * public Entry(String name, int paragraph, int line);
 *    Constructs an Entry object given a name, paragraph number, and a line number
 * 
 * public String getName();
 *    returns the name of this entry
 * 
 * public void addOccurence(int paragraph, int line);
 *    Adds a paragraph and line number onto an existing Entry and keeps them in increasing order
 * 
 * public String toString();
 *    Creates a String representation of this object
 * 
 * public int compareTo(Entry e);
 *    Compares an entry with another based on the comparison of their respective names
 * 
 */
public class Entry implements Comparable<Entry> {
  private List<Integer[]> occurences;
  private String name;

  /**
   * Constructs an Entry object given a name, paragraph number, and a line number
   * Postconditions: An Entry object is created
   * @param name The string that this entry is keeping track of
   * @param paragraph The paragraph number where this word was first found
   * @param line The line number where this word was first found
   */
  public Entry(String name, int paragraph, int line) {
    this.name = name;
    occurences = new LinkedList<Integer[]>();
    Integer[] arr = new Integer[2];
    arr[0] = paragraph;
    arr[1] = line;
    occurences.add(arr);
  }

  /**
   * Returns the name of this entry
   * @return The name of this entry
   */
  public String getName() {
    return name;
  }
  
  /**
   * Adds a paragraph and line number onto an existing Entry and keeps
   *    them in order, sorted by paragraph number, first
   * Postconditions: An extra paragraph and line number are added onto this entry
   * @param paragraph The paragraph number where this word was found
   * @param line The line number where this word was found
   */
  public void addOccurence(int paragraph, int line) {
    Integer[] array = new Integer[2];
    array[0] = paragraph;
    array[1] = line;
    int i = 0;
    while (i < occurences.size() &&
        (paragraph > occurences.get(i)[0]
        || (paragraph == occurences.get(i)[0] && line > occurences
            .get(i)[1]))) {
      i++;
    }

    occurences.add(i, array);
  }

  /**
   * Creates a String representation of this object
   * @return A String representation of this object
   */
  public String toString() {
    String str = "";
    int i = 0;

    // Loop through all of the occurences
    Iterator<Integer[]> iter = occurences.iterator();
    while (iter.hasNext()) {
      Integer[] arr = iter.next();

      String s = "";
      for (int j = 0; j < 24; j++)
        s += " ";

      if (i == 0)
        str += name + s.substring(0, 24 - name.length()) + "(" + arr[0]
            + "," + arr[1] + ")";
      else if (i % 8 == 0)
        str += "\n\"" + s.substring(0, 23) + "(" + arr[0] + ","
            + arr[1] + ")";
      else
        str += " (" + arr[0] + "," + arr[1] + ")";
      i++;
    }

    return str + "\n";
  }

  /**
   * Compares an entry with another based on the comparison of their respective names
   * @param e The Entry object that that current one will be compared against
   * @return An int less than 0 if this is less than the argument; 0 if they are equal; and greater
   *    than 0 if this is greater than the argument
   */
  public int compareTo(Entry e) {
    return name.compareTo(e.name);
  }
}
