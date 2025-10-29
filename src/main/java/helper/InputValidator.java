package helper;

import java.util.InputMismatchException;
import java.util.Scanner;
import java.util.Set;

public class InputValidator {

    public static int getValidIdFromUser(Scanner scanner, Set<Integer> idSet, String prompt) {
        int idToUpdate;
        while (true) {
            System.out.print(prompt);

            try {
                idToUpdate = scanner.nextInt();
                scanner.nextLine();

                if (idToUpdate == 0) {
                    return 0;
                }

                if (idSet.contains(idToUpdate)) {
                    break; // valid IdOperation, exit loop
                } else {
                    System.out.println("Invalid IdOperation. Try again.");
                }

            } catch (InputMismatchException e) {
                System.out.println("Enter a number.");
                scanner.nextLine(); // clear invalid input
            }
        }
        return idToUpdate;
    }

    public static int getValidInput(Scanner scanner, int min, int max, String prompt) {
        int val;
        do {
            System.out.print(prompt);
            try {
                val = scanner.nextInt();
                scanner.nextLine();

                if (val >= min && val <= max) {
                    return val;
                } else {
                    System.out.println("Choose between " + min + " and " + max +".");
                }
            } catch (InputMismatchException exception) {
                System.out.println("Enter a number.");
                scanner.nextLine();
            }
        } while (true);
    }
}
