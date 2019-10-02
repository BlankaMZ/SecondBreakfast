package com.example.blani.secondbreakfast;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import static java.lang.Math.abs;
import static java.lang.Math.min;
import static java.lang.Math.round;

public class OptionsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_options);
    }


    public void saveNewDay(View view) {

        EditText apples = findViewById(R.id.editTextNoApple);
        EditText sandwiches = findViewById(R.id.editTextNoSandwich);
        EditText others = findViewById(R.id.editTextNoOthers);
        EditText hour = findViewById(R.id.editTextHour);
        EditText minute = findViewById(R.id.editTextMinute);
        EditText alarms = findViewById(R.id.editTextNoAlarms);

        TextView warning = findViewById(R.id.textViewWarning);
        if (!checkQuantity(apples)) {
            warning.setText("Number of apples should be 0-50");
        } else if (!checkQuantity(sandwiches)) {
            warning.setText("Number of sandwiches should be 0-50");
        } else if (!checkQuantity(others)) {
            warning.setText("Number of others should be 0-50");
        } else if (!checkTime(hour, "hour")){
            warning.setText("Hour should be 0-23");
        } else if (!checkTime(minute,"minute")){
            warning.setText("Minutes should be 0-59");
        } else if (!checkAlarms(alarms)) {
            warning.setText("No more alarms than 5");
        }
        else {
            //int [] arrayOfQuantities = insertFood(apples, sandwiches, others);
            warning.setText(String.valueOf(getCurrentTime()[0])+String.valueOf(getCurrentTime()[1]));
            setAlarms(hour, minute, alarms);
        }
    }

    private int [] insertFood(EditText apples, EditText sandwiches, EditText others) {
        // musimy insertnąć do bazy jedzenie - znać wartość
        int applesInt = checkQuantityInt(apples);
        int sandwichesInt = checkQuantityInt(sandwiches);
        int othersInt = checkQuantityInt(others);

        int [] arrayOfInt = {applesInt, sandwichesInt, othersInt};
        return arrayOfInt;
    }

    private void setAlarms(EditText hour, EditText minute, EditText alarms){

        int [] currentTime = getCurrentTime();
        int alarmsInt = checkTimeInt(alarms);
        int [] periodTime = getPeriodTime(hour, minute, currentTime);
        int [] periodWindowsTimeInt = calculateWindows(periodTime, alarmsInt);
        int [][] alarmsTimes = getAlarmsTime(periodWindowsTimeInt, currentTime, alarmsInt);

        // musimy obliczyć godziny alarmów i je zainstalować
    }

    private int [] calculateWindows(int[] periodTime, int alarmsInt) {

        int [] windowsHourMinute = new int [2];
        int minutesOfPeriodTime = 60 * periodTime[0] + periodTime[1];
        double periodWindowsTime = (double) minutesOfPeriodTime/alarmsInt;
        Log.i("Minutes", String.valueOf(minutesOfPeriodTime));
        int periodWindowsTimeInt = (int)round(periodWindowsTime);
        Log.i("Windows", String.valueOf(periodWindowsTimeInt));
        int hour = periodWindowsTimeInt/60;
        Log.i("Hour", String.valueOf(hour));
        int minute = periodWindowsTimeInt%60;
        Log.i("Minute", String.valueOf(minute));

        windowsHourMinute[0] = hour;
        windowsHourMinute[1] = minute;
        return windowsHourMinute;
    }

    private int [][] getAlarmsTime(int [] periodWindowsTimeInt, int [] currentTime, int alarmsInt) {
        int [][] alarmsHours = new int [alarmsInt][2];
        int [] startTime = currentTime;
        for(int alarmNumber = 0; alarmNumber < alarmsInt; alarmNumber++){
            int hour = startTime[0] + periodWindowsTimeInt[0];
            int minute = startTime[1] + periodWindowsTimeInt[1];
            if(hour > 23) hour -= 24;
            if(minute>60){
                hour += 1;
                minute -=60;
            }
            alarmsHours[alarmNumber][0] = hour;
            alarmsHours[alarmNumber][1] = minute;
            startTime[0] = hour;
            startTime[1] = minute;
            Log.i("AlarmNumber", String.valueOf(alarmNumber));
            Log.i("AlarmHour", String.valueOf(hour));
            Log.i("AlarmMinute", String.valueOf(minute));

        }
        return alarmsHours;
    }

    private int[] getPeriodTime(EditText hour, EditText minute, int [] currentTime) {

        int minuteInt = checkTimeInt(minute);
        int hourInt = checkTimeInt(hour);
        int minuteDifference = abs(minuteInt - currentTime[1]);
        int [] periodTime = new int[2];

        if(hourInt> currentTime[0]){
            if(minuteInt >= currentTime[1]) {
                periodTime[0] = hourInt - currentTime[0];
                periodTime[1] = minuteDifference;
                Log.i("HK>HP,MK>MP", String.valueOf(periodTime[0])+ String.valueOf(periodTime[1]));
            }
            else{
                periodTime[0] = hourInt - currentTime[0] - 1;
                periodTime[1] = 60 - minuteDifference;
                Log.i(" >HP,MK<MP", String.valueOf(periodTime[0])+ String.valueOf(periodTime[1]));
            }
        } else if (hourInt < currentTime[0]){
            if(minuteInt >= currentTime[1]){
                periodTime[0] = 24 - currentTime[0] + hourInt;
                periodTime[1] = minuteDifference;
                Log.i("HK<HP,MK>MP", String.valueOf(periodTime[0])+ String.valueOf(periodTime[1]));
            }
            else {
                periodTime[0] = 24 - currentTime[0] + hourInt -1;
                periodTime[1] = 60 - currentTime[1];
                Log.i("HK>HP,MK<MP", String.valueOf(periodTime[0])+ String.valueOf(periodTime[1]));
            }
        } else if (hourInt == currentTime[0]){
            if(minuteInt == currentTime[1]){
                periodTime[0] = 24;
                periodTime[1] = 0;
                Log.i("HK=HP,MK=MP", String.valueOf(periodTime[0])+ String.valueOf(periodTime[1]));
            }
            else if(minuteInt > currentTime[1]){
                periodTime[0] = 0;
                periodTime[1] = minuteDifference;
                Log.i("HK=HP,MK>MP", String.valueOf(periodTime[0])+ String.valueOf(periodTime[1]));
            }
            else{
                periodTime[0] = 23;
                periodTime[1] = 60 - minuteDifference;
                Log.i("HK=HP,MK<MP", String.valueOf(periodTime[0])+ String.valueOf(periodTime[1]));
            }
        }
        return periodTime;
    }

    public int [] getCurrentTime() {
        Calendar calendar = Calendar.getInstance();
        int currentHourIn24Format = calendar.get(Calendar.HOUR_OF_DAY);
        int currentMinutes = calendar.get(Calendar.MINUTE);

        int [] currentTime = {currentHourIn24Format, currentMinutes};

        return currentTime;
    }

    private boolean checkQuantity(EditText quantity) {
        String quantityValue = String.valueOf(quantity.getText());
        int quantityValueInt = Integer.valueOf(quantityValue);
        if (quantityValueInt >= 0 && quantityValueInt <= 50) return true;
        else return false;
    }

    private int checkQuantityInt(EditText quantity) {
        String quantityValue = String.valueOf(quantity.getText());
        int quantityValueInt = Integer.valueOf(quantityValue);
        return quantityValueInt;
    }

    private boolean checkTime(EditText time, String hourOrMinute) {
        String timeValue = String.valueOf(time.getText());
        int timeValueInt = Integer.valueOf(timeValue);
        if (hourOrMinute == "hour") {
            if (timeValueInt >= 0 && timeValueInt < 24) return true;
            else return false;
        } else {
            if (timeValueInt >= 0 && timeValueInt < 60) return true;
            else return false;
        }
    }

    private int checkTimeInt(EditText time) {
        String timeValue = String.valueOf(time.getText());
        int timeValueInt = Integer.valueOf(timeValue);
        return timeValueInt;
    }

    private boolean checkAlarms(EditText alarms) {
        String alarmsValue = String.valueOf(alarms.getText());
        int alarmsValueInt = Integer.valueOf(alarmsValue);
        if (alarmsValueInt >= 0 && alarmsValueInt <= 5) return true;
        else return false;
    }


}

