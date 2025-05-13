/**
 * Represents an item in an order with product details, quantity, and price.
 * This class is immutable for the product field and provides validation for quantity and price.
 */
public class Item {
    private String product;
    private int quantity;
    private double price;

    /**
     * Default constructor for Jackson deserialization.
     */
    public Item() {}

    /**
     * Constructs a new Item with the specified product, quantity, and price.
     * 
     * @param product The name of the product (must not be null or empty)
     * @param quantity The quantity of the product (must be positive)
     * @param price The price of the product (must not be negative)
     * @throws IllegalArgumentException if any parameter is invalid
     */
    public Item(String product, int quantity, double price) {
        if (product == null || product.trim().isEmpty()) {
            throw new IllegalArgumentException("Product name cannot be null or empty");
        }
        if (quantity <= 0) {
            throw new IllegalArgumentException("Quantity must be positive");
        }
        if (price < 0) {
            throw new IllegalArgumentException("Price cannot be negative");
        }
        this.product = product;
        this.quantity = quantity;
        this.price = price;
    }

    /**
     * getters and setters for the product
     */
    public String getProduct() { return product; }
    public void setProduct(String product) { this.product = product; }

   /**
     * getters and setters for the quantity
     * @param quantity must be positive
     * @throws IllegalArgumentException if quantity is not positive
     */
    public int getQuantity() { return quantity; }
    public void setQuantity(int quantity) {
        if (quantity <= 0) {
            throw new IllegalArgumentException("Quantity must be positive");
        }
        this.quantity = quantity;
    }

   /**
     * getters and setters for the price
     * @param price must be positive
     * @throws IllegalArgumentException if price is not positive
     */
    public double getPrice() { return price; }
    public void setPrice(double price) {
        if (price < 0) {
            throw new IllegalArgumentException("Price cannot be negative");
        }
        this.price = price;
    }
}