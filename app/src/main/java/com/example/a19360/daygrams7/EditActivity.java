package com.example.a19360.daygrams7;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.Calendar;
import java.util.TimeZone;

public class EditActivity extends Activity {
    private EditText txt2;
    private RelativeLayout clock;
    private TextView Time,weekText,Done;
    private String str2,editText;
    private int week,month,day,year;
    private Calendar c = Calendar.getInstance();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        setContentView(R.layout.editview_item);
        c.setTimeZone(TimeZone.getTimeZone("GMT+8:00"));

        txt2=(EditText) findViewById(R.id.editText);
        clock =(RelativeLayout)findViewById(R.id.clock);
        Time =(TextView)findViewById(R.id.time);
        weekText = (TextView)findViewById(R.id.week);
        Done = (TextView)findViewById(R.id.Done);

        week = Integer.valueOf(getIntent().getStringExtra("week"));
        month = Integer.valueOf(getIntent().getStringExtra("month"));
        day = Integer.valueOf(getIntent().getStringExtra("day"));
        year = Integer.valueOf(getIntent().getStringExtra("year"));
        editText = getIntent().getStringExtra("text");

        weekText.setText(getWeek(week));
        if(week==1)
            weekText.setTextColor(android.graphics.Color.RED);
        Time.setText(getTime(month,day,year));
        txt2.setText(editText);
        clock.setOnClickListener(new mClick());
        Done.setOnClickListener(new done());
    }
    @Override
    public void onBackPressed() {
        str2 = txt2.getText().toString();
        Intent intent = new Intent();
        intent.setClass(EditActivity.this,MainActivity.class);
        Bundle bundle = new Bundle();
        bundle.putString("editText",str2);
        intent.putExtras(bundle);
        //Toast.makeText(getApplicationContext(),str2, Toast.LENGTH_SHORT).show();
        setResult(RESULT_OK,intent);
        finish();
    }
    class done implements View.OnClickListener
    {
        public void onClick(View v)
        {
            str2=txt2.getText().toString();
            //str=str2.substring(0,20);
            //Toast.makeText(getApplicationContext(),str2, Toast.LENGTH_SHORT).show();
            Intent intent = new Intent();
            intent.setClass(EditActivity.this,MainActivity.class);
            Bundle bundle = new Bundle();
            bundle.putString("editText",str2);
            intent.putExtras(bundle);
            //Toast.makeText(getApplicationContext(),str2, Toast.LENGTH_SHORT).show();
            setResult(RESULT_OK,intent);
            finish();
        }
    }
    class mClick implements View.OnClickListener{
        @Override
        public void onClick(View v) {
            String str =txt2.getText().toString()+getTime();
            txt2.setText(str);
            txt2.setSelection(str.length());//将光标移至文字末尾
        }
    }
    public String getTime(){
        String str="";
        int mHour,mMinute;
        mHour = c.get(Calendar.HOUR_OF_DAY);
        mMinute = c.get(Calendar.MINUTE);
        str=adjustTime(mHour,mMinute);
        return str;
    }
    public String adjustTime(int hour,int minute){
        String str;
        if(hour<12)
            str=adjustHour(hour)+":"+adjustMinute(minute)+"am";
        else
            str=adjustHour(hour)+":"+adjustMinute(minute)+"pm";
        return str;
    }
    public String adjustHour(int hour){
        String str;
        if(hour<10)
            str="0"+hour;
        else
            str=""+hour;
        return str;
    }
    public String adjustMinute(int Minute){
        String str;
        if(Minute<10)
            str="0"+Minute;
        else
            str=""+Minute;
        return str;
    }

    private String getTime(int month,int day,int year){
        String Time;
        Time = " / "+getMonth(month)+" "+day+" / "+year;
        return Time;
    }

    private String getWeek(int week) {

        String Week="";
        if (week == 1) {
            Week += "SUNDAY";
        }
        if (week == 2) {
            Week += "MONDAY";
        }
        if (week == 3) {
            Week += "TUESDAY";
        }
        if (week == 4) {
            Week += "WEDNESDAY";
        }
        if (week == 5) {
            Week += "THURSDAY";
        }
        if (week == 6) {
            Week += "FRIDAY";
        }
        if (week == 7) {
            Week += "SATURDAY";
        }
        return Week;
    }
    private String getMonth(int month) {

        String Month="";
        if (month == 1) {
            Month += "JANUARY";
        }
        if (month == 2) {
            Month += "FEBRUARY";
        }
        if (month == 3) {
            Month += "MARCH";
        }
        if (month == 4) {
            Month += "APRIL";
        }
        if (month == 5) {
            Month += "MAY";
        }
        if (month == 6) {
            Month += "JUNE";
        }
        if (month == 7) {
            Month += "JULY";
        }
        if (month == 8) {
            Month += "AUGUST";
        }
        if (month == 9) {
            Month += "SEPTEMBER";
        }
        if (month == 10) {
            Month += "OCTOBER";
        }
        if (month == 11) {
            Month += "NOVEMBER";
        }
        if (month == 12) {
            Month += "DECEMBER";
        }
        return Month;
    }
}
