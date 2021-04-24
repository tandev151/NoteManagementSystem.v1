package com.example.notemanagement.DAO;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.notemanagement.Entity.Chart;
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

    @Query("DELETE FROM Status where statusId=:statusId ")
    void deleteStatusById(int statusId);

    @Query ("DELETE FROM Status")
    void deleteAll();

    @Query("SELECT * FROM Status")
    LiveData<List<Status>> getAll();

    @Query("SELECT * FROM Status WHERE accountid = (:accountid)")
    LiveData<List<Status>> getStatusByAccountId(int accountid);

//    @Query("SELECT statusId, count(NoteId) as amount FROM Note WHERE userId = (:idUser) group by statusID ")
//    List<Chart> getStatusNoteById(int idUser);

    @Query("SELECT * FROM Status where name = :name")
    LiveData<List<Status>> getStatusByName(String name);

    @Query("SELECT * FROM Status")
    List<Status> getAllList();
}

