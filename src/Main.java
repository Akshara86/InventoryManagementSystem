import java.util.*;

class Item {
    int id,quantity;
    String name, category;

    public Item(int id, String name, String category, int quantity) {
        this.id = id;
        this.name = name;
        this.category = category;
        this.quantity = quantity;
    }

    @Override
    public String toString() {
        return "ID: " + id + ", Name: " + name + ", Category: " + category + ", Quantity: " + quantity;
    }
}

class InventorySystem {
    private Map<Integer, Item> inventory = new HashMap<>();
    private Map<String, PriorityQueue<Item>> categoryMap = new HashMap<>();

    public void addItem(int id, String name, String category, int quantity) {
        if (inventory.containsKey(id)) {
            System.out.println("Item with ID " + id + " already exists.");
            return;
        }
        Item newItem = new Item(id, name, category, quantity);
        inventory.put(id, newItem);

        categoryMap.putIfAbsent(category, new PriorityQueue<>((a, b) -> b.quantity - a.quantity));
        categoryMap.get(category).add(newItem);

        if (quantity < 10) {
            System.out.println("Restock Alert for: " + name);
        }
    }

    public Item searchItem(int id) {
        return inventory.getOrDefault(id, null);
    }

    public void updateItem(int id, int newQuantity) {
        Item item = inventory.get(id);
        if (item != null) {
            categoryMap.get(item.category).remove(item);
            item.quantity = newQuantity;
            categoryMap.get(item.category).add(item);
            System.out.println("Updated item: " + item);
            if (newQuantity < 4) {
                System.out.println("Restock Alert for: " + item.name);
            }
        } else {
            System.out.println("Item with ID " + id + " not found.");
        }
    }

    public void deleteItem(int id) {
        Item item = inventory.remove(id);
        if (item != null) {
            categoryMap.get(item.category).remove(item);
            System.out.println("Deleted item with ID: " + id);
        } else {
            System.out.println("Item with ID " + id + " not found.");
        }
    }

    public List<Item> getItemsByCategory(String category) {
        return new ArrayList<>(categoryMap.getOrDefault(category, null));
    }

    public List<Item> getTopKItems(int k) {
        PriorityQueue<Item> allItems = new PriorityQueue<>((a, b) -> b.quantity - a.quantity);
        allItems.addAll(inventory.values());

        List<Item> topKItems = new ArrayList<>();
        for (int i = 0; i < k && !allItems.isEmpty(); i++) {
            topKItems.add(allItems.poll());
        }
        return topKItems;
    }
}


public class Main {
    public static void main(String[] args) {
        InventorySystem system = new InventorySystem();


        //Add items
        system.addItem(1, "Laptop", "Electronics", 5);
        system.addItem(2, "Chair", "Furniture", 15);
        system.addItem(3, "Apple", "Groceries", 50);
        system.addItem(4, "pants", "Cloths", 23);

        // Search for an item by ID
        System.out.println("Search for item with ID 1: " + system.searchItem(1));

        // Update an item
        system.updateItem(1, 20);

        // Delete an item
        system.deleteItem(1);

        // Retrieve top 2 items by quantity
        System.out.println("Top 3 items: " + system.getTopKItems(3));
    }
}
