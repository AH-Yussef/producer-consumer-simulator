package com.example.backend;

import com.example.backend.componenetsInfo.MachineInfo;
import com.example.backend.componenetsInfo.QueueInfo;
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

     public MachineInfo[] jsonToMachineInfo(String jsonStr){
         Gson gson = new Gson();
         return gson.fromJson(jsonStr, MachineInfo[].class);
     }

     public QueueInfo[] jsonToQueueInfo(String jsonStr){
         Gson gson = new Gson();
         return gson.fromJson(jsonStr, QueueInfo[].class);
     }
}
