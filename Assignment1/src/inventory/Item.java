package inventory;

public class Item {
    enum ItemType {
        raw, manufactured, imported
    }

    private String name;
    private double price;
    private int quantity;
    private ItemType type;
    private double tax;

    public Item(String name, double price, int quantity, String type) {
        this.name = name;
        this.price = price;
        this.quantity = quantity;
        // will throw an exception if type is not raw, manufactured, or imported
        this.type = ItemType.valueOf(type);
        this.tax = 0.0;
    }

    /* Getter method for name */
    public String getName() {
        return this.name;
    }

    /* Setter method for tax */
    public void setTax(double tax) {
        this.tax = tax;
    }

    /* Calculates tax on the final price summed up by quantities of the item */
    public double calculateTax() {
        double salesTax;
        double totalPrice = this.price * this.quantity;
        switch (this.type) {
            case raw:
                salesTax = 0.125 * totalPrice;
                break;
            case manufactured:
                salesTax = 0.125 * totalPrice + 0.2 * (totalPrice + 0.125 * totalPrice);
                break;
            default:
                salesTax = 0.1 * totalPrice;
                double finalPrice = salesTax + totalPrice;
                if (finalPrice <= 100) salesTax += 5;
                else if (finalPrice <= 200) salesTax += 10;
                else salesTax += 0.05 * finalPrice;
        }
        return salesTax;
    }

    /* Displays info about the item, along with the tax and final amount */
    public void display() {
        this.tax = this.calculateTax();
        System.out.printf("Item name: %s\n", this.name);
        System.out.printf("Item price: %.2f\n", this.price);
        System.out.printf("Item quantity: %d\n", this.quantity);
        double finalPrice = this.price * this.quantity;
        System.out.printf("Final price: %.2f\n", finalPrice);
        System.out.printf("Tax applied: %.2f\n", this.tax);
        System.out.printf("Total amount: %.2f\n", (this.tax + finalPrice));
    }
}
