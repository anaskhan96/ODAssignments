import java.util.*;

import inventory.Item;

public class InventoryManagement {

    /* Parse given command line arguments and place them in the map for future reference */
    private static void parseCmdArgs(String[] arguments, List<String> flagsList, Map<String, String> argumentsMap) throws Exception {
        for (int i = 0; i < arguments.length; i += 2) {
            String flag = arguments[i].substring(1);
            if (flagsList.contains(flag)) {
                if (!(flag.equals("name") || argumentsMap.containsKey("name")))
                    throw new Exception("Flag name should be provided first");
                argumentsMap.put(flag, arguments[i + 1]);
            } else
                throw new IllegalArgumentException("Accepted arguments: -name, -price, -quantity, -type");
        }
        if (!argumentsMap.containsKey("type")) throw new MissingFormatArgumentException("Argument type is mandatory");
    }

    /* Add more items as per the user's wish */
    private static void addMoreItems(List<Item> itemList) {
        Scanner reader = new Scanner(System.in);
        System.out.print("\nDo you want to enter details of any other item? (y/n): ");
        // loop till the user wants to add more items
        while (reader.next().charAt(0) == 'y') {
            System.out.print("Name: ");
            String name = reader.nextLine();
            System.out.print("Price: ");
            double price = Double.parseDouble(reader.nextLine());
            System.out.print("Quantity: ");
            int quantity = Integer.parseInt(reader.nextLine());
            System.out.print("Type (raw, manufactured, or imported): ");
            String type = reader.nextLine();
            try {
                itemList.add(new Item(name, price, quantity, type));
                System.out.printf("Item %s added!\n", name);
            } catch (IllegalArgumentException iae) {
                // type provided was not an accepted input
                System.out.println("Argument type should either be raw, manufactured, or imported");
                System.out.println("Item discarded!\n");
            }
            System.out.print("\nDo you want to enter details of any other item? (y/n): ");
        }
    }

    /* Display info about each item from the item list */
    private static void displayAllItems(List<Item> itemList) {
        for (Item item : itemList) {
            System.out.println("----------");
            item.display();
        }
        System.out.println("----------");
    }

    public static void main(String[] args) {
        final List<String> flags = new ArrayList<>();
        flags.add("name");
        flags.add("price");
        flags.add("quantity");
        flags.add("type");

        Map<String, String> argumentsMap = new HashMap<>();
        try {
            parseCmdArgs(args, flags, argumentsMap);
        } catch (Exception e) {
            System.out.println(e.getClass() + ": " + e.getMessage());
            System.exit(1);
        }

        List<Item> items = new ArrayList<>();
        try {
            items.add(new Item(argumentsMap.getOrDefault("name", "-"), Double.parseDouble(argumentsMap.getOrDefault("price", "10")), Integer.parseInt(argumentsMap.getOrDefault("quantity", "1")), argumentsMap.get("type")));
        } catch (IllegalArgumentException iae) {
            // type provided was not an accepted input
            System.out.println("Argument type should either be raw, manufactured, or imported");
            System.exit(1);
        }
        System.out.print("Item added!\n");

        addMoreItems(items);
        displayAllItems(items);
    }
}
