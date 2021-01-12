package com.example.backend.memento;

public class Memento{
    private String circuitInfo;

    public Memento(String circuitInfo) {
        this.circuitInfo = circuitInfo;
    }

    public String getMementoInfo(){
        return this.circuitInfo;
    }
}