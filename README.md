# Order Parser

A Java program made for the assignment from Pandell. 


## Features

- Reads orders from JSON input file
- Validates product price consistency across orders
- Calculates order totals and overall revenue
- Tracks product quantities across all orders
- Generates detailed order reports
- Multi-thread processing for better performance

## Program Description
To get the total costs of individual orders, the quantity and price for each product needs to be stored. A hashmap is created between the product name and quantity to update the quantity when processing the order. A second hashmap is created between the product name and price, in order to report an error when the same product has a different price. It's probably an overkill and could potentially slow down the code, but inconsistent price could cause issues when calculating the total avenue, hence this feature is implemented. In terms of performance, ConcurrentHashMap and Stream are used to process the orders in parallel. For the final output, the program prints the outputs as well as generates the outputs as files.


## Project Structure

```
orderparser/
  ├── lib/                    # External libraries
  │   ├── jackson-databind-2.19.0.jar
  │   ├── jackson-core-2.19.0.jar
  │   └── jackson-annotations-2.19.0.jar
  ├── src/                    # Source code
  │   ├── Main.java          # Main program
  │   ├── Order.java         # Order class
  │   └── Item.java          # Item class
  └── README.md              
```

## Compilation

From the project root directory:

```bash
# Create output directory
mkdir out

# Compile the program
javac -cp "lib/*" src/*.java -d out
```

## Usage

Run the program with your JSON input file:

```bash
java -cp "out;lib/*" Main input.json
```

Replace `input.json` with the path to your JSON input file.


## Maven

If you prefer using Maven, a `pom.xml` is included. Use these commands:

```bash
# Compile and package
mvn clean package

# Run
java -jar target/orderparser-1.0-SNAPSHOT.jar input.json
```


### Input File Format

The input JSON file should contain an array of orders with this structure:

```json
[
  {
    "orderId": "1003",
    "customer": "Mike",
    "items": [
      {
        "product": "toy",
        "quantity": 5,
        "price": 3.0
      },
      {
        "product": "Gadget",
        "quantity": 2,
        "price": 15.0
      }
    ],
    "status": "shipped"
  }
]
```

### Output

The program generates two output files:
- `order_details.txt`: Detailed information about each order
- `order_summary.txt`: Summary of product quantities and total revenue

