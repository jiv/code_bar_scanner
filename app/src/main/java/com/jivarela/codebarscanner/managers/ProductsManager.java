package com.jivarela.codebarscanner.managers;


import com.jivarela.codebarscanner.classes.Product;

import java.util.ArrayList;

public class ProductsManager {
    private ArrayList<Product> mProductsList;
    private static ProductsManager sInstance = new ProductsManager();

    private ProductsManager() {
        this.mProductsList = new ArrayList<>();
    }

    public static ProductsManager getInstance() {
        return sInstance;
    }

    public ArrayList<Product> getProductsList() {
        return mProductsList;
    }

    public void addProduct(Product product) {
        mProductsList.add(product);
    }
}
