package com.example.arjun.su_bca.RoomDatabase;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

@Entity(tableName = "subjects")
public class SubjectEntity {

    @PrimaryKey(autoGenerate = true)
    @NonNull
    private int id;
    private String name;
    private String uname;

    @Ignore
    public SubjectEntity() {
        // Default constructor required for Room
    }

    public SubjectEntity(String name, String uname) {
        this.name = name;
        this.uname = uname;
    }

    @NonNull
    public int getId() {
        return id;
    }

    public void setId(@NonNull int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUname() { return uname; }

    public void setUname(String uname) { this.uname = uname; }
}
