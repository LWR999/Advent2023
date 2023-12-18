// ------------------------------------------------------------
// Advent of Code 2023
// Day 12 - SpringMaintenance
// Reconstruct accurate working map of the hotsprings
// filePath = ./day12/spring_map.txt
// Leon Rees - 18 December 2023
// ------------------------------------------------------------
package day12;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

// Renamed class from Day12 to SpringMaintenance
public class SpringMaintenance {
    public static void main(String[] args) throws IOException {
        // Track the start time for performance measurement
        long startTime = System.currentTimeMillis();

        // Initialize BufferedReader to read from file
        BufferedReader bufferedReader = new BufferedReader(new FileReader("./day12/spring_map.txt"));

        String line;
        List<GardenState> primaryStates = new ArrayList<>();
        List<GardenState> expandedStates = new ArrayList<>();
        while ((line = bufferedReader.readLine()) != null) {
            String[] parts = line.split(" ");
            List<Integer> indices = new ArrayList<>();
            for (String numberString : parts[1].split(",")) {
                indices.add(Integer.parseInt(numberString));
            }
            String simplePattern = parts[0] + ".";
            primaryStates.add(new GardenState(simplePattern.toCharArray(), indices));

            String complexPattern = parts[0] + "?" + parts[0] + "?" + parts[0] + "?" + parts[0] + "?" + parts[0] + ".";
            List<Integer> expandedIndices = new ArrayList<>();
            for (int i = 0; i < 5; i++) {
                expandedIndices.addAll(indices);
            }
            expandedStates.add(new GardenState(complexPattern.toCharArray(), expandedIndices));
        }
        bufferedReader.close();

        long totalPossibilities = calculateTotalPossibilities(primaryStates);
        System.out.println(totalPossibilities);
        long primaryStateTime = System.currentTimeMillis();
        System.out.printf("%5.3f sec\n\n", (primaryStateTime - startTime) / 1000f);

        totalPossibilities = calculateTotalPossibilities(expandedStates);
        long endTime = System.currentTimeMillis();
        System.out.println(totalPossibilities);
        System.out.printf("%5.3f sec\n", (endTime - primaryStateTime) / 1000f);
    }

    // Method to calculate total possibilities for a list of GardenState
    private static long calculateTotalPossibilities(List<GardenState> states) {
        long sum = 0;
        for (GardenState state : states) {
            sum += state.countPossibilities();
        }
        return sum;
    }

    // State class
    static class GardenState {
        private final char[] pattern;
        private final int patternIndex;
        private final List<Integer> values;
        private final int valueIndex;
        private final int currentGroup;
        private final HashMap<GardenState, Long> memoizationCache;

        public GardenState(char[] pattern, List<Integer> values) {
            this(pattern, values, 0, 0, 0, new HashMap<>());
        }

        private GardenState(char[] pattern, List<Integer> values, int currentGroup, int patternIndex, int valueIndex, HashMap<GardenState, Long> memoizationCache) {
            this.pattern = pattern;
            this.values = values;
            this.currentGroup = currentGroup;
            this.patternIndex = patternIndex;
            this.valueIndex = valueIndex;
            this.memoizationCache = memoizationCache;
        }

        public long countPossibilities() {
            if (memoizationCache.containsKey(this)) {
                return memoizationCache.get(this);
            }
            long result = -1;
            if (patternIndex == pattern.length) {
                result = determineEndStateValidity();
            } else {
                result = evaluateCurrentState();
            }
            if (result < 0) {
                throw new IllegalStateException("Invalid state encountered");
            }
            memoizationCache.put(this, result);
            return result;
        }

        private long determineEndStateValidity() {
            if (currentGroup == 0 && valueIndex == values.size()) {
                return 1; // Valid end state
            } else {
                return 0; // Invalid end state
            }
        }

        private long evaluateCurrentState() {
            if (pattern[patternIndex] == '.') {
                return stepOnEmpty();
            } else if (pattern[patternIndex] == '#') {
                return stepOnSpring();
            } else if (pattern[patternIndex] == '?') {
                return stepOnSpring() + stepOnEmpty();
            }
            return -1;
        }

        private long stepOnSpring() {
            if (valueIndex == values.size() || currentGroup >= values.get(valueIndex)) {
                return 0;
            } else {
                return new GardenState(pattern, values, currentGroup + 1, patternIndex + 1, valueIndex, memoizationCache).countPossibilities();
            }
        }

        private long stepOnEmpty() {
            if (currentGroup > 0) {
                if (values.get(valueIndex) == currentGroup) {
                    return new GardenState(pattern, values, 0, patternIndex + 1, valueIndex + 1, memoizationCache).countPossibilities();
                } else {
                    return 0;
                }
            } else {
                return new GardenState(pattern, values, 0, patternIndex + 1, valueIndex, memoizationCache).countPossibilities();
            }
        }

        @Override
        public boolean equals(Object other) {
            if (this == other) return true;
            if (!(other instanceof GardenState otherState)) return false;

            if (patternIndex != otherState.patternIndex) return false;
            if (valueIndex != otherState.valueIndex) return false;
            return currentGroup == otherState.currentGroup;
        }

        @Override
        public int hashCode() {
            int result = patternIndex;
            result = 31 * result + valueIndex;
            result = 31 * result + currentGroup;
            return result;
        }
    }
}
