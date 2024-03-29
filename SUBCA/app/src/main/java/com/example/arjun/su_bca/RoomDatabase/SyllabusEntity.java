package com.example.arjun.su_bca.RoomDatabase;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

@Entity(tableName = "syllabus")
public class SyllabusEntity {

    @PrimaryKey(autoGenerate = true)
    @NonNull
    private int id;
    private String title;
    private String syllabus;
    @ColumnInfo(name = "uname")
    private String uname;

    @Ignore
    public SyllabusEntity() {
        // Default constructor required for Room
    }

    public SyllabusEntity(String title, String syllabus, String uname) {
        this.title = title;
        this.syllabus = syllabus;
        this.uname = uname;
    }

    @NonNull
    public int getId() {
        return id;
    }

    public void setId(@NonNull int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getSyllabus() { return syllabus; }

    public void setSyllabus(String syllabus) { this.syllabus = syllabus; }

    public String getUname() { return uname; }

    public void setUname(String uname) { this.uname = uname; }

}
