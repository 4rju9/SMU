package com.example.arjun.su_bca.Utils;

import java.util.List;

public class TimeTableFromFireStoreModel {

    private List<String> subject_names;
    private String day, time;

    public TimeTableFromFireStoreModel() {
    }

    public List<String> getSubject_names() {
        return subject_names;
    }

    public void setSubject_names(List<String> subject_names) {
        this.subject_names = subject_names;
    }

    public void setTime(String time) { this.time = time; }

    public String getTime() { return time; }

    public String getDay() {
        return day;
    }

    public void setDay(String day) {
        this.day = day;
    }
}
