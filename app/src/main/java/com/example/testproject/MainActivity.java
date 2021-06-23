package com.example.testproject;

import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;
import androidx.appcompat.app.AppCompatActivity;


import android.content.ComponentName;
import android.content.pm.PackageManager;
import android.os.Bundle;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;

import android.os.SystemClock;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;


public class MainActivity extends AppCompatActivity implements  AdapterView.OnItemSelectedListener, DatePickerDialog.OnDateSetListener{

    EditText mdName;
    TextView d;
    ImageView date;
//    int year,month,day;
    Spinner spinner;
    Button insert;
    DBHelper db;
    int time;
    String txtTime;
//    Calendar calendar;
//    DatePickerDialog dpd;
//    String txtDate;

    private Calendar mCalendar;
    private int mYear, mMonth, mHour, mMinute, mDay;
    private String mDate;
    private String mTime;
    private String finalized_date_time;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mdName = findViewById(R.id.mdName);
        date = findViewById(R.id.date);
        d = findViewById(R.id.tvdate);
        spinner = findViewById(R.id.spinner);

        List<String> categories = new ArrayList<>();
        categories.add(0,"Select");
        categories.add("Morning");
        categories.add("Afternoon");
        categories.add("Evening");
        categories.add("Night");

        insert = findViewById(R.id.insert);
        db = new DBHelper(this);

        //spinner
        //style and populate spinner
        ArrayAdapter<String> adapter;
            adapter   = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item,categories);
       //dropdown layout style
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        //attaching data adapter to spinner
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if(parent.getItemAtPosition(position).equals("Select"))
                {
                    // do northing
                }
                else
                {
                    //on selecting a spinner
                    String item = parent.getItemAtPosition(position).toString();

                    //show selected spinner item
                    Toast.makeText(parent.getContext(),"Selected: "+item, Toast.LENGTH_SHORT).show();


                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                //auto generated method stub
            }
        });



        //date
        mCalendar = Calendar.getInstance();
        mHour = mCalendar.get(Calendar.HOUR_OF_DAY);
        mMinute = mCalendar.get(Calendar.MINUTE);
        mYear = mCalendar.get(Calendar.YEAR);
        mMonth = mCalendar.get(Calendar.MONTH) + 1;
        mDay = mCalendar.get(Calendar.DATE);

        mDate = mDay + "/" + mMonth + "/" + mYear;
        mTime = mHour + ":" + mMinute;

        d.setText(mDate);



        //inserting
        insert.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Spinner
                String t = spinner.getSelectedItem().toString();
                if(t.equals("Morning")){
                    time = 1;
                    mHour = 5;
                    mMinute = 25;
                    txtTime = mHour + ":" + mMinute;
                }else if(t.equals("Afternoon")){
                    time = 2;
                    mHour = 15;
                    mMinute = 00;
                    txtTime = mHour + ":" + mMinute;
                }else if(t.equals("Evening")){
                    time = 3;
                    mHour = 18;
                    mMinute = 00;
                    txtTime = mHour + ":" + mMinute;
                }else{
                    time = 4;
                    mHour = 21;
                    mMinute = 00;
                    txtTime = mHour + ":" + mMinute;
                }



                finalized_date_time = mDate + " " + txtTime;

                //alarm and stuff
                Calendar cal = Calendar.getInstance();
                SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss", Locale.getDefault());
//                try {
//                    cal.setTime(sdf.parse(finalized_date_time));
//                } catch (ParseException e) {
//                    e.printStackTrace();
//                }
                cal.set(Calendar.MINUTE,mMinute);
                cal.set(Calendar.SECOND,0);
                cal.set(Calendar.HOUR_OF_DAY, mHour);
                cal.set(Calendar.MONTH,--mMonth);
                cal.set(Calendar.YEAR,mYear);
                cal.set(Calendar.DATE,mDay);

                //call the the setAlarm function
                setAlarm(cal);


                // db related stuffs
                String MBName = mdName.getText().toString();
                String Date = mDate;
                int Time = time;


                //checking weather inserted on not
                Boolean checkInsert = db.insertData(MBName,Date,Time);
                if(checkInsert == true){
                    Toast.makeText(MainActivity.this,"New Data inserted", Toast.LENGTH_SHORT).show();
                }else{
                    Toast.makeText(MainActivity.this,"ERROR......", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void setAlarm(Calendar calendar) {
        //getting the alarm manager
        AlarmManager am = (AlarmManager) getSystemService(Context.ALARM_SERVICE);

        //creating a new intent specifying the broadcast receiver
        Intent i = new Intent(this, MyAlarm.class);

        //creating a pending intent using the intent
        PendingIntent pi = PendingIntent.getBroadcast(this, 0, i, 0);

        Calendar c = Calendar.getInstance();
        long currentTime = c.getTimeInMillis();
        long diffTime = calendar.getTimeInMillis() - currentTime;

        am.set(AlarmManager.ELAPSED_REALTIME, SystemClock.elapsedRealtime() + diffTime,pi);

        // Restart alarm if device is rebooted

        Toast.makeText(this, "Alarm is set", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {
        //nothing
    }


    public void setDate(View v){
        Calendar now = Calendar.getInstance();
        DatePickerDialog dpd = DatePickerDialog.newInstance(
                this,
                now.get(Calendar.YEAR),
                now.get(Calendar.MONTH),
                now.get(Calendar.DAY_OF_MONTH)
        );
        dpd.show(getSupportFragmentManager(), "Datepickerdialog");
    }


    @Override
    public void onDateSet(DatePickerDialog view, int year, int monthOfYear, int dayOfMonth) {
        monthOfYear ++;
        mDay = dayOfMonth;
        mMonth = monthOfYear;
        mYear = year;
        mDate = dayOfMonth + "/" + monthOfYear + "/" + year;
        d.setText(mDate);
    }

}
