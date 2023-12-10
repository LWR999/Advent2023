// ------------------------------------------------------------
// Advent of Code 2023
// Day 7 - CamelCards
// Rank the set of Camel Cards hands and calc total winnings
// filePath = ./day7/camel_cards_hands.txt
// Leon Rees - 10 December 2023
// ------------------------------------------------------------
package day7;
import java.io.*;
import java.util.*;

class CamelCards {
    static class Hand implements Comparable<Hand> {
        String handString;
        int bid;
        long handScore; // To store the calculated hand score

        Hand(String handString, int bid) {
            this.handString = handString;
            this.bid = bid;
            this.handScore = (determineHandType() * 10000000000L) + calculateHandScore(); // Calculate the hand score
        }

        private int determineHandType() {
            // Sort the hand string alphabetically
            char[] handArray = handString.toCharArray();
            Arrays.sort(handArray);
            String tempHandString = jokersHigh(new String(handArray));
            System.out.println("High : " + tempHandString);
        
            // Count the frequency of each card
            Map<Character, Integer> frequencyMap = new HashMap<>();
            for (char c : tempHandString.toCharArray()) {
                frequencyMap.put(c, frequencyMap.getOrDefault(c, 0) + 1);
            }        
            // Standard hand type determination
            return determineStandardHandType(frequencyMap);
        }
        
      
        private String jokersHigh(String handString) {
            if (!handString.contains("J")) {
                return handString; // No jokers to replace
            }
        
            // Count the frequency of each non-Joker card
            Map<Character, Integer> frequencyMap = new HashMap<>();
            for (char c : handString.toCharArray()) {
                if (c != 'J') {
                    frequencyMap.put(c, frequencyMap.getOrDefault(c, 0) + 1);
                }
            }
        
            // Find the most frequent card, preferring the highest-ranking card in case of a tie
            char mostFrequentCard = '2'; // Start with the lowest card
            int maxFrequency = 0;
            for (Map.Entry<Character, Integer> entry : frequencyMap.entrySet()) {
                char currentCard = entry.getKey();
                int currentFrequency = entry.getValue();
                if (currentFrequency > maxFrequency || (currentFrequency == maxFrequency && "A23456789TJQK".indexOf(currentCard) > "A23456789TJQK".indexOf(mostFrequentCard))) {
                    mostFrequentCard = currentCard;
                    maxFrequency = currentFrequency;
                }
            }
        
            // Replace all Jokers with the most frequent card
            return handString.replace('J', mostFrequentCard);
        }
        
         
        private int determineStandardHandType(Map<Character, Integer> frequencyMap) {
            boolean hasThree = false;
            boolean hasTwo = false;
            int maxFrequency = Collections.max(frequencyMap.values());
        
            if (maxFrequency == 5) {
                return 6; // Five of a Kind
            } else if (maxFrequency == 4) {
                return 5; // Four of a Kind
            } else if (maxFrequency == 3) {
                hasThree = true;
            }
        
            for (int freq : frequencyMap.values()) {
                if (freq == 2) {
                    if (hasThree) {
                        return 4; // Full House
                    }
                    if (hasTwo) {
                        return 2; // Two Pairs
                    }
                    hasTwo = true;
                }
            }
        
            if (hasThree) {
                return 3; // Three of a Kind
            }
            if (hasTwo) {
                return 1; // One Pair
            }
            return 0; // High Card
        }
        
        private long calculateHandScore() {
            long score = 0;
            for (int i = 0; i < handString.length(); i++) {
                char card = handString.charAt(i);
                int cardValue = card == 'J' ? 1 : "23456789TQKA".indexOf(card) + 2; // J is the weakest
                score += cardValue * Math.pow(10, (4 - i) * 2);
            }
            return score;
        }

        @Override
        public int compareTo(Hand other) {
            return Long.compare(this.handScore, other.handScore);
        }
    }

    public static void main(String[] args) {
        String filePath = "./day7/camel_cards_hands.txt";
        List<Hand> hands = readHandsFromFile(filePath);

        System.out.println("Total Winnings: " + totalWinnings(hands));
    }

    private static List<Hand> readHandsFromFile(String filePath) {
        List<Hand> hands = new ArrayList<>();
        try (Scanner scanner = new Scanner(new File(filePath))) {
            while (scanner.hasNextLine()) {
                String[] parts = scanner.nextLine().split(" ");
                String handStr = parts[0];
                int bid = Integer.parseInt(parts[1]);
                Hand handForRow = new Hand(handStr, bid);
                hands.add(handForRow);
                System.out.println("Hand: " + handForRow.handString + " - Bid: " + handForRow.bid + " - Score :" + handForRow.handScore);
            }
        } catch (FileNotFoundException e) {
            System.out.println("File not found: " + e.getMessage());
        }
        return hands;
    }

    private static int totalWinnings(List<Hand> hands) {
        Collections.sort(hands); // Sorts hands based on handScore in ascending order

        int total = 0;
        for (int i = 0; i < hands.size(); i++) {
            int rank = i + 1; // Rank is determined by the index in the sorted list
            total += hands.get(i).bid * rank;
            System.out.println("Hand: " + hands.get(i).handString + " - Score: " + hands.get(i).handScore + " - Bid: " + hands.get(i).bid + " - Rank: " + rank);
        }
        return total;
    }
}
