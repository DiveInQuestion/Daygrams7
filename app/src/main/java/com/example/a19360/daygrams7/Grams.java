package com.example.a19360.daygrams7;

import java.io.Serializable;

public class Grams implements Serializable
{
    public int getYear(){
        return this.year;
    }

    public void setYear(int year){
        this.year = year;
    }

    public int getMonth(){
        return this.month;
    }

    public void setMonth(int month){
        this.month = month;
    }

    public int getWeek() {
        return week;
    }

    public void setWeek(int week) { this.week = week; }

    public int getDay() {
        return day;
    }

    public void setDay(int day) {
        this.day = day;
    }

    public String getEditText() {
        return editText;
    }

    public void setEditText(String editText) {
        this.editText = editText;
    }

    private int year;
    private int month;
    private int week;
    private int day;
    private String editText=null;
}