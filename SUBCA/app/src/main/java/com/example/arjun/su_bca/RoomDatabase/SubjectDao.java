package com.example.arjun.su_bca.RoomDatabase;

import java.util.List;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

@Dao
public interface SubjectDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAll(SubjectEntity subjects);

    @Query("SELECT * FROM subjects")
    List<SubjectEntity> getAllSubjects();

    @Query("DELETE FROM subjects")
    void deleteAllSubjects();

}