package com.example.rotateclock;

import static androidx.appcompat.app.AppCompatDelegate.MODE_NIGHT_NO;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.fragment.app.DialogFragment;

import android.app.TimePickerDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.TimePicker;

import java.time.LocalDateTime;
import java.time.ZonedDateTime;
import java.util.Locale;

public class MainActivity extends AppCompatActivity implements TimePickerDialog.OnTimeSetListener{

    volatile float rotateDegree = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        AppCompatDelegate.setDefaultNightMode(MODE_NIGHT_NO);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                while (true){
                    LocalDateTime localDateTime = LocalDateTime.now();
                    onTimeChanged(localDateTime.getHour(),localDateTime.getMinute());
                    try {
                        Thread.sleep(1000);
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                }
            }
        });
        thread.start();

    }
    public void showTimePickerDialog(View v) {
        DialogFragment newFragment = new TimePickerFragment();
        newFragment.show(getSupportFragmentManager(), "timePicker");
    }
    //To rotate clock
    @Override
    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
        TextView textView = (TextView) findViewById(R.id.textView2);
        String str = String.format(Locale.US,"%02d:%02d",hourOfDay,minute);//Time when I woke up, i wish i could wake up at 7 o'clock.
        textView.setText(str);
        ImageView imageView = (ImageView) findViewById(R.id.imageView);
        hourOfDay %= 12;
        hourOfDay -= 7;
        rotateDegree = (float) (0-(hourOfDay*60+minute)*0.5);
        imageView.setRotation(rotateDegree);//degree
    }
    //To show Clock
    public void onTimeChanged(int hourOfDay, int minute){
        ImageView hourPointer = (ImageView) findViewById(R.id.hourPointer);
        ImageView minutePointer = (ImageView) findViewById(R.id.minutePointer);

        hourOfDay %= 12;
        hourPointer.setRotation(hourOfDay*30+rotateDegree);
        Switch toggle = (Switch) findViewById(R.id.switch1);
        minutePointer.setRotation((float) (minute*6)+((toggle.isChecked())?rotateDegree:0));
    }
}