import java.util.concurrent.atomic.AtomicInteger;

public class TicketSystem {
    private int totalTickets;
    private final int maxCapacity;
    private boolean stopped;
    private boolean finishedMessageDisplayed = false;
    private AtomicInteger totalSoldTickets = new AtomicInteger(0);
    private AtomicInteger ticketNumber = new AtomicInteger(1);
    private AtomicInteger unsuccessfulAttempts = new AtomicInteger(0);

    public TicketSystem(int maxCapacity, int initialTickets) {
        this.maxCapacity = maxCapacity;
        this.totalTickets = initialTickets;
        this.stopped = false;
    }

    public synchronized void addTickets(String vendorName, int count) {
        for (int i = 0; i < count; i++) {
            if (totalTickets >= maxCapacity || stopped) break;

            int ticketId = ticketNumber.getAndIncrement();
            totalTickets++;
            Logger.log("Ticket added: " + vendorName + "-Ticket-" + ticketId + ". Available tickets: " + totalTickets);
            Logger.log(vendorName + " added " + vendorName + "-Ticket-" + ticketId);
            notifyAll();
        }
    }

    public synchronized void buyTickets(String customerName, int purchaseLimit) {
        int ticketsBought = 0;

        while (ticketsBought < purchaseLimit && !stopped) {
            while (totalTickets <= 0 && !stopped) {
                unsuccessfulAttempts.incrementAndGet();
                Logger.log(customerName + " tried to buy tickets, but they are sold out.");
                return;
            }

            if (totalTickets > 0) {
                int ticketId = totalSoldTickets.incrementAndGet();
                totalTickets--;
                ticketsBought++;
                Logger.log(customerName + " bought Vendor-" + (ticketId % 5 + 1) + "-Ticket-" + ticketId + ". Available tickets: " + totalTickets);
                Logger.log(customerName + " bought Vendor-" + (ticketId % 5 + 1) + "-Ticket-" + ticketId + " (Total: " + ticketsBought + "/" + purchaseLimit + ")");
                notifyAll();
            }
        }

        if (ticketsBought == purchaseLimit) {
            Logger.log(customerName + " has reached their purchase limit of " + purchaseLimit + " tickets.");
        }

        if (totalTickets == 0 && !finishedMessageDisplayed) {
            finishedMessageDisplayed = true;
            Logger.log("Tickets are finished. TicketPool closed. 🎉🎉");
            Logger.log(unsuccessfulAttempts.get() + " customers tried to purchase tickets but none were available.");
        }
    }

    public synchronized void stop() {
        this.stopped = true;
        notifyAll();
    }

    public synchronized int getTotalTickets() {
        return totalTickets;
    }

    public synchronized boolean isStopped() {
        return stopped;
    }
}