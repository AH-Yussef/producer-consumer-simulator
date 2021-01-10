package com.example.backend;

import com.google.gson.Gson;

public class JsonConverter {
    public Machine[] jsonArrayToMachineArray(String jsonString){
        Gson gson = new Gson();
        return gson.fromJson(jsonString, Machine[].class);
    }
    public Queue[] jsonArrayToQueueArray(String jsonString){
        Gson gson = new Gson();
        return gson.fromJson(jsonString, Queue[].class);
    }
}
