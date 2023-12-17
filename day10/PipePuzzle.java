// ------------------------------------------------------------
// Advent of Code 2023
// Day 10 - PipePuzzle
// DFS to find the longest loop in the pipe map
// filePath = ./day10/puzzle_map.txt
// Leon Rees - 17 December 2023
// ------------------------------------------------------------
package day10;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;

// Renamed class from Day10 to PipePuzzle
public class PipePuzzle {

    // Inner class representing a Pipe with exits
    static class Pipe {
        char[] exits; // Array to store the exits of the pipe

        // Constructor to initialize the pipe with given exits
        public Pipe(char[] exits) {
            this.exits = exits;
        }

        // Method to get the opposite exit given one exit
        public char getOtherExit(char exit) {
            for (char c : exits) {
                if (c != exit) return c;
            }
            return 0; // Return null character if no other exit is found
        }

        // Method to check if the pipe has a specific exit
        public boolean hasExit(char s) {
            for (char c : exits) {
                if (c == s) return true;
            }
            return false;
        }
    }

    // Inner class representing a Tile in the puzzle
    static class Tile {
        public char c; // Character representation of the Tile
        public int x, y; // Coordinates of the Tile
        public int count = 0; // Used for tracking purposes
        public char mark = ' '; // Used to mark the Tile during processing

        // Constructor to initialize the Tile
        public Tile(char c, int x, int y) {
            this.c = c;
            this.x = x;
            this.y = y;
        }

        // Overridden toString method for Tile
        @Override
        public String toString() {
            return "(" + x + "," + y + ")\t" + c + "\t" + count;
        }
    }

    // Inner class representing a Point in 2D space
    static class Point {
        int x, y; // Coordinates of the Point

        // Constructor to initialize the Point
        public Point(int x, int y) {
            this.x = x;
            this.y = y;
        }
    }

    private final List<Point> polygon = new ArrayList<>(); // List to store points forming a polygon

    private final Map<Character, Pipe> pipes = new HashMap<>(); // Map to store pipes identified by their character representation
    private Tile[][] tiles; // 2D array of Tiles
    private int startX, startY; // Starting coordinates
    private final char[] exits = {'N', 'E', 'S', 'W'}; // Array of possible exits (North, East, South, West)
    int maxSteps = 0; // Variable to store the maximum number of steps

    // Method to find the second answer
    private void findAnswer2() {
        List<Tile> toTest = new ArrayList<>();
        // Find all tiles with count == 0 except the start tile
        for (int y = 0; y < this.tiles.length; ++y) {
            for (int x = 0; x < this.tiles.length; ++x) {
                if (this.tiles[y][x].count == 0 && !(x == startX && y == startY)) {
                    toTest.add(this.tiles[y][x]);
                }
            }
        }

        int insideCount = 0; // Counter for tiles inside the polygon
        int outsideCount = 0; // Counter for tiles outside the polygon
        for (Tile t : toTest) {
            if (isInside(t.x, t.y)) {
                t.mark = 'I'; // Mark the tile as inside
                insideCount++;
            } else {
                outsideCount++;
                t.mark = 'O'; // Mark the tile as outside
            }
        }
        printTiles(); // Print the tiles after processing
        System.out.println("insideCount = " + insideCount);
        System.out.println("outsideCount = " + outsideCount);
    }

    // Method to check if a point x, y is inside the closed loop of points in the polygon
    private boolean isInside(int x, int y) {
        // Implementation of the ray-casting algorithm to determine if a point is inside a polygon
        boolean c = false;
        int i, j;
        for (i = 0, j = polygon.size() - 1; i < polygon.size(); j = i++) {
            Point a = polygon.get(i);
            Point b = polygon.get(j);
            if (((a.y > y) != (b.y > y)) &&
                    (x < (b.x - a.x) * (y - a.y) / (b.y - a.y) + a.x)) {
                c = !c;
            }
        }
        return c;
    }

    // Method to print the current state of tiles
    private void printTiles() {
        for (int y = 0; y < tiles.length; ++y) {
            for (int x = 0; x < tiles.length; ++x) {
                if (tiles[y][x] == null) {
                    System.out.print(' ');
                    continue;
                }
                System.out.print(tiles[y][x].mark);
            }
            System.out.println();
        }
    }

    // Method to find the first answer
    private void findAnswer1() {
        Tile start = findStart(); // Find the start tile
        assert (start != null); // Ensure start tile is not null

        polygon.add(new Point(start.x, start.y)); // Add the start point to the polygon

        // Iterate through all exits to find a connecting pipe
        Tile t;
        for (char exit : exits) {
            t = getPipeAt(start.x, start.y, exit);
            if (t == null || t.c == '.' || t.count != 0) continue;
            char reverse = getReverseDirection(exit);
            if (pipes.get(t.c).hasExit(reverse)) {
                t.count = 1;
                polygon.add(new Point(t.x, t.y));
                System.out.print("Stepping " + exit + " ");
                followPipe(start, t, reverse); // Follow the pipe recursively
            }
        }
    }

    // Method to find the start tile, which is marked with 'S'
    private Tile findStart() {
        for (startY = 0; startY < tiles.length; ++startY) {
            for (startX = 0; startX < tiles.length; ++startX) {
                if (tiles[startY][startX].c == 'S') {
                    System.out.println("start at " + tiles[startY][startX]);
                    return tiles[startY][startX];
                }
            }
        }
        return null; // Return null if no start tile is found
    }

    // Method to follow a pipe recursively
    private void followPipe(Tile start, Tile t, char entryFrom) {
        Tile next;
        do {
            if (t == null || t.c == '.') {
                break;
            }
            System.out.println(t);
            char exit = pipes.get(t.c).getOtherExit(entryFrom);
            System.out.print("Stepping " + exit + " ");
            next = getPipeAt(t.x, t.y, exit);
            if (next == null) {
                System.out.println("no pipe");
                break;
            }
            if (next == start) {
                maxSteps = t.count + 1;
                System.out.println("found start. " + (maxSteps / 2) + " half size");
                break;
            }
            next.count = t.count + 1;
            entryFrom = getReverseDirection(exit);
            t = next;
            polygon.add(new Point(t.x, t.y));
        } while (true);
    }

    // Method to get a tile at a specified location based on the direction of exit
    private Tile getPipeAt(int x, int y, char exit) {
        return switch (exit) {
            case 'N' -> findTile(x, y - 1);
            case 'E' -> findTile(x + 1, y);
            case 'S' -> findTile(x, y + 1);
            case 'W' -> findTile(x - 1, y);
            default -> null;
        };
    }

    // Method to get the reverse direction given an exit
    private char getReverseDirection(char exit) {
        return switch (exit) {
            case 'N' -> 'S';
            case 'E' -> 'W';
            case 'S' -> 'N';
            case 'W' -> 'E';
            default -> 0;
        };
    }

    // Method to find a tile in the grid based on x and y coordinates
    private Tile findTile(int x, int y) {
        // Return null if coordinates are outside the grid bounds
        if (x < 0 || y < 0 || x >= tiles.length || y >= tiles.length) return null;
        // Return the tile at the given coordinates
        return tiles[y][x];
    }

    boolean firstLine = true;
    int yy = 0;

    // Process each line of the input
    private void processLine(String line) {
        // Skip empty lines
        if (line.trim().isEmpty()) return;

        // Initialize tiles array on the first line
        if (firstLine) {
            firstLine = false;
            int size = line.length();
            tiles = new Tile[size][];
            for (int i = 0; i < size; ++i) {
                tiles[i] = new Tile[size];
            }
        }

        // Convert line characters to tiles
        int x = 0;
        for (char c : line.toCharArray()) {
            tiles[yy][x] = new Tile(c, x, yy);
            x++;
        }
        yy++;
    }

    // Process the input file to set up the puzzle
    private void processFile() {
        // Initialize pipes with a mapping of characters to exits
        initializePipes();
    
        // Read all lines from the input file at once
        try {
            // Adjust the path as necessary for your file location
            List<String> allLines = Files.readAllLines(Paths.get("./day10/puzzle_map.txt"));
    
            // Process each line
            for (String line : allLines) {
                processLine(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Separate method to initialize the pipes
    private void initializePipes() {
        // A compact way to initialize all pipe types and their exits
        pipes.put('|', new Pipe(new char[]{'N', 'S'}));
        pipes.put('-', new Pipe(new char[]{'E', 'W'}));
        pipes.put('7', new Pipe(new char[]{'W', 'S'}));
        pipes.put('L', new Pipe(new char[]{'N', 'E'}));
        pipes.put('F', new Pipe(new char[]{'S', 'E'}));
        pipes.put('J', new Pipe(new char[]{'W', 'N'}));
    }    

    public static void main(String[] args) {
        // Create an instance of the puzzle and solve it
        PipePuzzle me = new PipePuzzle();
        me.processFile();
        long t0 = System.nanoTime();
        me.findAnswer1();
        long t1 = System.nanoTime();
        me.findAnswer2();
        long t2 = System.nanoTime();
        // Print the time taken for each solution part
        System.out.println("time 1 = " + (t1 - t0) / 1000000 + "ms");
        System.out.println("time 2 = " + (t2 - t1) / 1000000 + "ms");
    }
}