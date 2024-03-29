package com.example.arjun.su_bca.RoomDatabase;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

@Entity(tableName = "times_table")
public class TimesEntity {

    @PrimaryKey(autoGenerate = true)
    @NonNull
    private int id;

    private String time;

    @ColumnInfo(name = "name")
    private String name;

    @Ignore
    public TimesEntity() {}

    public TimesEntity(String time, String name) {
        this.time = time;
        this.name = name;
    }

    @NonNull
    public int getId() {
        return id;
    }

    public void setId(@NonNull int id) {
        this.id = id;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
