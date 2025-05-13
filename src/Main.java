/**
 * Main application class that processes orders from a JSON file.
 * Handles reading, processing, and reporting of order data.
 * Uses parallel processing and thread-safe structures for performance.
 */
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.core.type.TypeReference;
import java.io.File;
import java.io.IOException;
import java.io.FileWriter;
import java.io.BufferedWriter;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.DoubleAdder;
import java.util.stream.Collectors;

public class Main {
    // Path to the output files
    private static final String ORDER_Detail = "src/order_details.txt";
    private static final String ORDER_SUMMARY = "src/order_summary.txt";
    // Jackson ObjectMapper for JSON parsing
    private static final ObjectMapper MAPPER = new ObjectMapper();

    /**
     * Main entry point of the application.
     * 
     * @param args Command line arguments. Expects the input JSON file path as the first argument.
     */
    public static void main(String[] args) {
        if (args.length != 1) {
            System.err.println("Usage: java Main <input_json_file>");
            System.exit(1);
        }

        File inputFile = new File(args[0]);
        if (!inputFile.exists()) {
            System.err.println("Error: Input file '" + args[0] + "' does not exist");
            System.exit(1);
        }
        if (!inputFile.isFile()) {
            System.err.println("Error: '" + args[0] + "' is not a file");
            System.exit(1);
        }
        if (!inputFile.canRead()) {
            System.err.println("Error: Cannot read input file '" + args[0] + "'");
            System.exit(1);
        }

        try {
            processOrders(inputFile);
        } catch (IOException e) {
            System.err.println("Error processing orders: " + e.getMessage());
            System.exit(1);
        } catch (Exception e) {
            System.err.println("Unexpected error: " + e.getMessage());
            System.exit(1);
        }
    }

    /**
     * Reads orders from a JSON file, processes them in parallel,
     * calculates product quantities and total revenue, and outputs results.
     *
     * @param inputFile The JSON file containing orders to process
     * @throws IOException if there is an error reading or writing files
     */
    private static void processOrders(File inputFile) throws IOException {
        // Parse the list of orders from the input JSON file
        List<Order> orders = MAPPER.readValue(inputFile, new TypeReference<List<Order>>() {});
        // ConcurrentHashMap to store product quantities
        Map<String, Integer> productQuantity = new ConcurrentHashMap<>();
        // ConcurrentHashMap to track product prices for consistency
        Map<String, Double> productPrices = new ConcurrentHashMap<>();
        // Accumulate total revenue
        DoubleAdder revenue = new DoubleAdder();

        // Process each order in parallel and build formatted output strings
        List<String> outputs = orders.parallelStream().map(order -> {
            StringBuilder sb = new StringBuilder()
                .append("Order Id: ").append(order.getOrderId())
                .append(", Customer: ").append(order.getCustomer())
                .append(", Status: ").append(order.getStatus()).append("\n");
            
            // Total cost for this order
            double orderTotal = 0.0;
            
            // For each item in the order, append details, update product quantity and revenue
            for (Item item : order.getItems()) {
                String product = item.getProduct();
                double price = item.getPrice();
                
                // Check price consistency
                Double existingPrice = productPrices.get(product);
                if (existingPrice != null && Math.abs(existingPrice - price) > 0.001) {
                    throw new IllegalStateException(
                        String.format("Price mismatch for product '%s': Found $%.2f but previously had $%.2f",
                        product, price, existingPrice)
                    );
                }
                productPrices.putIfAbsent(product, price);
                
                double itemTotal = item.getQuantity() * price;
                orderTotal += itemTotal;
                // Append item details
                sb.append("  Product: ").append(product)
                  .append(", Qty: ").append(item.getQuantity())
                  .append(", Price: $ ").append(price)
                  .append("\n");
                // Accumulate product quantity
                productQuantity.merge(product, item.getQuantity(), Integer::sum);
            }
            // Add to revenue only if order is shipped
            if (order.getIntStatus() == 1) {
                revenue.add(orderTotal);
            }
            // Add order total
            sb.append("  Order Total: $ ").append(String.format("%.2f", orderTotal)).append("\n");
            return sb.toString();
        }).collect(Collectors.toList());

        writeOutputs(outputs, productQuantity, revenue.sum());
    }

    /**
     * Writes the order details and summary to both console and files.
     *
     * @param outputs List of formatted order details
     * @param productQuantity Map of product quantities
     * @param totalRevenue Total revenue from shipped orders
     * @throws IOException if there is an error writing to files
     */
    private static void writeOutputs(List<String> outputs, Map<String, Integer> productQuantity, double totalRevenue) throws IOException {
        // Build the orderdetails output string
        StringBuilder orderdetails = new StringBuilder();
        outputs.forEach(orderdetails::append);

        // Build the order summary and revenue output string
        StringBuilder ordersummary = new StringBuilder();
        ordersummary.append("Total sale product Quantities:\n");
        ordersummary.append("------------------\n");
        ordersummary.append(String.format("%-20s %s%n", "Product", "Quantity"));
        ordersummary.append("------------------\n");
        productQuantity.forEach((product, quantity) -> 
        ordersummary.append(String.format("%-20s %d%n", product, quantity)));
        ordersummary.append("------------------\n");        
        ordersummary.append("Total Revenue: ").append(totalRevenue).append("\n");

        // Print to console
        System.out.print(orderdetails.toString());
        System.out.print(ordersummary.toString());
        
        // Write to file
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(ORDER_Detail))) {
            writer.write(orderdetails.toString());
        }
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(ORDER_SUMMARY))) {
            writer.write(ordersummary.toString());
        }
    }
}
