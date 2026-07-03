package com.enceladus.enceladus.dto;

import org.aspectj.bridge.IMessage;

public class ApiError {

    private final String message;
    private final long timeStamp = System.currentTimeMillis();

    public ApiError(String message){
        this.message = message;
    }
    public String getMessage(){
        return message;
    }
    public long getTimeStamp(){
        return timeStamp;
    }
}
