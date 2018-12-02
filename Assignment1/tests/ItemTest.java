import static org.junit.jupiter.api.Assertions.*;

class ItemTest {

    @org.junit.jupiter.api.Test
    void calculateTax() {
        Item testItem1 = new Item("testItem1", 50.67, 5, "raw");
        Item testItem2 = new Item("testItem2", 89.32, 8, "manufactured");
        Item testItem3 = new Item("testItem3", 34.81, 4, "imported");

        assertEquals(Math.round(testItem1.calculateTax() * 100) / 100.0, 31.67);
        assertEquals(Math.round(testItem2.calculateTax() * 100) / 100.0, 250.10);
        assertEquals(Math.round(testItem3.calculateTax() * 100) / 100.0, 23.92);
    }
}