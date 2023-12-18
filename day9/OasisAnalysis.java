// ------------------------------------------------------------
// Advent of Code 2023
// Day 9 - OasisAnalysis
// Process the map to escape the sandstorm in ghost mode
// filePath = ./day9/environment_readings.txt
// Leon Rees - 11 December 2023
// ------------------------------------------------------------
package day9;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class OasisAnalysis {
    public static void main(String[] args) throws IOException {
        // Record the start time for performance measurement
        long startTime = System.currentTimeMillis();

        // Setup a BufferedReader to read from a file
        BufferedReader br = new BufferedReader(new FileReader("./day9/environment_readings.txt"));

        // Initialize variables to store lines and Node objects
        String s;
        List<Node> firstNodes = new ArrayList<>();
        List<Node> lastNodes = new ArrayList<>();

        // Read the file line by line
        while ((s = br.readLine()) != null) {
            Node prev = null;
            // Split each line into space-separated values and create Node objects
            for (String n : s.split(" ")) {
                Node nn = new Node(Long.parseLong(n), prev);
                // For the first node in a line, add it to firstNodes list
                if (prev == null) {
                    firstNodes.add(nn);
                }
                prev = nn;
            }
            // Add the last node of each line to lastNodes list
            lastNodes.add(prev);
        }
        br.close();
        
        // Calculate a value (p2) based on the first nodes of each line
        long p2 = 0;
        for (Node n : firstNodes) {
            n.build(null);
            p2 += n.predictLeft();
        }

        // Calculate another value (p1) based on the last nodes of each line
        long p1 = 0;
        for (Node n : lastNodes) {
            p1 += n.predictRight();
        }

        // Record the end time and calculate the total time taken
        long endTime = System.currentTimeMillis();
        System.out.printf("%5.3f sec\n", (endTime - startTime) / 1000f);
        System.out.println(p1);
        System.out.println(p2);
    }

    // Inner class representing a node in the data structure
    private static class Node {
        private long value;
        private Node left;
        private Node right;
        private Node bottom;

        // Constructor for Node
        public Node(long value, Node left) {
            this.value = value;
            this.left = left;
            // Link this node to the left node's right
            if (left != null) {
                left.right = this;
            }
        }

        // Build method to create and connect nodes
        public void build(Node lowerLeft) {
            if (right != null) {
                bottom = new Node(right.value - value, lowerLeft);
                right.build(bottom);
                // Check for a condition based on values of the nodes
                if (left == null) {
                    boolean allZeros = true;
                    Node n = bottom;
                    while (n != null) {
                        if (n.value != 0) {
                            allZeros = false;
                            break;
                        }
                        n = n.right;
                    }
                    if (!allZeros) {
                        bottom.build(null);
                    }
                }
            }
        }

        // Predict a value moving to the right in the node chain
        public long predictRight() {
            if (right != null) {
                throw new IllegalStateException();
            }
            if (left.bottom == null) {
                return 0;
            }
            return value + left.bottom.predictRight();
        }

        // Predict a value moving to the left in the node chain
        public long predictLeft() {
            if (left != null) {
                throw new IllegalStateException();
            }
            if (bottom == null) {
                return 0;
            }
            return value - bottom.predictLeft();
        }

        // Override toString for better visualization of Node objects
        @Override
        public String toString() {
            StringBuilder sb = new StringBuilder();
            sb.append(value);
            Node n = right;
            while (n != null) {
                sb.append(" ");
                sb.append(n.value);
                n = n.right;
            }
            if (bottom != null) {
                sb.append("\n");
                sb.append(bottom);
            }
            return sb.toString();
        }
    }
}
