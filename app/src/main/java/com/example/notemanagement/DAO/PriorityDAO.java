package com.example.notemanagement.DAO;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.example.notemanagement.Entity.Priority;

import java.util.List;

@Dao
public interface PriorityDAO {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    void insert(Priority priority);

    @Query("SELECT * FROM priority WHERE userId = (:idUser)")
    LiveData<List<Priority>> getPriorityById(int idUser);

    @Update
    void update(Priority priority);

    @Query("DELETE FROM priority where name=:priorityName ")
    void deletePriorityByName (String priorityName);

    @Query("DELETE FROM priority where priorityid=:priorityId ")
    void deletePriorityById (int priorityId);

    @Query ("DELETE FROM priority")
    void deleteAll();

    @Query("SELECT * FROM priority")
    LiveData<List<Priority>> getAll();

    @Query("SELECT * FROM priority")
    List<Priority> getAllList();

}
