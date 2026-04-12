package view;

public class OrderView {

    public void displayOrder(int orderId, String status) {
        System.out.println("Order ID: " + orderId);
        System.out.println("Status: " + status);
    }

    public void showMessage(String message) {
        System.out.println(message);
    }
}