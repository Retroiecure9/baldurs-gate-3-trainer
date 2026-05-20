package com.baldursgate3.trainer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Scanner;

/**
 * Command-line interface for the Baldur's Gate 3 Trainer.
 * Allows the user to interactively modify game values.
 */
public class TrainerCLI {

    private static final Logger logger = LoggerFactory.getLogger(TrainerCLI.class);

    /**
     * Entry point for the trainer CLI.
     *
     * @param args command-line arguments (unused)
     */
    public static void main(String[] args) {
        System.out.println("=== Baldur's Gate 3 Trainer ===");
        System.out.println("For educational purposes only.");

        // In a real scenario, you'd find the BG3 process ID automatically.
        // Here we use a placeholder.
        int processId = 12345; // Dummy PID

        try (MemoryScanner scanner = new MemoryScanner(processId)) {
            TrainerFeatures features = new TrainerFeatures(scanner);
            Scanner input = new Scanner(System.in);

            while (true) {
                System.out.println("\nOptions:");
                System.out.println("1. Set Health");
                System.out.println("2. Set Gold");
                System.out.println("3. Set Action Points");
                System.out.println("4. Exit");
                System.out.print("Choice: ");

                String choice = input.nextLine().trim();
                switch (choice) {
                    case "1" -> {
                        System.out.print("Enter health (0-9999): ");
                        int health = Integer.parseInt(input.nextLine());
                        if (features.setHealth(health)) {
                            System.out.println("Health set successfully.");
                        } else {
                            System.out.println("Failed to set health.");
                        }
                    }
                    case "2" -> {
                        System.out.print("Enter gold amount: ");
                        int gold = Integer.parseInt(input.nextLine());
                        if (features.setGold(gold)) {
                            System.out.println("Gold set successfully.");
                        } else {
                            System.out.println("Failed to set gold.");
                        }
                    }
                    case "3" -> {
                        System.out.print("Enter action points (0-10): ");
                        int points = Integer.parseInt(input.nextLine());
                        if (features.setActionPoints(points)) {
                            System.out.println("Action points set successfully.");
                        } else {
                            System.out.println("Failed to set action points.");
                        }
                    }
                    case "4" -> {
                        System.out.println("Exiting...");
                        return;
                    }
                    default -> System.out.println("Invalid option.");
                }
            }
        } catch (Exception e) {
            logger.error("Error running trainer", e);
            System.err.println("An error occurred: " + e.getMessage());
        }
    }
}
