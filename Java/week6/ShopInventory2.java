import java.util.Stack;
import java.util.Queue;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.Map;

class Item {
    public final String name;
    public double price;

    Item(String name, double price) {
        this.name = name;
        this.price = price;
    }

    public String toString() {
        return this.name;
    }
}

class Basket {
    private final Stack<Item> basket;

    Basket() {
        // TODO - create the stack
        this.basket = new Stack<Item>();
    }

    public void addItem(Item item) {
        // TODO
        this.basket.push(item);
    }

    public Item removeItem() {
        // TODO
        if(this.basket.isEmpty()) {
            return null;
        }
        return this.basket.pop();
    }

    public String toString() {
        return "basket:" + this.basket.toString();
    }
}

class Checkout {
    private final Queue<Item> checkout;

    Checkout(Basket basket) {
        // TODO - create the queue, add items from basket by using addItem()
        this.checkout = new LinkedList<Item>();

        Item item = basket.removeItem();
        while(item != null) {
            addItem(item);
            item = basket.removeItem();
        }
    }

    public void addItem(Item item) {
        // TODO
        this.checkout.add(item);
    }

    public Item removeItem() {
        // TODO
        if(this.checkout.isEmpty()) {
            return null;
        }
        return this.checkout.remove();
    }

    public String toString() {
        return "checkout:" + this.checkout.toString();
    }
}

class Bill {
    private final Map<String,Integer> basket;
    private double price;

    Bill(Checkout checkout) {
        // TODO - initialise Map, set price, bill items from checkout
        this.basket = new LinkedHashMap<String,Integer>(); // Used LinkedHashMap here to preserve the insertion order
        this.price = 0;

        Item item = checkout.removeItem();
        while(item != null) {
            billItem(item);
            item = checkout.removeItem();
        }
    }

    public void billItem(Item item) {
        // TODO - put item in map, keep count of same items being billed
        // Note that the Map is from String to Integer
        // Items have names as Strings and count of items is an integer
        if(this.basket.containsKey(item.name)) {
            this.basket.put(item.name, this.basket.get(item.name) + 1);
        }
        else {
            this.basket.put(item.name, 1);
        }
        this.price += item.price;
    }

    public double getTotal() {
        return this.price;
    }

    public String toString() {
        String output = "";
        for(String item: this.basket.keySet()) {
            output += item + " (" + this.basket.get(item) + "nos)\n";
        }
        return output + "total: " + this.price;
    }
}

public class ShopInventory2 {
    public static void main(String[] args) {
        Basket basket = new Basket();
        loadBasket(basket);
        //System.out.println(basket); // for DEBUG
        Checkout checkout = new Checkout(basket);
        //System.out.println(checkout); // for DEBUG
        Bill bill = new Bill(checkout);
        System.out.println(bill);
    }

    static void loadBasket(Basket basket) {
        basket.addItem(new Item("Twinings Earl Grey Tea", 4.50));
        basket.addItem(new Item("Folans Orange Marmalade", 4.00));
        basket.addItem(new Item("Free-range Eggs", 3.35));
        basket.addItem(new Item("Brennan's Bread", 2.15));
        basket.addItem(new Item("Ferrero Rocher", 7.00));
        basket.addItem(new Item("Ferrero Rocher", 7.00));
    }
}