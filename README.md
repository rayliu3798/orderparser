# Order Parser

A Java program that processes order data from JSON files, validates product prices, calculates totals, and generates detailed reports.

## Features

- Reads orders from JSON input file
- Validates product price consistency across orders
- Calculates order totals and overall revenue
- Tracks product quantities across all orders
- Generates detailed order reports
- Supports parallel processing for better performance
- Thread-safe data structures for concurrent operations

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
  └── README.md              # This file
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

