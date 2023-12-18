// ------------------------------------------------------------
// Advent of Code 2023
// Day 13 - MirrorFinder
// Find perfect reflections in a map to determine where mirrors
// are
// filePath = ./day13/rock_ash_map.txt
// Leon Rees - 18 December 2023
// ------------------------------------------------------------
package day13;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

// MirrorFinder class to process input and calculate values based on specific rules
public class MirrorFinder {

    // Main method to drive the program
    public static void main(String[] args) throws IOException {
        long startTime = System.currentTimeMillis();

        // Setting up the reader for input file
        BufferedReader fileReader = new BufferedReader(new FileReader("./day13/rock_ash_map.txt"));
        String line;
        List<String> currentData = new ArrayList<>();
        List<MirrorPart> mirrorParts = new ArrayList<>();

        // Reading lines from the file and creating MirrorPart objects
        while ((line = fileReader.readLine()) != null) {
            if (line.isEmpty()) {
                mirrorParts.add(new MirrorPart(currentData));
                currentData.clear();
            } else {
                currentData.add(line);
            }
        }
        fileReader.close();
        mirrorParts.add(new MirrorPart(currentData));

        // Calculating and printing the sum of values
        long totalValue = 0;
        for (MirrorPart part : mirrorParts) {
            totalValue += part.computeValue();
        }
        System.out.println(totalValue);
        long part1Time = System.currentTimeMillis();
        System.out.printf("%5.3f sec\n\n", (part1Time - startTime) / 1000f);

        // Calculating and printing the sum of smudges
        long totalSmudge = 0;
        for (MirrorPart part : mirrorParts) {
            int smudgeValue = part.calculateSmudge();
            totalSmudge += smudgeValue;
            System.out.println(smudgeValue);
        }
        System.out.println(totalSmudge);
        long endTime = System.currentTimeMillis();
        System.out.printf("%5.3f sec\n", (endTime - part1Time) / 1000f);
    }

    // Inner class to represent a mirror part
    static class MirrorPart {
        boolean[][] pattern;
        long[] patternRows;
        long[] patternCols;

        // Constructor to initialize a MirrorPart with a given list of strings
        public MirrorPart(List<String> lines) {
            int rows = lines.size();
            int cols = lines.get(0).length();
            pattern = new boolean[rows][cols];

            // Converting the string pattern to a boolean array
            for (int i = 0; i < rows; i++) {
                String str = lines.get(i);
                for (int j = 0; j < str.length(); j++) {
                    pattern[i][j] = str.charAt(j) == '#';
                }
            }

            // Processing rows
            patternRows = new long[rows];
            for (int i = 0; i < rows; i++) {
                for (int j = cols - 1; j >= 0; j--) {
                    patternRows[i] = patternRows[i] << 1;
                    if (pattern[i][j]) {
                        patternRows[i] += 1;
                    }
                }
            }

            // Processing columns
            patternCols = new long[cols];
            for (int j = 0; j < cols; j++) {
                for (int i = rows - 1; i >= 0; i--) {
                    patternCols[j] = patternCols[j] << 1;
                    if (pattern[i][j]) {
                        patternCols[j] += 1;
                    }
                }
            }
        }

        // Method to compute value of a part
        public int computeValue() {
            int value = compute(patternCols, -1);
            if (value == -1) {
                value = compute(patternRows, -1) * 100;
            }
            if (value < 0) {
                throw new IllegalStateException("Invalid value computed");
            }
            return value;
        }

        // Method to calculate smudge of a part
        public int calculateSmudge() {
            int smudgeValue = smudge(patternCols, patternRows.length);
            if (smudgeValue == -1) {
                smudgeValue = smudge(patternRows, patternCols.length) * 100;
            }
            if (smudgeValue < 0) {
                throw new IllegalStateException("Invalid smudge value computed");
            }
            return smudgeValue;
        }

        // Helper method to calculate smudge
        private static int smudge(long[] array, int bitCount) {
            int smudgeBit = 1;
            int ignoreIndex = compute(array, -1);
            System.out.println(Arrays.toString(array) + " " + bitCount + " - " + ignoreIndex);
            for (int i = 0; i < bitCount; i++) {
                for (int j = 0; j < array.length; j++) {
                    array[j] ^= smudgeBit;
                    System.out.println(smudgeBit + " " + i + " " + j + Arrays.toString(array));
                    int value = compute(array, ignoreIndex);
                    if (value != -1) {
                        return value;
                    }
                    array[j] ^= smudgeBit;
                }
                smudgeBit <<= 1;
            }
            return -1;
        }

        // Helper method to compute value
        private static int compute(long[] array, int ignoreIndex) {
            out:
            for (int i = 1; i < array.length; i++) {
                int limit = Math.min(i, array.length - i);
                for (int j = 0; j < limit; j++) {
                    if (array[i - j - 1] != array[i + j]) {
                        continue out;
                    }
                }
                if (i != ignoreIndex) {
                    return i;
                }
            }
            return -1;
        }
    }
}
