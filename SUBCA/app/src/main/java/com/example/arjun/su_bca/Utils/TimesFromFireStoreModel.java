package com.example.arjun.su_bca.Utils;

import java.util.List;

public class TimesFromFireStoreModel {

    private String name;
    private List<String> times;

    public TimesFromFireStoreModel() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<String> getTimes() {
        return times;
    }

    public void setTimes(List<String> times) {
        this.times = times;
    }
}
