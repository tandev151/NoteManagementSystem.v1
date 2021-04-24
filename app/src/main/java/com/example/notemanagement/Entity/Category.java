package com.example.notemanagement.Entity;

import androidx.annotation.NonNull;
import androidx.annotation.TransitionRes;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;
import androidx.room.TypeConverter;
import androidx.room.TypeConverters;

import com.example.notemanagement.Convert;

import java.util.Date;
import java.sql.Time;

@Entity(tableName = "Category")
public class Category {
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name="category_id")
    @NonNull
    private int categoryId;

    public void setCategoryId(int categoryId) {
        this.categoryId = categoryId;
    }

    public int getCategoryId() {
        return categoryId;
    }
    @ColumnInfo(name="name")
    @NonNull
    private String name;

    public void setName(@NonNull String name) {
        this.name = name;
    }

    @NonNull
    public String getName() {
        return name;
    }
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
    }


    @NonNull
    @Override
    public String toString() {
        return getName();
    }
}
