package com.example.notemanagement.DAO;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.example.notemanagement.Entity.Account;

@Dao
public interface AccountDAO {

    @Query("Insert into Account(user_name,pass_word) values (:username,:password)")
    void register(String username, String password);

    @Insert
    void insert(Account account);

    @Update
    void update(Account account);

    @Query("Select * from account where user_name =:username and pass_word = :password")
    Account login(String username, String password);

    @Delete
    void delete(Account account);

    @Query("SELECT * FROM Account where id= :uid ")
    Account getUserById (Integer uid);

    @Query("SELECT * FROM Account where user_name= :email ")
    Account getUserByMail (String email);
}
