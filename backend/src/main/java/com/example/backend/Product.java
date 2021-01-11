package com.example.backend;

import java.util.Random;

public class Product {
    Simulator simulator = Simulator.getInstance();
    private String color = new String();
    public Product(){
        //making sure that the colors are unique
        do{
        this.color = String.valueOf(1+new Random().nextInt(254)) + "," + String.valueOf(1+new Random().nextInt(254)) + "," 
        + String.valueOf(1+new Random().nextInt(254));
        }while(simulator.colorUsed(this.color));
    }
    public String getColor(){
        return color;
    }
}
