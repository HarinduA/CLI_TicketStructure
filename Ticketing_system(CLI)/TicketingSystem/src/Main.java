import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.io.File;
import java.io.FilenameFilter;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        TicketSystemConfig config = null;

        while (true) {
            Logger.log("Commands: 'previousLoad', 'new'");
            System.out.print("Enter command: ");
            String command = scanner.nextLine().trim().toLowerCase();

            switch (command) {
                case "previousload":
                    File[] configFiles = getConfigFiles();
                    if (configFiles.length == 0) {
                        Logger.log("No configuration files found. Please create a new one.");
                        break;
                    } else if (configFiles.length == 1) {
                        config = TicketSystemConfig.loadConfig(configFiles[0].getName());
                        if (config == null) {
                            Logger.log("Failed to load configuration. Please create a new one.");
                            break;
                        }
                        Logger.log("Loaded configuration from " + configFiles[0].getName());
                    } else {
                        Logger.log("Available configuration files:");
                        for (File file : configFiles) {
                            Logger.log("- " + file.getName());
                        }
                        System.out.print("Enter the configuration file name to load: ");
                        String filename = scanner.nextLine().trim();
                        File selectedFile = new File(filename);
                        if (!selectedFile.exists()) {
                            Logger.log("File not found: " + filename);
                            break;
                        }
                        config = TicketSystemConfig.loadConfig(filename);
                        if (config == null) {
                            Logger.log("Failed to load configuration from " + filename);
                            break;
                        }
                        Logger.log("Loaded configuration from " + filename);
                    }
                    startSystem(scanner, config);
                    break;

                case "new":
                    config = newConfig(scanner);
                    startSystem(scanner, config);
                    break;

                default:
                    Logger.log("Invalid command. Try 'previousLoad' or 'new'.");
            }
        }
    }

    private static TicketSystemConfig newConfig(Scanner scanner) {
        TicketSystemConfig config = new TicketSystemConfig();
        config.configureSystem();
        String filename = getNextConfigFilename();
        config.saveConfig(filename);
        Logger.log("Configuration saved to " + filename);
        return config;
    }

    private static void startSystem(Scanner scanner, TicketSystemConfig config) {
        TicketSystem ticketSystem = new TicketSystem(config.getMaxTicketCapacity(), config.getTotalTickets());
        Logger.log("Vendors are about to start adding tickets. Customers can purchase tickets after this.");

        ExecutorService executor = Executors.newFixedThreadPool(10); // Total thread pool size can be adjusted

        int numberOfVendors = getValidatedInput(scanner, "Enter the number of vendors: ", 1, 50);
        for (int i = 0; i < numberOfVendors; i++) {
            executor.submit(new Vendor(ticketSystem, config.getTicketReleaseRate(), "Vendor-" + (i + 1)));
        }

        int numberOfCustomers = getValidatedInput(scanner, "Enter the number of customers: ", 1, 50);
        for (int i = 0; i < numberOfCustomers; i++) {
            executor.submit(new Customer(ticketSystem, config.getCustomerRetrievalRate(), 1, "Customer-" + (i + 1)));
        }

        executor.shutdown(); // No more tasks will be accepted
        try {
            if (!executor.awaitTermination(60, TimeUnit.SECONDS)) {
                executor.shutdownNow();
            }
        } catch (InterruptedException e) {
            executor.shutdownNow();
        }

        Logger.log("Ticket system has stopped. All threads have completed execution.");
    }

    private static int getValidatedInput(Scanner scanner, String prompt, int min, int max) {
        int value;
        while (true) {
            System.out.print(prompt);
            if (scanner.hasNextInt()) {
                value = scanner.nextInt();
                if (value >= min && value <= max) {
                    scanner.nextLine(); // Consume newline
                    return value;
                }
                Logger.log("Invalid input. Value must be between " + min + " and " + max + ".");
            } else {
                Logger.log("Invalid input. Please enter an integer.");
                scanner.next(); // Clear invalid input
            }
        }
    }

    private static String getNextConfigFilename() {
        File dir = new File(".");
        File[] configFiles = dir.listFiles((d, name) -> name.matches("Configuration\\d+\\.txt"));
        int nextNumber = configFiles == null ? 1 : configFiles.length + 1;
        return "Configuration" + nextNumber + ".txt";
    }

    private static File[] getConfigFiles() {
        File dir = new File(".");
        return dir.listFiles((d, name) -> name.matches("Configuration(\\d+)?\\.txt"));
    }
}