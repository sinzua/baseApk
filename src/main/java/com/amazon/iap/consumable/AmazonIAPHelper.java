package com.amazon.iap.consumable;

import android.content.Context;
import android.util.Log;
import com.amazon.device.iap.PurchasingListener;
import com.amazon.device.iap.PurchasingService;
import com.amazon.device.iap.model.Product;
import com.amazon.device.iap.model.ProductDataResponse;
import com.anjlab.android.iab.v3.SkuDetails;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

public class AmazonIAPHelper {
    private static final String PATH = "/mnt/sdcard/amazon.sdktester.json";
    private static final String TAG = "AmazonIAPHelper";
    private static PurchasingListener sPurchasingListener;

    public static void setupIAPOnCreate(Context context, PurchasingListener purchasingListener) {
        sPurchasingListener = purchasingListener;
        PurchasingService.registerListener(context, purchasingListener);
        try {
            copyDataBase(context);
        } catch (Exception e) {
            Log.d(TAG, "copyDataBase failed!");
        }
        Log.d(TAG, "IS_SANDBOX_MODE:" + PurchasingService.IS_SANDBOX_MODE);
    }

    private static void copyDataBase(Context context) throws IOException {
        InputStream myInput = context.getAssets().open("amazon.sdktester.json");
        OutputStream myOutput = new FileOutputStream(PATH);
        byte[] buffer = new byte[1024];
        while (true) {
            int length = myInput.read(buffer);
            if (length > 0) {
                myOutput.write(buffer, 0, length);
            } else {
                myOutput.flush();
                myOutput.close();
                myInput.close();
                return;
            }
        }
    }

    public static void getProductData(ArrayList<String> productIdList) {
        Set<String> productSkus = new HashSet();
        Iterator it = productIdList.iterator();
        while (it.hasNext()) {
            productSkus.add((String) it.next());
        }
        PurchasingService.getProductData(productSkus);
    }

    public static void purchase(String productId) {
        Log.d(TAG, "amazon purchase: " + productId);
        PurchasingService.purchase(productId);
    }

    public static List<SkuDetails> getSkusDetails(ProductDataResponse response) {
        List<SkuDetails> skusDetailsList = new ArrayList();
        for (Product product : response.getProductData().values()) {
            SkuDetails skuDetails = convertSkuDetails(product);
            if (skuDetails != null) {
                skusDetailsList.add(skuDetails);
            }
        }
        return skusDetailsList;
    }

    public static SkuDetails convertSkuDetails(Product product) {
        return new SkuDetails(product.getSku(), product.getTitle(), product.getDescription(), product.getProductType().equals("CONSUMABLE"), "", Double.valueOf(0.0d), product.getPrice());
    }
}
