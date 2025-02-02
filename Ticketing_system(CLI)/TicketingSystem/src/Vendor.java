import java.util.Random;

public class Vendor implements Runnable {
    private final TicketSystem ticketSystem;
    private final int maxTicketsPerRelease;
    private final String vendorName;
    private final Random random;

    public Vendor(TicketSystem ticketSystem, int maxTicketsPerRelease, String vendorName) {
        this.ticketSystem = ticketSystem;
        this.maxTicketsPerRelease = maxTicketsPerRelease;
        this.vendorName = vendorName;
        this.random = new Random();
    }

    @Override
    public void run() {
        while (!Thread.currentThread().isInterrupted() && !ticketSystem.isStopped()) {
            int randomTicketCount = random.nextInt(maxTicketsPerRelease) + 1;
            ticketSystem.addTickets(vendorName, randomTicketCount);

            try {
                int randomInterval = random.nextInt(2000) + 1000;
                Thread.sleep(randomInterval);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
        Logger.log(vendorName + " has finished adding tickets.");
    }
}