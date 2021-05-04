package com.example.notemanagement.Entity;

import androidx.annotation.NonNull;
import androidx.annotation.TransitionRes;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;

import androidx.room.TypeConverters;

import com.example.notemanagement.Convert;

import com.example.notemanagement.Convert;


import java.util.Date;

@Entity(tableName = "Category")
public class Category {
    @PrimaryKey (autoGenerate = true)
    @ColumnInfo(name = "CategoryId")
    private int CategoryId;

    @ColumnInfo(name = "Name")
    private String Name;

    @TypeConverters(Convert.class)
    @ColumnInfo(name = "CreateDate", defaultValue = "CURRENT_TIMESTAMP")
    private Date CreateDate;

    @ColumnInfo(name = "AccountId")
    private int AccountId;
    public Category(){

    }

    @ColumnInfo(name = "IsDeleted", defaultValue = "false")
    private boolean IsDeleted;

    public void setDeleted(boolean deleted) {
        IsDeleted = deleted;
    }

    public boolean IsDeleted() {
        return IsDeleted;
    }

    public void setIsDeleted(boolean deleteD) {
        IsDeleted = deleteD;
    }

    public Category(String name, Date createDate, int accountId) {
        this.Name = name;
        this.CreateDate = createDate;
        this.AccountId = accountId;

    }

    public int getCategoryId() {
        return CategoryId;
    }

    public void setCategoryId(int categoryId) {
        CategoryId = categoryId;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public Date getCreateDate() {
        return CreateDate;
    }

    public void setCreateDate(Date createDate) {
        CreateDate = createDate;
    }

    public int getAccountId() {
        return AccountId;
    }

    public void setAccountId(int accountId) {
        AccountId = accountId;
    }

    @NonNull
    @Override
    public String toString() {
        return getName();
    }
}
