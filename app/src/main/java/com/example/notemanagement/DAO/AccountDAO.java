package com.example.notemanagement.DAO;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.example.notemanagement.Entity.Account;
import com.example.notemanagement.Entity.Category;
import com.example.notemanagement.Entity.Priority;

import java.util.Calendar;

@Dao
public interface AccountDAO {

    @Query("Insert into Account(UserName,Password) values (:userName,:password)")
    void register(String userName, String password);

    @Insert
    void insert(Account account);
    @Update
    void update(Account account);

    @Query("Select * from account where UserName =:userName and Password = :password")
    Account login(String userName, String password);

    @Delete
    void delete(Account account);


    @Query("SELECT * FROM Account where id= :uid ")
    Account getUserById (Integer uid);

    @Query("SELECT * FROM Account where UserName= :email ")
    Account getUserByMail (String email);

    @Query("SELECT * FROM CATEGORY WHERE Name = (:nameCate)")
    Category getPriorityByName(String nameCate);
}
