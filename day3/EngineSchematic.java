// ------------------------------------------------------------
// Advent of Code 2023
// Day 3 - EngineSchematic
// Decode part numbers from the engine schematic
// Leon Rees - 3 December 2023
// ------------------------------------------------------------
package day3;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

public class EngineSchematic {

  public static void main(String[] args) {
    String filePath = "./day3/engine_schematic.txt";

    try {
      // Read the file contents into an array of strings, each representing a line
      BufferedReader reader = new BufferedReader(new FileReader(filePath));
      String[] lines = reader.lines().toArray(String[]::new);
      reader.close(); // Close the reader to free resources

      int totalGearRatioSum = 0; // Variable to accumulate the total sum of gear ratios

      // Iterate through each line and character position to identify gears
      for (int i = 0; i < lines.length; i++) {
        for (int j = 0; j < lines[i].length(); j++) {
          // Check if the current character is a gear symbol (*)
          if (lines[i].charAt(j) == '*') {
            // Find numbers adjacent to the gear and add them to a set
            Set<Integer> adjacentNumbers = findAdjacentNumbers(lines, i, j);
            // If exactly two numbers are found, multiply them to get the gear ratio
            if (adjacentNumbers.size() == 2) {
              int gearRatio = adjacentNumbers.stream().reduce(1, (a, b) -> a * b);
              totalGearRatioSum += gearRatio; // Add the gear ratio to the total sum
            }
          }
        }
      }

      // Output the total sum of all gear ratios
      System.out.println("Total sum of gear ratios: " + totalGearRatioSum);
    } catch (IOException e) {
      e.printStackTrace(); // Print the error if something goes wrong during file reading
    }
  }

  // Method to find numbers adjacent to a gear symbol (*)
  private static Set<Integer> findAdjacentNumbers(String[] lines, int lineIndex, int charIndex) {
    Set<Integer> numbers = new HashSet<>(); // A set to hold unique adjacent numbers

    // Check all positions around the gear symbol (*)
    for (int i = -1; i <= 1; i++) {
      for (int j = -1; j <= 1; j++) {
        // Skip the gear position itself
        if (i == 0 && j == 0) continue;

        int rowIndex = lineIndex + i;
        int colIndex = charIndex + j;

        // Check if the position is within the bounds of the schematic
        if (isValidIndex(rowIndex, colIndex, lines)) {
          // Check if the character at the position is a digit
          char ch = lines[rowIndex].charAt(colIndex);
          if (Character.isDigit(ch)) {
            // Extract the complete number starting from this digit
            int number = extractNumber(lines, rowIndex, colIndex);
            // Add the number to the set if it's valid
            if (number != -1) {
              numbers.add(number);
              // If two numbers are found, return the set immediately
              if (numbers.size() == 2) {
                return numbers;
              }
            }
          }
        }
      }
    }

    return numbers; // Return the set of adjacent numbers
  }

  // Check if the given index is within the bounds of the schematic
  private static boolean isValidIndex(int rowIndex, int colIndex, String[] lines) {
    return rowIndex >= 0 && rowIndex < lines.length && colIndex >= 0 && colIndex < lines[rowIndex].length();
  }

  // Extracts a complete number from the schematic starting at a given position
  private static int extractNumber(String[] lines, int rowIndex, int colIndex) {
    // Find the start of the number
    while (colIndex > 0 && Character.isDigit(lines[rowIndex].charAt(colIndex - 1))) {
      colIndex--; // Move backward to the start of the number
    }

    // Build the number from its individual digits
    StringBuilder numberBuilder = new StringBuilder();
    while (colIndex < lines[rowIndex].length() && Character.isDigit(lines[rowIndex].charAt(colIndex))) {
      numberBuilder.append(lines[rowIndex].charAt(colIndex)); // Append each digit
      colIndex++; // Move forward through the number
    }

    return Integer.parseInt(numberBuilder.toString()); // Convert the string to an integer
  }
}
