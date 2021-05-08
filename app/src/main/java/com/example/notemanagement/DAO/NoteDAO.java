package com.example.notemanagement.DAO;

import android.database.Cursor;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;

import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.notemanagement.Entity.Note;

import java.util.List;

@Dao
public interface NoteDAO {

    @Insert
    void createNewNote(Note note);

    @Query("Select * from Note where AccountId = :accountId and IsDeleted = 0")
    LiveData<List<Note>> getAllByUserId(int accountId);

    @Query("SELECT Status.name,count(*) from Note,Status " +
            "WHERE Note.AccountId =:accountId and Status.StatusId =Note.StatusId GROUP by Status.Name")
    Cursor getNameAndCountNoteByStatus(int accountId);

    @Update
    void update(Note note);

    @Query("UPDATE note SET IsDeleted= 1 where NoteId = (:noteId)")
    void deleteById(int noteId);

}
