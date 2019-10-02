package com.example.blani.secondbreakfast;

/**
 * Created by Blani! on 26.05.2018.
 */

public class Food {

    private String type;
    private int quantity;

    public Food(){}

    public Food(String type){
        this.type = type;
        this.quantity = 0;
    }

    public void setType(String type){
        this.type = type;
    }
    public void setQuantity(int quantity){
        this.quantity = quantity;
    }
    public String getType(){
        return this.type;
    }
    public int getQuantity(){
        return this.quantity;
    }


}
