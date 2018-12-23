public class Multithreading {
    public static void main(String[] args) {
        ItemStore itemStore = new ItemStore();

        Thread fetchThread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    itemStore.fetchItems();
                } catch (InterruptedException ie) {
                    System.out.println("Fetch thread was interrupted: " + ie.getMessage());
                    System.exit(1);
                }
            }
        });

        Thread updateThread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    itemStore.updateItems();
                } catch (InterruptedException ie) {
                    System.out.println("Update thread was interrupted: " + ie.getMessage());
                    System.exit(1);
                }
            }
        });

        fetchThread.start();
        updateThread.start();

        try {
            fetchThread.join();
            updateThread.join();
        } catch (InterruptedException ie) {
            System.out.println("Thread interrupted while joining: " + ie.getMessage());
            System.exit(1);
        }

        // display all the items with the updated tax information
        itemStore.display();
    }
}
