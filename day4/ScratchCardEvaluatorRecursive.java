// ------------------------------------------------------------
// Advent of Code 2023
// Day 4 - ScratchCardEvaluator
// Calculate the total points of the scratch card
// (recursive version)
// Input data : ./day4/scratch_cards_data.txt
// Leon Rees - 4 December 2023
// ------------------------------------------------------------
package day4;

import java.io.*;
import java.util.*;

public class ScratchCardEvaluatorRecursive {

    // Class to represent a scratch card
    static class ScratchCard {
        int[] winningNumbers;
        int[] yourNumbers;

        ScratchCard(int[] winningNumbers, int[] yourNumbers) {
            this.winningNumbers = winningNumbers;
            this.yourNumbers = yourNumbers;
        }
    }

    private static List<ScratchCard> cards;

    public static void main(String[] args) {
        try {
            cards = readCardsFromFile("./day4/scratch_cards_data.txt");
            int totalCards = processAllScratchCards();
            System.out.println("Total scratchcards: " + totalCards);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Read card data from a file
    private static List<ScratchCard> readCardsFromFile(String fileName) throws IOException {
        List<ScratchCard> cards = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(fileName))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(":")[1].trim().split("\\|");
                int[] winningNumbers = parseNumbers(parts[0]);
                int[] yourNumbers = parseNumbers(parts[1]);
                cards.add(new ScratchCard(winningNumbers, yourNumbers));
            }
        }
        return cards;
    }

    // Parse numbers from a string
    private static int[] parseNumbers(String numbers) {
        return Arrays.stream(numbers.trim().split(" "))
                 .filter(str -> !str.isEmpty())
                 .mapToInt(Integer::parseInt)
                 .toArray();
    }

    // Process all scratch cards and calculate the total number including copies
    private static int processAllScratchCards() {
        int totalCards = 0;
        for (int i = 0; i < cards.size(); i++) {
            totalCards += processCardAndCopies(i, 1);
        }
        return totalCards;
    }

    // Recursive method to process a scratch card and its copies
    private static int processCardAndCopies(int cardIndex, int copies) {
        if (cardIndex >= cards.size()) {
            return 0;
        }

        ScratchCard card = cards.get(cardIndex);
        int matches = countMatches(card);
        int totalCopies = copies;

        // Process each copy of the current card
        for (int i = 0; i < copies; i++) {
            // For each copy, add the won copies of the subsequent cards
            for (int j = 1; j <= matches; j++) {
                totalCopies += processCardAndCopies(cardIndex + j, 1);
            }
        }

        return totalCopies;
    }

    // Count the number of matches in a card
    private static int countMatches(ScratchCard card) {
        Set<Integer> winningNumbersSet = new HashSet<>();
        for (int number : card.winningNumbers) {
            winningNumbersSet.add(number);
        }

        int matches = 0;
        for (int number : card.yourNumbers) {
            if (winningNumbersSet.contains(number)) {
                matches++;
            }
        }
        return matches;
    }
}
