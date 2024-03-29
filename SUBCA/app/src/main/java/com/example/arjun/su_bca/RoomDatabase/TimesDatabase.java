package com.example.arjun.su_bca.RoomDatabase;

import androidx.room.Database;
import androidx.room.RoomDatabase;

@Database(entities = {TimesEntity.class}, version = 1, exportSchema = false)
public abstract class TimesDatabase extends RoomDatabase {

    public abstract TimesDao getTimesDao();

}
