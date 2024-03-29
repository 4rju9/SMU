package com.example.arjun.su_bca.RoomDatabase;

import androidx.room.Database;
import androidx.room.RoomDatabase;

@Database(entities = {SubjectEntity.class}, version = 1, exportSchema = false)
public abstract class SubjectDatabase extends RoomDatabase {
    public abstract SubjectDao subjectDao();
}
