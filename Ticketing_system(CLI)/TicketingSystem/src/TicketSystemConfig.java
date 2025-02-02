import java.io.*;
import java.util.Scanner;

public class TicketSystemConfig {
    private int totalTickets;
    private int ticketReleaseRate;
    private int customerRetrievalRate;
    private int maxTicketCapacity;

    public void configureSystem() {
        Scanner scanner = new Scanner(System.in);

        System.out.println("Welcome to the Ticket System Configuration!");

        totalTickets = getValidatedInput(scanner, "Enter total number of tickets available initially: ", 1, Integer.MAX_VALUE);
        maxTicketCapacity = getValidatedInput(scanner, "Enter maximum ticket capacity (total tickets available for customers to buy): ", 1, Integer.MAX_VALUE);
        ticketReleaseRate = getValidatedInput(scanner, "Enter ticket release rate (number of tickets vendors add per interval): ", 1, maxTicketCapacity);
        customerRetrievalRate = getValidatedInput(scanner, "Enter customer retrieval rate (number of tickets customers buy per interval): ", 1, maxTicketCapacity);

        System.out.println("Configuration complete!");
    }

    private int getValidatedInput(Scanner scanner, String prompt, int min, int max) {
        int value;
        while (true) {
            System.out.print(prompt);
            if (scanner.hasNextInt()) {
                value = scanner.nextInt();
                if (value >= min && value <= max) {
                    break;
                } else {
                    System.out.println("Invalid input. Value must be between " + min + " and " + max + ".");
                }
            } else {
                System.out.println("Invalid input. Please enter a positive integer.");
                scanner.next(); // Clear invalid input
            }
        }
        return value;
    }

    public int getTotalTickets() {
        return totalTickets;
    }

    public int getTicketReleaseRate() {
        return ticketReleaseRate;
    }

    public int getCustomerRetrievalRate() {
        return customerRetrievalRate;
    }

    public int getMaxTicketCapacity() {
        return maxTicketCapacity;
    }

    public void saveConfig(String filename) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filename))) {
            writer.write("Total Number of Tickets: " + totalTickets);
            writer.newLine();
            writer.write("Ticket Release Rate: " + ticketReleaseRate);
            writer.newLine();
            writer.write("Customer Retrieval Rate: " + customerRetrievalRate);
            writer.newLine();
            writer.write("Maximum Ticket Capacity: " + maxTicketCapacity);
            writer.newLine();
            System.out.println("Configuration saved to " + filename);
        } catch (IOException e) {
            System.err.println("Error saving configuration: " + e.getMessage());
        }
    }

    public static TicketSystemConfig loadConfig(String filename) {
        TicketSystemConfig config = new TicketSystemConfig();
        try (BufferedReader reader = new BufferedReader(new FileReader(filename))) {
            config.totalTickets = Integer.parseInt(reader.readLine().split(": ")[1]);
            config.ticketReleaseRate = Integer.parseInt(reader.readLine().split(": ")[1]);
            config.customerRetrievalRate = Integer.parseInt(reader.readLine().split(": ")[1]);
            config.maxTicketCapacity = Integer.parseInt(reader.readLine().split(": ")[1]);
            System.out.println("Configuration loaded from " + filename);
            return config;
        } catch (IOException | NumberFormatException e) {
            System.err.println("Error loading configuration: " + e.getMessage());
            return null;
        }
    }
}
