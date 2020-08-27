package com.example.anko.newconnect;

import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.TextView;
import android.widget.Toast;

public class ScheduleActivity extends AppCompatActivity {
    CalendarView calenderView;
    TextView myDate;
    Button schedule_btn;

    public String dateSelected;

    public String description;

    private SharedPreferences savedEvents;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_schedule);

        savedEvents = getSharedPreferences("myPrefs", MODE_PRIVATE);

        calenderView = (CalendarView) findViewById(R.id.calenderView);
        myDate = (TextView) findViewById(R.id.myDate);
        schedule_btn = (Button) findViewById(R.id.schedule_btn);

        calenderView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(@NonNull CalendarView calendarView, int i, int i1, int i2) {
                //String
                dateSelected = (i1 + 1) + "/" + i2 + "/" + i;
                myDate.setText(dateSelected);
            }
        });

        schedule_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setEventClick();
            }
        });
//code end
    }
    public void setEventClick()
    {
        if (savedEvents.getString(dateSelected, "") != "") //Launches Show activity if event already exists
        {

            description = savedEvents.getString(dateSelected, "");
            savedEvents = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
            SharedPreferences.Editor editor = savedEvents.edit();
            editor.putString(dateSelected, description);

            Toast.makeText(ScheduleActivity.this, "Saved", Toast.LENGTH_SHORT).show();

           // Intent show = new Intent(this, ShowActivity.class);
            //this.startActivity(show);
        }//end if

        else  //if date has no event, launches activity to create one
        {
            SharedPreferences.Editor editor = savedEvents.edit();
            editor.putString(dateSelected, "");
            editor.putString("thisDate", dateSelected);

            Toast.makeText(ScheduleActivity.this, "Please Select a Date", Toast.LENGTH_SHORT).show();


            //Intent create = new Intent(this, CreateActivity.class);
            //this.startActivity(create);
        }//end else
    }//end setEventClick
}
