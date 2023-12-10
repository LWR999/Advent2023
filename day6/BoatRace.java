// ------------------------------------------------------------
// Advent of Code 2023
// Day 6 - BoatRace
// Calculate different strategies for winning the boat race
// filePath = ./day6/race_results.txt
// Leon Rees - 10 December 2023
// ------------------------------------------------------------
package day6;

import java.io.*;
import java.util.*;

public class BoatRace {

    public static void main(String[] args) {
        // File path for the input data
        String filePath = "./day6/race_results.txt";

        // Variables to store time and distance
        long time = 0;
        long distance = 0;

        // Read the file
        try (Scanner scanner = new Scanner(new File(filePath))) {
            // Skip the label "Time:" and read the time as a single concatenated string
            scanner.next(); // Skip the "Time:" label
            String timeStr = scanner.nextLine().trim().replaceAll(" ", "");
            time = Long.parseLong(timeStr);

            // Skip the label "Distance:" and read the distance as a single concatenated string
            scanner.next(); // Skip the "Distance:" label
            String distanceStr = scanner.nextLine().trim().replaceAll(" ", "");
            distance = Long.parseLong(distanceStr);
        } catch (FileNotFoundException e) {
            System.out.println("File not found: " + e.getMessage());
            return;
        }

        // Calculate the number of ways to beat the record for the race
        long ways = calculateWaysToBeatRecord(time, distance);

        // Output the result
        System.out.println("Total number of ways to beat the record in the race: " + ways);
    }

    // Method to calculate the number of ways to beat the record for the single race
    private static long calculateWaysToBeatRecord(long time, long recordDistance) {
        long ways = 0;
        for (long holdTime = 1; holdTime < time; holdTime++) {
            long travelTime = time - holdTime;
            long distance = holdTime * travelTime;
            if (distance > recordDistance) {
                ways++;
            }
        }
        return ways;
    }
}
