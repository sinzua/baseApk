package com.amazon.device.iap.internal.model;

import com.amazon.device.iap.model.Product;
import com.amazon.device.iap.model.ProductType;

public class ProductBuilder {
    private String description;
    private String price;
    private ProductType productType;
    private String sku;
    private String smallIconUrl;
    private String title;

    public Product build() {
        return new Product(this);
    }

    public ProductBuilder setSku(String str) {
        this.sku = str;
        return this;
    }

    public ProductBuilder setProductType(ProductType productType) {
        this.productType = productType;
        return this;
    }

    public ProductBuilder setDescription(String str) {
        this.description = str;
        return this;
    }

    public ProductBuilder setPrice(String str) {
        this.price = str;
        return this;
    }

    public ProductBuilder setSmallIconUrl(String str) {
        this.smallIconUrl = str;
        return this;
    }

    public ProductBuilder setTitle(String str) {
        this.title = str;
        return this;
    }

    public String getSku() {
        return this.sku;
    }

    public ProductType getProductType() {
        return this.productType;
    }

    public String getDescription() {
        return this.description;
    }

    public String getPrice() {
        return this.price;
    }

    public String getSmallIconUrl() {
        return this.smallIconUrl;
    }

    public String getTitle() {
        return this.title;
    }
}
