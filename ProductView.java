package view;

public class ProductView {

    public void displayProduct(int id, String name, double price, int stock) {
        System.out.println("\n----- PRODUCT DETAILS -----");
        System.out.println("ID: " + id);
        System.out.println("Name: " + name);
        System.out.println("Price: ₹" + price);
        System.out.println("Stock: " + stock);
        System.out.println("---------------------------");
    }

    public void showMessage(String message) {
        System.out.println(message);
    }
}