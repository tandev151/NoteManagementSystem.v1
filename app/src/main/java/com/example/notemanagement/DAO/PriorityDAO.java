package com.example.notemanagement.DAO;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Index;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.example.notemanagement.Entity.Priority;

import java.util.List;

@Dao
public interface PriorityDAO {
    @Insert(onConflict = OnConflictStrategy.ABORT)
    void insert(Priority priority);

    @Query("SELECT * FROM priority WHERE AccountId = (:idAccount) AND IsDeleted=0")
    LiveData<List<Priority>> getPriorityById(int idAccount);

    @Update
    void update(Priority priority);

    @Query("UPDATE Priority SET Name= (:namePriority) WHERE PriorityId= (:priorityId) AND IsDeleted=0")
    void updateNamePriority(int priorityId, String namePriority);

    @Query("UPDATE Priority SET IsDeleted=1 where PriorityId=:priorityId ")
    void deletePriorityById(int priorityId);

    @Query("UPDATE Priority SET IsDeleted=1")
    void deleteAll();

    @Query("SELECT * FROM priority WHERE IsDeleted=0")
    LiveData<List<Priority>> getAll();

    @Query("SELECT * FROM priority WHERE IsDeleted=0")
    List<Priority> getAllList();

    @Query("SELECT * FROM priority WHERE AccountId = (:accountId) AND IsDeleted=0")
    List<Priority> getAllByUserId(int accountId);

    @Query("SELECT * FROM Priority WHERE Name = (:namePriority) AND IsDeleted=0")
    Priority getPriorityByName(String namePriority);
}
