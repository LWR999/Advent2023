// ------------------------------------------------------------
// Advent of Code 2023
// Day 8 - SandstormEscape
// Process the map to escape the sandstorm in ghost mode
// filePath = ./day8/ghost_map.txt
// Leon Rees - 10 December 2023
// ------------------------------------------------------------
package day8;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;

public class SandstormEscape {

    private static final int QUICK_MAP_FACTOR = 10000;

    public static void main(String[] args) {
        // File path for the input file
        String filePath = "./day8/ghost_map.txt"; // Update the path to your input file
        try {
            // Reading all lines from the file
            List<String> lines = Files.readAllLines(Paths.get(filePath));
            // Parsing the lines to extract map and instructions
            ParseResult parse = parse(lines);
            // Navigating the map based on instructions and printing the number of steps taken
            System.out.println("Took " + navigate(parse) + " steps!");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Method to navigate through the map based on instructions
    private static long navigate(ParseResult parse) {
        // Finding start and end indices based on nodes ending with 'A' and 'Z'
        List<Integer> startIndices = new ArrayList<>();
        List<Integer> endIndices = new ArrayList<>();
        for (int i = 0; i < parse.map.length; i++) {
            if (isStartIndex(i, parse.indexToNameMap)) startIndices.add(i);
            if (isEndIndex(i, parse.indexToNameMap)) endIndices.add(i);
        }

        // Creating a quick map for faster navigation
        List<Pair<List<Integer>, Integer>> quickMap = createQuickMap(parse, endIndices);

        // Finding the shortest path to the end
        long steps = 0L;
        List<Integer> currentIndices = new ArrayList<>(startIndices);
        while (true) {
            List<List<Integer>> allStepsToEndIndices = new ArrayList<>();
            List<Integer> allFinalIndices = new ArrayList<>();
            boolean valid = true;
            for (int k : currentIndices) {
                Pair<List<Integer>, Integer> quickMapEntry = quickMap.get(k);
                if (valid && quickMapEntry.getLeft().size() == 0) {
                    valid = false;
                }
                if (valid) {
                    allStepsToEndIndices.add(quickMapEntry.getLeft());
                }
                allFinalIndices.add(quickMapEntry.getRight());
            }

            if (valid) {
                int lowestIdenticalEntry = findLowestIdenticalEntry(allStepsToEndIndices);
                if (lowestIdenticalEntry >= 0) {
                    steps += lowestIdenticalEntry;
                    break;
                }
            }

            steps += (long) parse.instr.length * QUICK_MAP_FACTOR;
            for (int i = 0; i < currentIndices.size(); i++) {
                currentIndices.set(i, allFinalIndices.get(i));
            }
        }

        return steps;
    }

    // Method to create a quick map for navigation
    private static List<Pair<List<Integer>, Integer>> createQuickMap(ParseResult parse, List<Integer> endIndices) {
        List<Pair<List<Integer>, Integer>> res = new ArrayList<>();

        for (int i = 0; i < parse.map.length; i++) {
            int currIdx = i;
            List<Integer> stepsToReachableEndIndices = new ArrayList<>();
            if (endIndices.contains(currIdx)) stepsToReachableEndIndices.add(0);
            int finalIndex = 0;
            for (int j = 0; j < parse.instr.length * QUICK_MAP_FACTOR; j++) {
                char currInstr = parse.instr[j % parse.instr.length];
                if (currInstr == 'L') {
                    currIdx = parse.map[currIdx][0];
                } else if (currInstr == 'R') {
                    currIdx = parse.map[currIdx][1];
                }
                if (endIndices.contains(currIdx)) {
                    stepsToReachableEndIndices.add(j + 1);
                }
                finalIndex = currIdx;
            }
            res.add(Pair.of(stepsToReachableEndIndices, finalIndex));
        }

        return res;
    }

    // Method to check if an index corresponds to a start node (ends with 'A')
    private static boolean isStartIndex(int idx, Map<Integer, String> indexToNameMap) {
        return indexToNameMap.get(idx).endsWith("A");
    }

    // Method to check if an index corresponds to an end node (ends with 'Z')
    private static boolean isEndIndex(int idx, Map<Integer, String> indexToNameMap) {
        return indexToNameMap.get(idx).endsWith("Z");
    }

    // Method to find the lowest identical entry in a list of lists
    private static int findLowestIdenticalEntry(List<List<Integer>> lists) {
        List<Integer> masterList = new ArrayList<>();
        for (List<Integer> list : lists) {
            masterList.addAll(list);
        }
        masterList.sort(Integer::compare);

        int last = -1;
        int count = 0;
        for (int k : masterList) {
            if (k == last) {
                count++;
            } else {
                if (count == lists.size()) {
                    return last;
                }
                last = k;
                count = 1;
            }
        }

        return -1;
    }

    // Method to parse the input lines into a map and instructions
    private static ParseResult parse(List<String> lines) {
        char[] instr = lines.get(0).toCharArray();

        Map<String, Integer> nameToIndexMap = new HashMap<>();
        Map<Integer, String> indexToNameMap = new HashMap<>();
        int idxMax = 0;
        for (int i = 2; i < lines.size(); i++) {
            String name = lines.get(i).split(" = ")[0];
            nameToIndexMap.put(name, i - 2);
            indexToNameMap.put(i - 2, name);
            idxMax = i - 2;
        }

        int[][] map = new int[idxMax + 1][2];
        for (int i = 2; i < lines.size(); i++) {
            String edgesTuple = lines.get(i).split(" = ")[1];
            edgesTuple = edgesTuple.substring(1, edgesTuple.length() - 1);
            String[] edgesStr = edgesTuple.split(", ");

            int[] edges = new int[2];
            edges[0] = nameToIndexMap.get(edgesStr[0]);
            edges[1] = nameToIndexMap.get(edgesStr[1]);
            map[i - 2] = edges;
        }

        return new ParseResult(nameToIndexMap, indexToNameMap, instr, map);
    }

    static class ParseResult {
        Map<String, Integer> nameToIndexMap;
        Map<Integer, String> indexToNameMap;
        char[] instr;
        int[][] map;

        ParseResult(Map<String, Integer> nameToIndexMap, Map<Integer, String> indexToNameMap, char[] instr, int[][] map) {
            this.nameToIndexMap = nameToIndexMap;
            this.indexToNameMap = indexToNameMap;
            this.instr = instr;
            this.map = map;
        }
    }

    static class Pair<L, R> {
        private final L left;
        private final R right;

        private Pair(L left, R right) {
            this.left = left;
            this.right = right;
        }

        public static <L, R> Pair<L, R> of(L left, R right) {
            return new Pair<>(left, right);
        }

        public L getLeft() {
            return left;
        }

        public R getRight() {
            return right;
        }
    }
}
