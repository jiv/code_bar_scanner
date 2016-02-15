package com.jivarela.codebarscanner;

/**
 * Created by nacho on 19/01/16.
 */
public class Product {
    String code;
    int quantity;

    public Product(String c, int q){
        this.code = c;
        this.quantity = q;
    }

    public void setCode(String c){
        this.code = c;
    }

    public void setQuantity(int q){
        this.quantity = q;
    }
}
