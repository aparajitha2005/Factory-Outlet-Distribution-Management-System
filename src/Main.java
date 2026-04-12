import factory.OrderFactory;
import model.Product;
import model.Order;

import view.ProductView;
import view.OrderView;

import controller.ProductController;
import controller.OrderController;

import java.util.Scanner;

public class Main {

    public static void main(String[] args) {

        Scanner sc = new Scanner(System.in);

        Product product = new Product(1, "Laptop", 50000, 10);
        ProductView pView = new ProductView();
        ProductController pController = new ProductController(product, pView);

        OrderView oView = new OrderView();
        OrderController oController = new OrderController(null, oView);

        while (true) {

            System.out.println("\n===== FACTORY OUTLET SYSTEM =====");
            System.out.println("1. View Product");
            System.out.println("2. Update Stock");
            System.out.println("3. Create Order");
            System.out.println("4. Update Order Status");
            System.out.println("5. View Order");
            System.out.println("6. Exit");

            int choice = sc.nextInt();

            switch (choice) {

                case 1:
                    pController.displayProduct();
                    break;

                case 2:
                    System.out.print("Enter quantity: ");
                    int qty = sc.nextInt();
                    pController.updateStock(qty);
                    break;

                case 3:

                    if (product.getStock() <= 0) {
                        System.out.println("Out of stock! Cannot create order.");
                        break;
                    }

                    System.out.print("Enter Order ID: ");
                    int id = sc.nextInt();

                    System.out.print("Enter Order Type (normal/bulk/priority): ");
                    String type = sc.next();

                    Order order = OrderFactory.createOrder(type, id);
                    oController.setOrder(order);

                    oController.createOrder();
                    break;

                case 4:
                    System.out.print("Enter status: ");
                    sc.nextLine();
                    String status = sc.nextLine();

                    oController.updateStatus(status);
                    break;

                case 5:
                    oController.displayOrder();
                    break;

                case 6:
                    sc.close();
                    System.exit(0);
            }
        }
    }
}