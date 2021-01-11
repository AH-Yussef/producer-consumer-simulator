package com.example.backend;

import java.util.Random;

public class Product {
    Simulator simulator = Simulator.getInstance();
    private String color = new String();
    public Product(){
        //if color already exists we will keep generating colors until a unique one
        do{
        this.color = String.valueOf(1+new Random().nextInt(254)) + "," + String.valueOf(1+new Random().nextInt(254)) + "," 
        + String.valueOf(1+new Random().nextInt(254));
        }while(simulator.colorUsed(this.color));
        
        //if a unique color is found then it is added to the set
        simulator.addColor(this.color);
    }
    public String getColor(){
        return color;
    }
}
