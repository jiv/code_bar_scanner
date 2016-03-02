package com.jivarela.codebarscanner;

/**
 * Created by nacho on 19/01/16.
 */
public class Product {
    String code;
    String quantity;

    public Product(String c){
        this.code = c;
        this.quantity = "";
    }

    public String getCode() {
        return code;
    }

    public void setCode(String c){
        this.code = c;
    }

    public String getQuantity() {
        return quantity;
    }

    public void setQuantity(String q){
        this.quantity = q;
    }
}
