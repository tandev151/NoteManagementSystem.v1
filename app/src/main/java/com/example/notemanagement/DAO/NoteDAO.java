package com.example.notemanagement.DAO;

import android.database.Cursor;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.notemanagement.Entity.Account;
import com.example.notemanagement.Entity.Category;
import com.example.notemanagement.Entity.Note;
import com.example.notemanagement.Entity.Status;

import java.util.ArrayList;
import java.util.List;

import javax.sql.DataSource;

@Dao
public interface NoteDAO {

    @Insert
    void createNewNote(Note note);

    @Query("Select * from Account where id = (:AccountId)")
    Account getUserById(int AccountId);

    @Query("Select name from Status where statusid = (:StatusId)")
    String getNameStatusById(int StatusId);

//    @Query("Select Name from Priority where PriorityId = (:PriorityId)")
//    String getNamePriorityById(int PriorityId);

    @Query("Select name from Category where CategoryId = (:CategoryId)")
    String getNameCategoryById(int CategoryId);

    @Query("Select * from Category where CategoryId = (:CategoryId)")
    Category getCategory(int CategoryId);

    @Query("Select * from Note where IsDeleted = 0")
    List<Note> getAll();

    @Query("Select * from Note where AccountId = :accountId and IsDeleted = 0")
    List<Note> getAll(int accountId);

    @Query("Select * from Note where AccountId = :accountId and IsDeleted = 0")
    LiveData<List<Note>> getAllByUserId(int accountId);

    @Query("SELECT Status.name,count(*) from Note,Status " +
            "WHERE Note.AccountId =:accountId and Status.StatusId =Note.StatusId GROUP by Status.Name")
    Cursor getNameAndCountNoteByStatus(int accountId);

    @Query("Select StatusId,Count(*) from Note where AccountId = :accountId group by StatusId")
    Cursor getCountNoteByStatus(int accountId);
    @Delete
    void deleteNote(Note note);

    @Update
    void update(Note note);

    @Query("UPDATE note SET IsDeleted= 1 where NoteId = (:noteId)")
    void deleteById(int noteId);

//    @Query("Update Note set Name = :Name, CategoryId = :CategoryId, PriorityId = :PriorityId, StatusId =:StatusId," +
//            "PlanDate = :PlanDate, CreateDate =:CreateDate where NoteId = :NoteId")
//    void updateNote(Integer NoteId, String Name, Integer CategoryId, Integer PriorityId, Integer StatusId, Date PlanDate, Date CreateDate);
}
