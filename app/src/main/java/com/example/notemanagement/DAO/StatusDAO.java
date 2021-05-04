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

    @Query("UPDATE Status SET IsDeleted=1 where statusId=:statusId ")
    void deleteStatusById(int statusId);

    @Query ("UPDATE Status SET IsDeleted=1 ")
    void deleteAll();

    @Query("SELECT * FROM Status WHERE IsDeleted=0")
    LiveData<List<Status>> getAll();

    @Query("SELECT * FROM Status WHERE AccountId = (:accountId) AND IsDeleted=0")
    LiveData<List<Status>> getStatusByAccountId(int accountId);

    @Query("UPDATE Status SET Name= (:nameStatus) WHERE StatusId= (:statusId) AND IsDeleted=0")
    void updateNameStatus(int statusId, String nameStatus);

    @Query(("SELECT * FROM Status WHERE Name= (:nameStatus) AND IsDeleted=0"))
    Status getStatusByNameDuplicate(String nameStatus);

    @Query("SELECT * FROM Status WHERE AccountId = (:accountId) AND IsDeleted=0")
   List<Status> getAllByUserId(int accountId);
}

