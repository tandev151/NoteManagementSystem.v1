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

    @Query("Select * from Account where id = (:UserId)")
    Account getUserById(int UserId);

    @Query("Select name from Status where statusid = (:StatusId)")
    String getNameStatusById(int StatusId);

//    @Query("Select Name from Priority where PriorityId = (:PriorityId)")
//    String getNamePriorityById(int PriorityId);

    @Query("Select name from Category where categoryId = (:CategoryId)")
    String getNameCategoryById(int CategoryId);

    @Query("Select * from Category where categoryId = (:CategoryId)")
    Category getCategory(int CategoryId);

    @Query("Select * from Note")
    List<Note> getAll();

    @Query("Select * from Note where UserId = :UserId")
    List<Note> getAll(int UserId);

    @Query("Select * from Note where UserId = :UserId")
    LiveData<List<Note>> getAllByUserId(int UserId);

    @Query("SELECT Status.name,count(*) from Note,Status " +
            "WHERE Note.UserId =:UserId and Status.statusId =Note.StatusId GROUP by Status.name")
    Cursor getNameAndCountNoteByStatus(int UserId);

    @Query("Select StatusId,Count(*) from Note where UserId = :UserId group by StatusId")
    Cursor getCountNoteByStatus(int UserId);
    @Delete
    void deleteNote(Note note);

    @Update
    void update(Note note);


//    @Query("Update Note set Name = :Name, CategoryId = :CategoryId, PriorityId = :PriorityId, StatusId =:StatusId," +
//            "PlanDate = :PlanDate, CreateDate =:CreateDate where NoteId = :NoteId")
//    void updateNote(Integer NoteId, String Name, Integer CategoryId, Integer PriorityId, Integer StatusId, Date PlanDate, Date CreateDate);
}
