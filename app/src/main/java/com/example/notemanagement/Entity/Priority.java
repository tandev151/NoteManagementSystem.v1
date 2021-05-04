package com.example.notemanagement.Entity;


import androidx.annotation.ContentView;
import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Index;
import androidx.room.PrimaryKey;
import androidx.room.TypeConverters;

import com.example.notemanagement.Convert;

import java.util.Date;
import java.util.jar.Attributes;

@Entity(tableName = "Priority", indices = {@Index(value = {"Name"},unique = true)})
@TypeConverters(Convert.class)
public class Priority {

    @PrimaryKey(autoGenerate = true)
    @NonNull
    @ColumnInfo(name = "PriorityId")
    private int PriorityId;

    @ColumnInfo(name = "Name")
    private String Name;

    @TypeConverters(Convert.class)
    @ColumnInfo(name = "CreateDate")
    private Date CreateDate;

    @ColumnInfo(name = "AccountId")
    private int AccountId;

    @ColumnInfo(name = "IsDeleted", defaultValue = "false")
    private boolean IsDeleted;

    public boolean IsDeleted() {
        return IsDeleted;
    }

    public void setIsDeleted(boolean deleted) {
        IsDeleted = deleted;
    }

    public int getAccountId() {
        return AccountId;
    }

    public void setAccountId(int accountId) {
        AccountId = accountId;
    }

    public Priority() {
    }

    public Priority(String name, Date createDate, int idAccount) {
        this.Name = name;
        this.CreateDate = createDate;
        this.AccountId = idAccount;
    }

    public int getPriorityId() {
        return PriorityId;
    }



    public void setPriorityId(int priorityId) {
        this.PriorityId = priorityId;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        this.Name = name;
    }

    public Date getCreateDate() {
        return CreateDate;
    }

    public void setCreateDate(Date createDate) {
        this.CreateDate = createDate;
    }


    @NonNull
    @Override
    public String toString() {
        return getName();
    }
}
