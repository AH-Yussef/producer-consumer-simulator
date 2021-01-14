package com.example.backend;


public class Product {
    Simulator simulator = Simulator.getInstance();
    private String color = new String();
    public Product(){
        this.color = simulator.getNewColor();
    }
    public String getColor(){
        return color;
    }
}
