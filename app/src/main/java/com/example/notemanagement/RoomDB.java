package com.example.notemanagement;

import android.content.Context;

import android.renderscript.RenderScript;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverter;
import androidx.room.TypeConverters;

import com.example.notemanagement.DAO.AccountDAO;
import com.example.notemanagement.DAO.CategoryDAO;
import com.example.notemanagement.DAO.PriorityDAO;
import com.example.notemanagement.DAO.StatusDAO;
import com.example.notemanagement.Entity.Account;
import com.example.notemanagement.Entity.Category;
import com.example.notemanagement.Entity.Priority;
import com.example.notemanagement.DAO.NoteDAO;
import com.example.notemanagement.DAO.StatusDAO;
import com.example.notemanagement.Entity.Account;
import com.example.notemanagement.Entity.Category;
import com.example.notemanagement.Entity.Note;
import com.example.notemanagement.Entity.Status;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


@Database(entities = {Account.class, Note.class, Status.class, Category.class},version = 1, exportSchema = false)
@TypeConverters(Convert.class)
public abstract class RoomDB  extends RoomDatabase {

    //Create database instance
    public abstract AccountDAO accountDAO();

    public abstract StatusDAO statusDAO();

    public abstract PriorityDAO priorityDAO();
    public abstract NoteDAO noteDAO();
    public abstract CategoryDAO categoryDAO();

    private static String dbName = "notedatabase1";
    private static volatile RoomDB INSTANCE;
    private static final int NUMBER_OF_THREADS = 4;

    public static final ExecutorService databaseWriteExecutor =
            Executors.newFixedThreadPool(NUMBER_OF_THREADS);

    public static synchronized RoomDB getDatabase(final Context context) {
        if (INSTANCE == null) {
            synchronized (RoomDB.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                            RoomDB.class, dbName).build();
                }
            }
        }
        return INSTANCE;
    }

}