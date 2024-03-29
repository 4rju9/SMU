package com.example.arjun.su_bca.RoomDatabase;

import java.util.List;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

@Dao
public interface SyllabusDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAll(SyllabusEntity Syllabus);

    @Query("SELECT * FROM syllabus WHERE uname = :uname ORDER BY id ASC")
    List<SyllabusEntity> getAllSubjects(String uname);

    @Query("DELETE FROM syllabus")
    void deleteAllSubjects();

}
