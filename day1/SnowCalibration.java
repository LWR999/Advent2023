// ------------------------------------------------------------
// Advent of Code 2023
// Day 1 - SnowCalibration
// Pull the trebuchet calibration values from a text file
// Leon Rees - 2 December 2023
// ------------------------------------------------------------

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class SnowCalibration {
  public static void main(String[] args) {
    File file = new File("./day1/calibration_file.txt");
    int sum = 0;

    try (Scanner scanner = new Scanner(file)) {
      while (scanner.hasNextLine()) {
        String line = scanner.nextLine();

        // Replace spelled-out numbers with digits
        int firstDigit = -1, lastDigit = -1;
        firstDigit  = NumberFinder.findFirstNumber(line);
        lastDigit   = NumberFinder.findLastNumber(line);

        System.out.println(line + " converts to " + firstDigit + lastDigit);

        // Combine them to form a two-digit number and add to the sum
        if (firstDigit != -1 && lastDigit != -1) {
          sum += firstDigit * 10 + lastDigit;
        }
        System.out.println("Calibration: " + (firstDigit * 10 + lastDigit));
      }
    } catch (FileNotFoundException e) {
      System.out.println("File not found: " + file.getAbsolutePath());
    }

    System.out.println("Total calibration value: " + sum);
  }

}  