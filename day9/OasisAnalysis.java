// ------------------------------------------------------------
// Advent of Code 2023
// Day 9 - OasisAnalysis
// Process the map to escape the sandstorm in ghost mode
// filePath = ./day9/environment_readings.txt
// Leon Rees - 11 December 2023
// ------------------------------------------------------------
package day9;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class OasisAnalysis {
    public static void main(String[] args) throws FileNotFoundException {
        // Read puzzle input from a file
        File file = new File("./day9/environment_readings.txt");
        Scanner scanner = new Scanner(file);
        List<List<Integer>> histories = new ArrayList<>();

        // Parse each line into a list of integers
        while (scanner.hasNextLine()) {
            String[] values = scanner.nextLine().split(" ");
            List<Integer> history = new ArrayList<>();
            for (String value : values) {
                history.add(Integer.parseInt(value));
            }
            histories.add(history);
        }
        scanner.close();

        // Process each history to find the extrapolated previous value
        long sum = 0;
        for (List<Integer> history : histories) {
            sum += extrapolatePreviousValue(history);
        }

        // Output the sum of the extrapolated values
        System.out.println("Sum of extrapolated values: " + sum);
    }

    // Method to extrapolate the previous value of a given history
    private static int extrapolatePreviousValue(List<Integer> history) {
        List<List<Integer>> sequences = new ArrayList<>();
        sequences.add(new ArrayList<>(history));

        // Generate sequences of differences until all are zero
        while (!allZeroes(sequences.get(0))) {
            List<Integer> previousSequence = sequences.get(0);
            List<Integer> newSequence = new ArrayList<>();
            for (int i = 1; i < previousSequence.size(); i++) {
                newSequence.add(previousSequence.get(i) - previousSequence.get(i - 1));
            }
            sequences.add(0, newSequence);
        }

        // Add a zero at the beginning and calculate the new first values
        sequences.get(0).add(0, 0);
        for (int i = 1; i < sequences.size(); i++) {
            int newFirstValue = sequences.get(i).get(0) + sequences.get(i - 1).get(0);
            sequences.get(i).add(0, newFirstValue);
        }

        // Return the new first value of the original history
        return sequences.get(sequences.size() - 1).get(1);
    }

    // Helper method to check if all elements in a list are zero
    private static boolean allZeroes(List<Integer> list) {
        for (int value : list) {
            if (value != 0) {
                return false;
            }
        }
        return true;
    }
}
