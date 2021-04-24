package com.example.notemanagement.Entity;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

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
    private Time createDate;

    public void setCreateDate(@NonNull Time createDate) {
        this.createDate = createDate;
    }

    @NonNull
    public Time getCreateDate() {
        return createDate;
    }

    @NonNull
    @Override
    public String toString() {
        return getName();
    }
}
