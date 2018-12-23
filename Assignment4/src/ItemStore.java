import java.sql.*;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

import inventory.Item;

class ItemStore {

    private Queue<Item> fetchedItems = new LinkedList<>();
    private List<Item> updatedItems = new ArrayList<>();
    private int queueCap = 5;
    private boolean completed = false; // value to notify updateThread that fetchThread has finished its job
    private Connection connection;

    ItemStore() {
        try {
            Class.forName("com.mysql.jdbc.Driver");
            this.connection = DriverManager.getConnection("jdbc:mysql://localhost/inventory", "root", "");
            System.out.println("Connection to db established!");
        } catch (ClassNotFoundException ce) {
            System.out.println("Error fetching the driver: " + ce.getMessage());
            System.exit(1);
        } catch (SQLException se) {
            System.out.println("Error establishing a connection to the database: " + se.getMessage());
            System.exit(1);
        }
    }

    /* Fetch items from the db and store in fetchedItems */
    void fetchItems() throws InterruptedException {
        try {
            Statement statement = this.connection.createStatement();
            ResultSet resultSet = statement.executeQuery("select name, price, quantity, type from items");
            while (resultSet.next()) {
                synchronized (this) {
                    while (fetchedItems.size() == queueCap)
                        wait();
                    String name = resultSet.getString("name");
                    float price = resultSet.getFloat("price");
                    int quantity = resultSet.getInt("quantity");
                    String type = resultSet.getString("type");
                    Item item = new Item(name, price, quantity, type);
                    fetchedItems.add(item);
                    System.out.println("Fetched item: " + item.getName());

                    // ask the other thread to resume working
                    notify();
                }
            }
            // set completed flag to true as all rows have been successfully scanned
            this.completed = true;
        } catch (SQLException se) {
            System.out.println("Error: " + se.getMessage());
            System.exit(1);
        }
    }

    /* Alter items with new tax and store in updatedItems */
    void updateItems() throws InterruptedException {
        // run the loop until the completed flag is set to true, or if there are still some items left to update afterwards
        while (fetchedItems.size() != 0 || !this.completed) {
            synchronized (this) {
                while (fetchedItems.size() == 0)
                    wait();
                Item item = fetchedItems.remove();
                item.setTax(item.calculateTax());
                updatedItems.add(item);
                System.out.println("Updated item: " + item.getName());

                // ask the other thread to resume working
                notify();
            }
        }
    }

    /* Display all updated items */
    void display() {
        for (Item item : updatedItems) {
            System.out.println("----------");
            item.display();
            System.out.println("----------");
        }
    }
}
