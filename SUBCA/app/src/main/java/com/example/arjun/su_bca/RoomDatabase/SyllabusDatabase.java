package com.example.arjun.su_bca.RoomDatabase;

import androidx.room.Database;
import androidx.room.RoomDatabase;

@Database(entities = {SyllabusEntity.class}, version = 1, exportSchema = false)
public abstract class SyllabusDatabase extends RoomDatabase {
    public abstract SyllabusDao syllabusDao();
}
