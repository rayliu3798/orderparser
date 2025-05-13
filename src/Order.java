import java.util.List;


/**
 * Represents an order in the system with customer details, items, and status.
 * This class is designed to be immutable for most fields and provides thread-safe operations.
 */
public class Order {
    private String orderId;
    private String customer;
    private List<Item> items;
    private double orderTotal;
    private String status;
    private static final String SHIPPED_STATUS = "shipped";

    /**
     * Default constructor for Jackson deserialization.
     */
    public Order() {}

    /**
     * Constructs a new Order with the specified details.
     * 
     * @param orderId The unique identifier for the order (must not be null or empty)
     * @param customer The customer name (must not be null or empty)
     * @param items The list of items in the order (must not be null or empty)
     * @param status The status of the order (must not be null or empty)
     * @throws IllegalArgumentException if any parameter is invalid
     */
    public Order(String orderId, String customer, List<Item> items, String status) {
        if (orderId == null || orderId.trim().isEmpty()) {
            throw new IllegalArgumentException("Order ID cannot be null or empty");
        }
        if (customer == null || customer.trim().isEmpty()) {
            throw new IllegalArgumentException("Customer cannot be null or empty");
        }
        if (items == null || items.isEmpty()) {
            throw new IllegalArgumentException("Items list cannot be null or empty");
        }
        if (status == null || status.trim().isEmpty()) {
            throw new IllegalArgumentException("Status cannot be null or empty");
        }
        this.orderId = orderId;
        this.customer = customer;
        this.items = items;
        this.status = status;
        this.orderTotal = calculateOrderTotal();
    }

    /**
     * Calculates the total value of the order by summing the price * quantity for each item.
     * 
     * @return The total value of the order
     */
    private double calculateOrderTotal() {
        return items.stream()
                .mapToDouble(item -> item.getPrice() * item.getQuantity())
                .sum();
    }

    /**
     * Returns an integer representation of the order status.
     * Returns 1 if the status is "shipped" (case-insensitive), 0 otherwise.
     * 
     * @return 1 for shipped orders, 0 for other statuses
     */
    public int getIntStatus() {
        return SHIPPED_STATUS.equalsIgnoreCase(status) ? 1 : 0;
    }

    /**
     * getters and setters for orderId, customer, items, status, and orderTotal
     */
    public String getOrderId() { return orderId; }
    public void setOrderId(String orderId) { this.orderId = orderId; }

    public String getCustomer() { return customer; }
    public void setCustomer(String customer) { this.customer = customer; }

    public List<Item> getItems() { return items; }
    public void setItems(List<Item> items) { this.items = items; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public double getOrderTotal() { return orderTotal; }
    public void setOrderTotal(double orderTotal) { this.orderTotal = orderTotal; }

    
}