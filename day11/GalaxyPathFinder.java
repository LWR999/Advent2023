// ------------------------------------------------------------
// Advent of Code 2023
// Day 11 - GalaxyPathFinder
// Find the shortest paths to galaxies in the galaxy map
// filePath = ./day11/galaxy_map.txt
// Leon Rees - 17 December 2023
// ------------------------------------------------------------
package day11;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

// Utilizing binarySearch from Collections for efficiency
import static java.util.Collections.binarySearch;

// Renamed class from Day11 to GalaxyPathFinder
public class GalaxyPathFinder {
    
    public static void main(String[] args) throws IOException {
        long startTime = System.currentTimeMillis();

        // Reading input file for galaxy data
        BufferedReader fileReader = new BufferedReader(new FileReader("./day11/galaxy_map.txt"));
        HashMap<Integer, List<StellarObject>> columnToGalaxiesMap = new HashMap<>();
        List<StellarObject> stellarObjects = new ArrayList<>();
        List<Integer> vacantRows = new ArrayList<>();
        List<Integer> vacantColumns = new ArrayList<>();
        String currentLine;
        int currentRow = 0;
        int totalColumns = -1;

        // Processing each line of the file
        while ((currentLine = fileReader.readLine()) != null) {
            boolean galaxyInRow = false;
            totalColumns = currentLine.length();
            for (int col = 0; col < totalColumns; col++) {
                char charAtPosition = currentLine.charAt(col);
                if (charAtPosition == '#') {
                    galaxyInRow = true;
                    StellarObject galaxy = new StellarObject(currentRow, col);
                    stellarObjects.add(galaxy);
                    columnToGalaxiesMap.computeIfAbsent(col, k -> new ArrayList<>()).add(galaxy);
                }
            }
            if (!galaxyInRow) {
                vacantRows.add(currentRow);
            }
            currentRow++;
        }
        fileReader.close();

        // Identifying empty columns
        for (int col = 0; col < totalColumns; col++) {
            if (columnToGalaxiesMap.get(col) == null) {
                vacantColumns.add(col);
            }
        }

        // Calculating distances
        long initialCalculation = calculateDistances(stellarObjects, vacantRows, vacantColumns, 2);
        System.out.println(initialCalculation);
        long expandedCalculation = calculateDistances(stellarObjects, vacantRows, vacantColumns, 1000000);
        System.out.println(expandedCalculation);

        long endTime = System.currentTimeMillis();
        System.out.printf("\n%5.3f sec\n", (endTime - startTime) / 1000f);
    }

    // Method for calculating distances between galaxies
    private static long calculateDistances(List<StellarObject> galaxies, List<Integer> emptyRows, List<Integer> emptyCols, long expansionFactor) {
        expansionFactor--;
        long totalDistance = 0;
        for (int i = 0; i < galaxies.size(); i++) {
            StellarObject firstGalaxy = galaxies.get(i);
            for (int j = i + 1; j < galaxies.size(); j++) {
                StellarObject secondGalaxy = galaxies.get(j);
                long distance = Math.abs(firstGalaxy.row - secondGalaxy.row) + Math.abs(firstGalaxy.col - secondGalaxy.col);
                distance += Math.abs(binarySearch(emptyRows, firstGalaxy.row) - binarySearch(emptyRows, secondGalaxy.row)) * expansionFactor;
                distance += Math.abs(binarySearch(emptyCols, firstGalaxy.col) - binarySearch(emptyCols, secondGalaxy.col)) * expansionFactor;
                totalDistance += distance;
            }
        }
        return totalDistance;
    }

    // Record class for representing a galaxy
    record StellarObject(int row, int col) {
    }
}
