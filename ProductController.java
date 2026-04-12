package controller;

import model.Product;
import view.ProductView;

public class ProductController {

    private Product product;
    private ProductView view;

    public ProductController(Product product, ProductView view) {
        this.product = product;
        this.view = view;
    }

    public void displayProduct() {
        view.displayProduct(
                product.getId(),
                product.getName(),
                product.getPrice(),
                product.getStock()
        );
    }

    public void updateStock(int qty) {

        if (qty <= 0) {
            view.showMessage("Invalid quantity!");
            return;
        }

        product.setStock(product.getStock() + qty);
        view.showMessage("Stock updated successfully!");
    }
}