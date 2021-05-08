package com.example.notemanagement.DAO;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.notemanagement.Entity.Account;

@Dao
public interface AccountDAO {

    @Insert
    void insert(Account account);
    @Update
    void update(Account account);

    @Delete
    void delete(Account account);

    @Query("SELECT * FROM Account where UserName= :email ")
    Account getUserByMail (String email);

}
