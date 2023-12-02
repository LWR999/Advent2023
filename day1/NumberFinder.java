// ------------------------------------------------------------
// Advent of Code 2023
// Day 1 - SnowCalibration
// Helper class that finds first and last numbers in a string
// (spellled or numerics)
// Leon Rees - 2 December 2023
// ------------------------------------------------------------
import java.util.Map;
import java.util.HashMap;

public class NumberFinder {
  private static final Map<String, Integer> numberWords = new HashMap<>();
  
  // Static block to initialize the mapping of spelled-out numbers to their numeric counterparts
  static {
    numberWords.put("zero", 0);
    numberWords.put("one", 1);
    numberWords.put("two", 2);
    numberWords.put("three", 3);
    numberWords.put("four", 4);
    numberWords.put("five", 5);
    numberWords.put("six", 6);
    numberWords.put("seven", 7);
    numberWords.put("eight", 8);
    numberWords.put("nine", 9);
  }

  // Method to find the first number in a string
  public static Integer findFirstNumber(String line) {
    StringBuilder currentWord = new StringBuilder(); // StringBuilder to accumulate letters
  
    // Iterate through each character in the line
    for (int i = 0; i < line.length(); i++) {
      char c = line.charAt(i);
  
      // If the character is a letter, append it to currentWord
      if (Character.isLetter(c)) {
        currentWord.append(c);
        String word = currentWord.toString().toLowerCase();
  
        // Check if the word matches or starts with any spelled-out number
        for (Map.Entry<String, Integer> entry : numberWords.entrySet()) {
          if (word.endsWith(entry.getKey())) {
            return entry.getValue(); // Return the corresponding number if match found
          }
        }
      } 
      // If the character is a digit and no spelled-out number is currently being built, return the digit
      else if (Character.isDigit(c)) {
          return Character.getNumericValue(c);
      }
    }
    return null; // Return null if no number is found
  }
  
  // Method to find the last number in a string
  public static Integer findLastNumber(String line) {
    StringBuilder currentWord = new StringBuilder(); // StringBuilder to accumulate letters in reverse
  
    // Iterate through the line in reverse
    for (int i = line.length() - 1; i >= 0; i--) {
      char c = line.charAt(i);
  
      // If the character is a letter, insert it at the beginning of currentWord
      if (Character.isLetter(c)) {
        currentWord.insert(0, c);
        String reversedWord = currentWord.toString().toLowerCase();
  
        // Check if the reversedWord starts with any spelled-out number
        for (Map.Entry<String, Integer> entry : numberWords.entrySet()) {
          if (reversedWord.startsWith(entry.getKey())) {
            return entry.getValue(); // Return the corresponding number if match found
          }
        }
      } 
      // If the character is a digit, check the length of currentWord
      else if (Character.isDigit(c)) {
        return Character.getNumericValue(c); // Return the digit
      }
    }
    return null; // Return null if no number is found
  }

}