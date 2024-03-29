package com.example.arjun.su_bca.RoomDatabase;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;
@Entity(tableName = "time_table")
public class TimeTableEntitiy {

    @PrimaryKey(autoGenerate = true)
    @NonNull
    private int id;

    private String subject;
    private String time;

    @ColumnInfo(name = "day")
    private String day;

    @Ignore
    public TimeTableEntitiy() {

    }

    public TimeTableEntitiy(String subject, String time, String day) {
        this.subject = subject;
        this.time = time;
        this.day = day;
    }

    @NonNull
    public int getId() {
        return id;
    }

    public void setId(@NonNull int id) {
        this.id = id;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getDay() {
        return day;
    }

    public void setDay(String day) {
        this.day = day;
    }
}
