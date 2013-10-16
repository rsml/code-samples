/**
 * @author Ross Miller
 * @version 1.0
 * @since 2010-3-30
 *
 * To compile use the command `javac ColumnSort.java`
 * To run use the command `java ColumnSort sample.dat` (where sample.dat
 * is any plain text file where each line is either whitespace or an integer)
 */

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 * 
 * Class: ColumnSort
 * 
 * This class reads in a file from the command line, and then sorts all of the lines and outputs the sorted lines.
 * Column sort is a fast sorting algorithm this is easily parallelized
 * At it's core it relies on Quicksort
 * This class only works when sorting Integers
 * This is a class who's methods are all static and has no class variables
 * 
 * METHODS
 * -------
 * public static void main(String[] args)
 *      Reads in the given file, outputs various information about the input, then calls all of the functions necessary to sort the numbers and outputs the numbers
 * 
 * public static Integer[][] step2(Integer[][] numbers)
 *      Transposes the r x s matrix and reshapes it back into an r x s matrix by writing each row of the transpose as a r/s x s submatrix.
 * 
 * public static Integer[][] step4(Integer[][] numbers)
 *      Reshapes each set of r/s element rows into r element rows. It then transposes the resulting matrix.
 * 
 * public static Integer[][] step6(Integer[][] numbers)
 *      Shift each column down r/2 positions. Wraps into the next column if necessary. The first half of the first column is filled with negative infinities. The second half of the last column is filled with positive infinities.
 * 
 * public static Integer[][] step8(Integer[][] numbers)
 *      Shifts each column up by r/2 positions. Wraps around to the previous column if necessary. 
 * 
 * public static Integer[][] oddStep(Integer[][] numbers)
 *      uses the quicksort method to sort each column
 * 
 * public static void quicksort(Integer[] toSort, int left, int right)
 *      Uses quicksortHelper to help sort an array of Integers.
 * 
 * public static int quicksortHelper(Integer[] toSort, int left, int right)
 *      Gets a pivot and swaps elements on either side so they are in sorted order
 * 
 */
public class ColumnSort {
  
  /**
   * Reads in the given file, outputs various information about the input, then calls all of the functions necessary to sort the numbers and outputs the numbers.
   * Outputs how many numbers there are, the number of rows and columns, the time it takes to sort the numbers, and all of the numbers in sorted order.
   *
   * @param args The path of a file containing lines where each line is either whitespace or a number.
   * @return void
   */
  public static void main(String[] args) {
    if (args == null || args.length == 0) {
      System.out.println("Please enter a filename as the first argument");
      return;
    }

    String filename = args[0];
    
    List<Integer> elements = new ArrayList<Integer>();
    File file = new File(filename);
    Scanner scanner = null;
    try {
      scanner = new Scanner(file);
    } catch (FileNotFoundException e) {
      System.out.println("That file cannot be found");
      return;
    }
    
    // Get each line, trim it, and store it
    while (scanner.hasNextLine()) {
      String line = scanner.nextLine().trim();
      if (line == null || line.length() == 0)
        continue;
      Integer number = Integer.parseInt(line);
      if (line.length() > 0) elements.add(number);
    }
    
    int length = elements.size();
    int rows = 1;
    int cols = length;
    
    // Turn the elements into a matrix the is as square as possible
    while (rows*cols != length || rows < 2*Math.pow(cols-1,2) || rows % cols != 0) {
      cols--;
      rows = length/cols;
    }
    
    System.out.println("n = "+length);
    System.out.println("r = "+rows);
    System.out.println("s = "+cols);
    
    // Construct 2d array given the number of rows and cols that we found above
    Integer[][] array = new Integer[rows][cols];
    for (int row=0; row<rows; row++) {
      for (int col=0; col<cols; col++) {
        array[row][col] = elements.get((col * rows) + row);
      }
    }
    
    System.gc(); // Clear up any unneeded space
    
    long startTime = System.nanoTime();
    
    // Perform all of the steps in order
    Integer[][] result = step8(oddStep(step6(oddStep(step4(oddStep(step2(oddStep(array))))))));
    
    long elapsedTime = System.nanoTime() - startTime;
    long roundedTime = (int) (elapsedTime/1000000.0 + 0.5); // Round to the nearest millisecond
    
    System.out.println("Elapsed time = " + roundedTime / 1000.0 + " seconds.");
    
    // Print out sorted list
    for (int col=0; col<cols; col++) {
      for (int row=0; row<rows; row++) {
        System.out.println(result[row][col]);
      }
    }
  }
  
  /**
   * Transposes the r x s matrix and reshapes it back into an r x s matrix by writing each row of the transpose as a r/s x s submatrix.
   * Preconditions: oddStep has been called
   *
   * @param numbers A 2d array of partially sorted numbers
   * @return The result of applying the algorithm described above to the argument
   */
  public static Integer[][] step2(Integer[][] numbers) {
    int rows = numbers.length;
    int cols = numbers[0].length;
    
    Integer[][] newNumbers = new Integer[rows][cols];
    
    // This should never happen, but if it does, it's better to catch it now than to have the program crash
    if (rows % cols != 0) {
      System.out.println("Error: Rows is not a multible of Cols");
      System.exit(1);
    }
    
    int ratio = rows/cols;
    for (int col=0; col<cols; col++) {
      for (int row=0; row<rows; row++) {
        newNumbers[(col * ratio) + (row / cols)][row % cols] = numbers[row][col];
      }
    }
    
    return newNumbers;
  }
  
  /**
   * Reshapes each set of r/s element rows into r element rows. It then transposes the resulting matrix.
   * Preconditions: Methods are called in the following order: oddStep, step2, oddStep
   * 
   * @param numbers A 2d array of partially sorted numbers
   * @return The result of applying the algorithm described above to the argument
   */
  public static Integer[][] step4(Integer[][] numbers) {
    int rows = numbers.length;
    int cols = numbers[0].length;
    
    Integer[][] newNumbers = new Integer[rows][cols];
    
    int ratio = rows / cols;
    for (int row=0; row<rows; row++) {
      for (int col=0; col<cols; col++) {
        newNumbers[row][col] = numbers[(col * ratio) + (row / cols)][row % cols];
      }
    }
    
    return newNumbers;
  }
  
  /**
   * Shift each column down r/2 positions. Wraps into the next column if necessary.
   * The first half of the first column is filled with negative infinities.
   * The second half of the last column is filled with positive infinities.
   * Preconditions: Methods are called in the following order: oddStep, step2, oddStep, step4, oddStep
   *
   * @param numbers A 2d array of partially sorted numbers
   * @return The result of applying the algorithm described above to the argument
   */
  public static Integer[][] step6(Integer[][] numbers) {
    int rows = numbers.length;
    int cols = numbers[0].length;
    
    Integer[][] newNumbers = new Integer[rows][cols + 1];
    
    for (int row=0; row<rows/2; row++) newNumbers[row][0] = Integer.MIN_VALUE; // Substitute for negative infinity
    for (int row=rows/2; row<rows; row++) newNumbers[row][cols] = Integer.MAX_VALUE; // Substitute for positive infinity
    
    for (int row=0; row<rows; row++) {
      for (int col=0; col<cols; col++) {
        int number = (col * rows) + row;
        
        number += (rows / 2);
        int newRow = number % rows;
        int newCol = number / rows;
        
        newNumbers[newRow][newCol] = numbers[row][col];
      }
    }
    
    return newNumbers;
  }
  
  /**
   * Shifts each column up by r/2 positions. Wraps around to the previous column if necessary. 
   * Preconditions: Methods are called in the following order: oddStep, step2, oddStep, step4, oddStep, step6, oddStep
   *
   * @param numbers A 2d array of partially sorted numbers
   * @return The result of applying the algorithm described above to the argument
   */
  public static Integer[][] step8(Integer[][] numbers) {
    int rows = numbers.length;
    int cols = numbers[0].length;
    
    Integer[][] newNumbers = new Integer[rows][cols-1];
    
    int extra = rows % 2;
    for (int col=0; col<cols; col++) {
      for (int row=0; row<rows; row++) {
        if (row < rows/2) {
          if (col > 0)
            newNumbers[row+(rows/2)+extra][col-1] = numbers[row][col];
        } else {
          if (col < cols-1)
            newNumbers[row-(rows/2)][col] = numbers[row][col];
        }
      }
    }
    
    return newNumbers;
  }
  
  /**
   * Uses the quicksort method to sort each column
   * Preconditions: The correct order of methods has been called.
   * See the precondition of step8 for more info on the order
   *
   * @param numbers A 2d array of partially sorted numbers
   * @return The result of applying the algorithm described above to the argument
   */
  public static Integer[][] oddStep(Integer[][] numbers) {
    int rows = numbers.length;
    int cols = numbers[0].length;
    
    for (int col=0; col<cols; col++) {
      Integer[] column = new Integer[rows];
      for (int row=0; row<rows; row++) {
        column[row] = numbers[row][col];
      }
      
      quicksort(column, 0, rows-1);
      for (int row=0; row<rows; row++) {
        numbers[row][col] = column[row];
      }
    }
    
    return numbers;
  }
  
  /**
   * Sorts an array of Integers using the quicksort algorithm.
   *
   * @param toSort An array of Integers to be sorted
   * @param left The leftmost element to be sorted
   * @param right The rightmost element to be sorted
   * @return A sorted array of Integers 
   */
  public static void quicksort(Integer[] toSort, int left, int right) {
    int position = quicksortHelper(toSort,left,right); // First pivot
    if (position < right) quicksort(toSort, position, right);
    if (left < position - 1) quicksort(toSort, left, position - 1);
  }
  
  /**
   * Gets a pivot and swaps elements on either side so they are in sorted order
   *
   * @param toSort An array of Integers to be sorted
   * @param left The leftmost element to be sorted
   * @param right The rightmost element to be sorted
   * @return The new max or min index (whichever applies) for the next quicksort of a sublist (if there is one)
   */
  private static int quicksortHelper(Integer[] toSort, int left, int right) {
    int pivot = toSort[(left + right) / 2]; // Pick middle element as pivot

    // Sort around pivot
    while (left <= right) {
      while (toSort[left] < pivot)
        left++;

      while (toSort[right] > pivot)
        right--;
      
      // Now we're at a point where toSort[left] >= pivot, and toSort[right] <= pivot. So we need to swap these elements
      if (left <= right) {
        int swap = toSort[left];
        toSort[left] = toSort[right];
        toSort[right] = swap;

        right--;
        left++;
      }
    }
    
    return left;
  }
}
