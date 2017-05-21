package com.amazon.device.iap.internal.model;

import com.amazon.device.iap.model.ProductType;
import com.amazon.device.iap.model.Receipt;
import java.util.Date;

public class ReceiptBuilder {
    private Date cancelDate;
    private ProductType productType;
    private Date purchaseDate;
    private String receiptId;
    private String sku;

    public Receipt build() {
        return new Receipt(this);
    }

    public String getReceiptId() {
        return this.receiptId;
    }

    public String getSku() {
        return this.sku;
    }

    public ProductType getProductType() {
        return this.productType;
    }

    public Date getPurchaseDate() {
        return this.purchaseDate;
    }

    public Date getCancelDate() {
        return this.cancelDate;
    }

    public ReceiptBuilder setReceiptId(String str) {
        this.receiptId = str;
        return this;
    }

    public ReceiptBuilder setSku(String str) {
        this.sku = str;
        return this;
    }

    public ReceiptBuilder setProductType(ProductType productType) {
        this.productType = productType;
        return this;
    }

    public ReceiptBuilder setPurchaseDate(Date date) {
        this.purchaseDate = date;
        return this;
    }

    public ReceiptBuilder setCancelDate(Date date) {
        this.cancelDate = date;
        return this;
    }
}
