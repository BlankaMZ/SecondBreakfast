package com.example.blani.secondbreakfast;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.InputFilter;
import android.view.View;
import android.widget.TextView;


public class FoodActivity extends AppCompatActivity {

    private int numberOfApples;
    private int numberOfSandwiches;
    private int numberOfOther;
    MyDBHandler dbHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_food);
        dbHandler = new MyDBHandler(this);
        if(dbHandler.isTableEmpty()){
            dbHandler.addProduct("Fruits",0);
            dbHandler.addProduct("Sandwiches",0);
            dbHandler.addProduct("Others",0);
        }

        numberOfApples = dbHandler.findProductQuantity("Fruits").getQuantity();
        setNumberOfApples(numberOfApples);
        numberOfSandwiches = dbHandler.findProductQuantity("Sandwiches").getQuantity();
        setNumberOfSandwiches(numberOfSandwiches);
        numberOfOther = dbHandler.findProductQuantity("Others").getQuantity();
        setNumberOfOther(numberOfOther);

    }

    public void increaseApples(View view){
        if(numberOfApples < 50) numberOfApples += 1;
        setNumberOfApples(numberOfApples);
    }

    public void decreaseApples(View view){
        if(numberOfApples != 0) numberOfApples -= 1;
        setNumberOfApples(numberOfApples);
    }

    public void increaseSandwiches(View view){
        if(numberOfSandwiches < 50) numberOfSandwiches += 1;
        setNumberOfSandwiches(numberOfSandwiches);
    }

    public void decreaseSandwiches(View view){
        if(numberOfSandwiches != 0) numberOfSandwiches -= 1;
        setNumberOfSandwiches(numberOfSandwiches);
    }

    public void increaseOther(View view){
        if (numberOfOther < 50) numberOfOther += 1;
        setNumberOfOther(numberOfOther);
    }

    public void decreaseOther(View view){
        if (numberOfOther != 0) numberOfOther -= 1;
        setNumberOfOther(numberOfOther);
    }

    public void setNumberOfApples(int number){
        TextView noApples = findViewById(R.id.textApple);
        noApples.setText(String.valueOf(number));
        dbHandler.updateProductQuantity("Fruits", number);
    }

    public void setNumberOfSandwiches(int number){
        TextView noSandwiches = findViewById(R.id.textSandwich);
        noSandwiches.setText(String.valueOf(number));
        dbHandler.updateProductQuantity("Sandwiches", number);
    }

    public void setNumberOfOther(int number){
        TextView noOther = findViewById(R.id.textOther);
        noOther.setText(String.valueOf(number));
        dbHandler.updateProductQuantity("Others", number);
    }




}
