public class Customer implements Runnable {
    private final TicketSystem ticketSystem;
    private final int maxPurchaseLimit;
    private final String customerName;

    public Customer(TicketSystem ticketSystem, int maxPurchaseLimit, int i, String customerName) {
        this.ticketSystem = ticketSystem;
        this.maxPurchaseLimit = maxPurchaseLimit;
        this.customerName = customerName;
    }

    @Override
    public void run() {
        ticketSystem.buyTickets(customerName, maxPurchaseLimit);

        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}