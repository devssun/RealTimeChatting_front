package com.example.choihyesun.realtimechatting;

/**
 * Created by choihyesun on 15. 11. 9..
 */
public class MyItem {
    private String message;
    private String time;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public MyItem(String message, String time){
        this.message = message;
        this.time = time;
    }

    public MyItem(){

    }

}
