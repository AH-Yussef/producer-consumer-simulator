package com.example.backend;

import java.util.Random;

public class Product {
    String color = new String();
    public Product(){
        this.color = String.valueOf(new Random().nextInt(255)) + "," + String.valueOf(new Random().nextInt(255)) + "," 
        + String.valueOf(new Random().nextInt(255));
    }
    public String getColor(){
        return color;
    }
}
