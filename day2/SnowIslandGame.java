// ------------------------------------------------------------
// Advent of Code 2023
// Day 2 - SnowIslandGame
// Figure out which games are possible
// Leon Rees - 2 December 2023
// ------------------------------------------------------------
package day2;

import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class SnowIslandGame {

  public static void main(String[] args) {
    // File path to the game data file
    String filePath = "./day2/game_data.txt";

    try {
      // Create a BufferedReader to read from the file
      BufferedReader reader = new BufferedReader(new FileReader(filePath));
      String line;
      long totalPowerSum = 0; // Variable to store the sum of powers of all games

      // Read the file line by line
      while ((line = reader.readLine()) != null) {
        // Find the minimum number of cubes for the current game
        int[] minCubes = findMinimumCubes(line);
        // Calculate the power for the current game
        long power = (long) minCubes[0] * minCubes[1] * minCubes[2];
        // Add the power to the total sum
        totalPowerSum += power;
      }

      // Close the BufferedReader
      reader.close();
      // Print the total sum of powers
      System.out.println("Total sum of the power of the minimum sets: " + totalPowerSum);
    } catch (IOException e) {
      // Print the stack trace in case of an exception
      e.printStackTrace();
    }
  }

  // Method to find the minimum number of cubes required for each color in a game
  private static int[] findMinimumCubes(String game) {
    // Split the game data into individual turns
    String[] turns = game.split(";");
    // Variables to store the maximum count of each color found in any turn
    int maxRed = 0, maxGreen = 0, maxBlue = 0;

    // Iterate through each turn
    for (String turn : turns) {
      // Count the cubes of each color in the turn
      int red = countCubes(turn, "red");
      int green = countCubes(turn, "green");
      int blue = countCubes(turn, "blue");

      // Update the maximum count if the current turn has more cubes of that color
      maxRed = Math.max(maxRed, red);
      maxGreen = Math.max(maxGreen, green);
      maxBlue = Math.max(maxBlue, blue);
    }

    // Return the maximum counts as the minimum required cubes for the game
    return new int[] {maxRed, maxGreen, maxBlue};
  }

  // Helper method to count the number of cubes of a specific color in a turn
  private static int countCubes(String turn, String color) {
    // Use regex to find the number of cubes of the specified color in the turn string
    Matcher matcher = Pattern.compile("(\\d+) " + color).matcher(turn);
    int count = 0;
    // Sum up all occurrences of that color in the turn
    while (matcher.find()) {
      count += Integer.parseInt(matcher.group(1));
    }
    return count;
  }
}
