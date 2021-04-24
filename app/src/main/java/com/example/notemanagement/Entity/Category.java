package com.example.notemanagement.Entity;

import androidx.annotation.NonNull;
import androidx.annotation.TransitionRes;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;
<<<<<<< HEAD
import androidx.room.TypeConverters;

import com.example.notemanagement.Convert;
=======
import androidx.room.TypeConverter;
import androidx.room.TypeConverters;
>>>>>>> fa7cbb89d0a5392410634bc76dc5a5bf04e921af

import com.example.notemanagement.Convert;

import java.util.Date;
import java.sql.Time;
import java.util.Date;

@Entity(tableName = "Category")
public class Category {
    @PrimaryKey (autoGenerate = true)
    @ColumnInfo(name = "categoryId")
    private int categoryId;

    public void setCategoryId(int categoryId) {
        this.categoryId = categoryId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Date getCreateDate() {
        return createDate;
    }
<<<<<<< HEAD

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    @ColumnInfo(name = "name")
    private String name;

    @TypeConverters(Convert.class)
    @ColumnInfo(name = "createDate", defaultValue = "CURRENT_TIMESTAMP")
    private Date createDate;

    @ColumnInfo(name = "userId")
    @ForeignKey(entity = User.class, parentColumns = "userId", childColumns = "userId", onDelete = ForeignKey.CASCADE)
    private int userId;



    public int getCategoryId() {
        return categoryId;
    }

    public Category(String name, Date createDate, int userId) {
        this.name = name;
        this.createDate = createDate;
        this.userId = userId;
=======
    @ColumnInfo(name = "create_date")
    @NonNull
    @TypeConverters(Convert.class)
    private Date createDate;

    public void setCreateDate(@NonNull Date createDate) {
        this.createDate = createDate;
    }

    @NonNull
    @TypeConverters(Convert.class)
    public Date getCreateDate() {
        return createDate;
>>>>>>> fa7cbb89d0a5392410634bc76dc5a5bf04e921af
    }


    @NonNull
    @Override
    public String toString() {
        return getName();
    }
}
