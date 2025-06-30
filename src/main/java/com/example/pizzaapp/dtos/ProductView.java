package com.example.pizzaapp.dtos;

import com.example.pizzaapp.models.Product;
import com.example.pizzaapp.enums.ProductSize;

public class ProductView {

    private final Product product;
    private final String sizesJson;
    private final String toppingsJson;
    private final ProductSize defaultSize;
    private final double defaultPrice;

    public ProductView(Product product, String sizesJson, String toppingsJson, ProductSize defaultSize,
            double defaultPrice) {
        this.product = product;
        this.sizesJson = sizesJson;
        this.toppingsJson = toppingsJson;
        this.defaultSize = defaultSize;
        this.defaultPrice = defaultPrice;
    }

    public Product getProduct() {
        return product;
    }

    public String getSizesJson() {
        return sizesJson;
    }

    public String getToppingsJson() {
        return toppingsJson;
    }

    public ProductSize getDefaultSize() {
        return defaultSize;
    }

    public double getDefaultPrice() {
        return defaultPrice;
    }

    @Override
    public String toString() {
        return "ProductView{" +
                "name='" + product.getName() + '\'' +
                ", description='" + product.getDescription() + '\'' +
                ", imageUrl='" + product.getImageUrl() + '\'' +
                ", category='" + (product.getCategory() != null ? product.getCategory().getName() : "null") + '\'' +
                ", sizesJson=" + sizesJson +
                ", toppingsJson=" + toppingsJson +
                ", defaultSize=" + defaultSize +
                ", defaultPrice=" + defaultPrice +
                '}';
    }

}
