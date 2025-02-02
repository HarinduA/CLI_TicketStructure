import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Logger {
    private static BufferedWriter writer;

    static {
        try {
            String dateTime = new SimpleDateFormat("yyyy-MM-dd_HH-mm-ss").format(new Date());
            String filename = "correct_" + dateTime + "_ticket_log.txt";
            writer = new BufferedWriter(new FileWriter(filename, true));

            // Register shutdown hook to close writer at the end of the program
            Runtime.getRuntime().addShutdownHook(new Thread(() -> {
                try {
                    if (writer != null) {
                        writer.close();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Synchronized method to log messages to file and console
    public static synchronized void log(String message) {
        System.out.println(message); // Print to console
        try {
            writer.write(message);
            writer.newLine();
            writer.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}