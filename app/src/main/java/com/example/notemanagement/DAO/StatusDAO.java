package com.example.notemanagement.DAO;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.notemanagement.Entity.Status;

import java.util.List;

@Dao
public interface StatusDAO {
    @Insert
    void insert(Status status);

    @Update
    void update(Status status);

    @Query("DELETE FROM Status where name=:statusName ")
    void deleteStatusByName (String statusName);

    @Query("DELETE FROM Status where statusid=:statusid ")
    void deleteStatusById(int statusid);

    @Query ("DELETE FROM Status")
    void deletAll();

    @Query("SELECT * FROM Status")
    LiveData<List<Status>> getAll();

    @Query("SELECT * FROM Status where name = :name")
    LiveData<List<Status>> getStatusByName(String name);
}
