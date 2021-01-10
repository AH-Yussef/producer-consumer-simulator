package com.example.backend;

import java.util.Random;

public class Product {
    private String color = new String();
    public Product(){
        this.color = String.valueOf(1+new Random().nextInt(254)) + "," + String.valueOf(1+new Random().nextInt(254)) + "," 
        + String.valueOf(1+new Random().nextInt(254));
    }
    public String getColor(){
        return color;
    }
}
