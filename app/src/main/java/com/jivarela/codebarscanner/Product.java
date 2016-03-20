package com.jivarela.codebarscanner;

/**
 * Created by nacho on 19/01/16.
 */
public class Product {
    String code;
    Integer quantity;

    public Product(String c){
        this.code = c;
        this.quantity = 0;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String c){
        this.code = c;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer q){
        this.quantity = q;
    }
}
