// ------------------------------------------------------------
// Advent of Code 2023
// Day 14 - DishFocusController_Part2
// Spin the rocks a stupid amount of times to align the mirrors
// filePath = ./day14/mirror_map.txt
// Leon Rees - 18 December 2023
// ------------------------------------------------------------
package day14;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class DishFocusController_Part2 {

    private static int numberOfRows;
    private static int numberOfColumns;

    public static void main(String[] args) throws IOException {
        long startTime = System.currentTimeMillis();
        BufferedReader bufferedReader = new BufferedReader(new FileReader("./day14/mirror_map.txt"));

        List<ArrayList<PlatformState>> platform = new ArrayList<>();
        String currentLine;
        while ((currentLine = bufferedReader.readLine()) != null) {
            ArrayList<PlatformState> row = new ArrayList<>();
            for (char ch : currentLine.toCharArray()) {
                row.add(PlatformState.determineState(ch));
            }
            platform.add(row);
        }
        bufferedReader.close();

        numberOfRows = platform.size();
        numberOfColumns = platform.get(0).size();

        PlatformManipulator manipulator = new PlatformManipulator(platform);

        manipulator.tiltNorth();
        long initialLoad = manipulator.calculateLoad();
        System.out.println("Initial load: " + initialLoad);
        long timeAfterFirstPart = System.currentTimeMillis();
        System.out.printf("Time for first part: %5.3f sec\n\n", (timeAfterFirstPart - startTime) / 1000f);

        manipulator.tiltWest();
        manipulator.tiltSouth();
        manipulator.tiltEast();
        manipulator.displayPlatform();

        final long targetCycles = 1000000000L;
        long previousTime = System.currentTimeMillis();
        HashMap<List<ArrayList<PlatformState>>, Long> seenConfigurations = new HashMap<>();
        HashMap<Long, Long> loadPerCycle = new HashMap<>();
        long startOfCycle = -1;
        long endOfCycle = -1;

        for (long cycle = 2; cycle < targetCycles; cycle++) {
            if (cycle % 10000000 == 0) {
                long currentTime = System.currentTimeMillis();
                System.out.printf("%4.2f%% completed in %5.3f sec\n", 100d * cycle / targetCycles, (currentTime - previousTime) / 1000f);
                previousTime = currentTime;
            }

            manipulator.runCycle();
            List<ArrayList<PlatformState>> currentState = manipulator.getPlatformCopy();

            if (seenConfigurations.containsKey(currentState)) {
                startOfCycle = seenConfigurations.get(currentState);
                endOfCycle = cycle - 1;
                break;
            }

            seenConfigurations.put(currentState, cycle);
            loadPerCycle.put(cycle, manipulator.calculateLoad());
        }

        long cycleLength = endOfCycle - startOfCycle + 1;
        System.out.println("Repeating pattern found from cycle " + startOfCycle + " to " + endOfCycle + " (length: " + cycleLength + ")");
        long targetCycle = startOfCycle + (targetCycles % cycleLength);
        System.out.println("Total load after " + targetCycles + " cycles: " + loadPerCycle.get(targetCycle));

        long endTime = System.currentTimeMillis();
        System.out.printf("Total time: %5.3f sec\n", (endTime - timeAfterFirstPart) / 1000f);
    }

    private static class PlatformManipulator {
        private List<ArrayList<PlatformState>> platform;
    
        public PlatformManipulator(List<ArrayList<PlatformState>> platform) {
            this.platform = platform;
        }
    
        public void displayPlatform() {
            for (List<PlatformState> row : platform) {
                row.forEach(state -> System.out.print(state.characterRepresentation));
                System.out.println();
            }
            System.out.println();
        }
    
        public long calculateLoad() {
            long totalLoad = 0;
            int currentWeight = numberOfRows;
            for (List<PlatformState> row : platform) {
                long countOfRoundRocks = row.stream().filter(state -> state == PlatformState.ROUND).count();
                totalLoad += countOfRoundRocks * currentWeight;
                currentWeight--;
            }
            return totalLoad;
        }
    
        public void tiltNorth() {
            for (int col = 0; col < numberOfColumns; col++) {
                for (int row = 0; row < numberOfRows; row++) {
                    if (platform.get(row).get(col) == PlatformState.ROUND) {
                        int newRow = row;
                        while (newRow > 0 && platform.get(newRow - 1).get(col) == PlatformState.EMPTY) {
                            newRow--;
                        }
                        if (newRow != row) {
                            platform.get(row).set(col, PlatformState.EMPTY);
                            platform.get(newRow).set(col, PlatformState.ROUND);
                        }
                    }
                }
            }
        }
    
        public void tiltSouth() {
            for (int col = 0; col < numberOfColumns; col++) {
                for (int row = numberOfRows - 1; row >= 0; row--) {
                    if (platform.get(row).get(col) == PlatformState.ROUND) {
                        int newRow = row;
                        while (newRow < numberOfRows - 1 && platform.get(newRow + 1).get(col) == PlatformState.EMPTY) {
                            newRow++;
                        }
                        if (newRow != row) {
                            platform.get(row).set(col, PlatformState.EMPTY);
                            platform.get(newRow).set(col, PlatformState.ROUND);
                        }
                    }
                }
            }
        }
    
        public void tiltWest() {
            for (int row = 0; row < numberOfRows; row++) {
                List<PlatformState> currentRow = platform.get(row);
                for (int col = 0; col < numberOfColumns; col++) {
                    if (currentRow.get(col) == PlatformState.ROUND) {
                        int newCol = col;
                        while (newCol > 0 && currentRow.get(newCol - 1) == PlatformState.EMPTY) {
                            newCol--;
                        }
                        if (newCol != col) {
                            currentRow.set(col, PlatformState.EMPTY);
                            currentRow.set(newCol, PlatformState.ROUND);
                        }
                    }
                }
            }
        }
    
        public void tiltEast() {
            for (int row = 0; row < numberOfRows; row++) {
                List<PlatformState> currentRow = platform.get(row);
                for (int col = numberOfColumns - 1; col >= 0; col--) {
                    if (currentRow.get(col) == PlatformState.ROUND) {
                        int newCol = col;
                        while (newCol < numberOfColumns - 1 && currentRow.get(newCol + 1) == PlatformState.EMPTY) {
                            newCol++;
                        }
                        if (newCol != col) {
                            currentRow.set(col, PlatformState.EMPTY);
                            currentRow.set(newCol, PlatformState.ROUND);
                        }
                    }
                }
            }
        }
    
        public void runCycle() {
            tiltNorth();
            tiltWest();
            tiltSouth();
            tiltEast();
        }
    
        public List<ArrayList<PlatformState>> getPlatformCopy() {
            List<ArrayList<PlatformState>> copy = new ArrayList<>();
            for (ArrayList<PlatformState> row : platform) {
                copy.add(new ArrayList<>(row));
            }
            return copy;
        }
    }
    

    enum PlatformState {
        EMPTY('.'), ROUND('O'), SQUARE('#');

        private final char characterRepresentation;

        PlatformState(char characterRepresentation) {
            this.characterRepresentation = characterRepresentation;
        }

        static PlatformState determineState(char character) {
            for (PlatformState state : values()) {
                if (state.characterRepresentation == character) {
                    return state;
                }
            }
            throw new IllegalArgumentException("Unknown platform state character: " + character);
        }
    }
}
