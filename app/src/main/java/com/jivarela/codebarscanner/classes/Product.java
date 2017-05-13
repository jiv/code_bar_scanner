package com.jivarela.codebarscanner.classes;

public class Product {
    private String code;
    private Integer quantity;

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
