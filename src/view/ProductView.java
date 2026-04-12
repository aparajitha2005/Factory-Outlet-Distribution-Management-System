package view;

public class ProductView {

    public void displayProduct(int id, String name, double price, int stock) {
        System.out.println("Product ID: " + id);
        System.out.println("Name: " + name);
        System.out.println("Price: " + price);
        System.out.println("Stock: " + stock);
    }

    public void showMessage(String message) {
        System.out.println(message);
    }
}