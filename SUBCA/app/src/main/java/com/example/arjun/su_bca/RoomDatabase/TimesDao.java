package com.example.arjun.su_bca.RoomDatabase;

import java.util.List;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

@Dao
public interface TimesDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertTimes(TimesEntity entity);

    @Query("SELECT * FROM times_table WHERE name = :name ORDER BY id ASC")
    List<TimesEntity> getTimes(String name);

    @Query("DELETE FROM times_table")
    void deleteTimes();

}
