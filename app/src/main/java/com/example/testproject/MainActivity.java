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
    //to enter the medicine name
    EditText mdName;
    // to enter date
    TextView d;

    //date image (date picker)
    ImageView date;


    //spinner to display Time of day
    Spinner spinner;

    //button to insert and alarm
    Button insert;

    //SQLite database to insert the medicine
    DBHelper db;


    String txtTime;//time of day


    //    Calendar
    private Calendar mCalendar;

    //    DatePickerDialog
    private int mYear, mMonth, mHour, mMinute, mDay;

    //    String txtDate;
    private String mDate;

    private String mTime;
    private String finalized_date_time;

    String timeOfDay;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        mdName = findViewById(R.id.mdName);//medicine name
        date = findViewById(R.id.date);//date
        d = findViewById(R.id.tvdate);//time of day

        //spinner for dropdown list to select time of day
        spinner = findViewById(R.id.spinner);

        //list to display time of day
        List<String> categories = new ArrayList<>();
        //hint (select) for spinner
        categories.add(0,"Select");
        //dropdown
        categories.add("Morning");//@8:00
        categories.add("Afternoon");//1:00
        categories.add("Evening");//6:30
        categories.add("Night");//10:30

        //insert button
        insert = findViewById(R.id.insert);


        //SQLite database
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
                if(parent.getItemAtPosition(position).equals("Select"))//select (hint text)
                {
                    // do nothing
                }
                else
                {
                    //on selecting a spinner
                    String item = parent.getItemAtPosition(position).toString();

                    //show selected spinner item
                    Toast.makeText(parent.getContext(),"Time of day: "+item, Toast.LENGTH_SHORT).show();


                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                //auto generated method...
            }
        }
        );



        //date format
        mCalendar = Calendar.getInstance();
        mHour = mCalendar.get(Calendar.HOUR_OF_DAY);
        mMinute = mCalendar.get(Calendar.MINUTE);
        mYear = mCalendar.get(Calendar.YEAR);
        mMonth = mCalendar.get(Calendar.MONTH) + 1;
        mDay = mCalendar.get(Calendar.DATE);

        mDate = mDay + "/" + mMonth + "/" + mYear;
        mTime = mHour + ":" + mMinute;

        d.setText(mDate);



        //inserting data......
        insert.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Spinner
                String t = spinner.getSelectedItem().toString();
                if(t.equals("Morning")){
                    timeOfDay = "Morning";
                    mHour = 8;
                    mMinute = 00;
                    txtTime = mHour + ":" + mMinute;

                }
                else if(t.equals("Afternoon")){
                    timeOfDay = "Afternoon";
                    mHour = 13;
                    mMinute = 00;
                    txtTime = mHour + ":" + mMinute;

                }
                else if(t.equals("Evening")){
                    timeOfDay = "Evening";
                    mHour = 18;
                    mMinute = 30;
                    txtTime = mHour + ":" + mMinute;

                }
                else{
                    timeOfDay = "Night";
                    mHour = 01;
                    mMinute = 15;
                    txtTime = mHour + ":" + mMinute;

                }

                finalized_date_time = mDate + " " + txtTime;

                //alarm and stuff
                //calender
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

                // setAlarm function
                setAlarm(cal);


                // database related(store the values)
                String MBName = mdName.getText().toString();//medicine name
                String Date = mDate;//date
                String Time = timeOfDay;//time of day


                //checking weather give data is inserted on not
                Boolean checkInsert = db.insertData(MBName,Date,Time);
                if(checkInsert == true){
                    Toast.makeText(MainActivity.this,"Data inserted successfully", Toast.LENGTH_SHORT).show();//on success message
                }else{
                    Toast.makeText(MainActivity.this,"Data Failed", Toast.LENGTH_SHORT).show();//failure message
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

        Toast.makeText(this, "Alarm Set", Toast.LENGTH_SHORT).show();
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
                now.get(Calendar.YEAR),//year
                now.get(Calendar.MONTH),//month
                now.get(Calendar.DAY_OF_MONTH)//day
        );
        dpd.show(getSupportFragmentManager(), "Datepickerdialog");//Datepickerdialog show
    }


    @Override
    public void onDateSet(DatePickerDialog view, int year, int monthOfYear, int dayOfMonth) {
        monthOfYear ++;
        mDay = dayOfMonth;
        mMonth = monthOfYear;
        mYear = year;
        mDate = dayOfMonth + "/" + monthOfYear + "/" + year;
        d.setText(mDate);//print the date
    }


}
