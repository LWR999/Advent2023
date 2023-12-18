// ------------------------------------------------------------
// Advent of Code 2023
// Day 14 - DishFocusController_Part1
// Calculate the load on the north support to align the mirrors
// filePath = ./day14/mirror_map.txt
// Leon Rees - 18 December 2023
// ------------------------------------------------------------
package day14;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class DishFocusController_Part1{
    public static void main(String[] args) throws IOException {
        // Reading the puzzle input from a file
        List<String> lines = readLinesFromFile("./day14/mirror_map.txt");

        // Converting the input into a 2D array for easier manipulation
        char[][] platform = convertTo2DArray(lines);

        // Tilting the platform north
        tiltPlatformNorth(platform);

        // Calculating the total load on the north support beams
        int totalLoad = calculateTotalLoad(platform);

        // Outputting the result
        System.out.println("Total load on the north support beams: " + totalLoad);
    }

    // Reads lines from a file and returns them as a list of strings
    private static List<String> readLinesFromFile(String filename) throws IOException {
        BufferedReader reader = new BufferedReader(new FileReader(filename));
        List<String> lines = new ArrayList<>();
        String line;
        while ((line = reader.readLine()) != null) {
            lines.add(line);
        }
        reader.close();
        return lines;
    }

    // Converts a list of strings into a 2D array of characters
    private static char[][] convertTo2DArray(List<String> lines) {
        char[][] array = new char[lines.size()][];
        for (int i = 0; i < lines.size(); i++) {
            array[i] = lines.get(i).toCharArray();
        }
        return array;
    }

    // Tilts the platform north, causing rounded rocks (O) to move upwards
    private static void tiltPlatformNorth(char[][] platform) {
        for (int col = 0; col < platform[0].length; col++) {
            for (int row = 0; row < platform.length; row++) {
                if (platform[row][col] == 'O') {
                    int currentRow = row;
                    while (currentRow > 0 && platform[currentRow - 1][col] == '.') {
                        // Swap the rock and the empty space
                        platform[currentRow - 1][col] = 'O';
                        platform[currentRow][col] = '.';
                        currentRow--;
                    }
                }
            }
        }
    }

    // Calculates the total load on the north support beams
    private static int calculateTotalLoad(char[][] platform) {
        int load = 0;
        for (int row = 0; row < platform.length; row++) {
            for (int col = 0; col < platform[row].length; col++) {
                if (platform[row][col] == 'O') {
                    load += platform.length - row;
                }
            }
        }
        return load;
    }
}
