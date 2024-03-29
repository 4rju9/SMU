package com.example.arjun.su_bca.RoomDatabase;

import java.util.List;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

@Dao()
public interface TimeTableDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertTimeTable(TimeTableEntitiy timeTable);

    @Query("SELECT * FROM time_table WHERE day = :day ORDER BY id ASC")
    List<TimeTableEntitiy> getAllTimeTable(String day);

    @Query("DELETE FROM time_table")
    void deleteAllTimeTable();

}
