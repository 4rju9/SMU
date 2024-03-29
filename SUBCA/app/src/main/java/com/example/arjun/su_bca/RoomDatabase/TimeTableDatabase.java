package com.example.arjun.su_bca.RoomDatabase;

import androidx.room.Database;
import androidx.room.RoomDatabase;

@Database(entities = {TimeTableEntitiy.class}, version = 1, exportSchema = false)
public abstract class TimeTableDatabase extends RoomDatabase {

    public abstract TimeTableDao getTimeTableDao();

}
