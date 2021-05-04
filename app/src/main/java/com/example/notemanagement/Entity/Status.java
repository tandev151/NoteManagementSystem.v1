package com.example.notemanagement.Entity;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;
import androidx.room.TypeConverters;

import com.example.notemanagement.Convert;

import java.util.Calendar;
import java.util.Date;

@Entity(tableName="Status")
public class Status {
    @PrimaryKey(autoGenerate = true)
    @NonNull
    @ColumnInfo(name="StatusId")
    private int StatusId;

    @ColumnInfo(name = "Name")
    private String Name;

    @ColumnInfo(name = "CreateDate")
    @TypeConverters(Convert.class)
    private Date  CreateDate;

    @ColumnInfo(name="AccountId")
    private int AccountId;

    @ColumnInfo(name = "IsDeleted", defaultValue = "false")
    private boolean IsDeleted;

    public boolean IsDeleted() {
        return IsDeleted;
    }

    public void setIsDeleted(boolean deleteD) {
        IsDeleted = deleteD;
    }

    public Status() {
    }

    public Status(int statusId, String name, Date createDate) {
        this.StatusId = statusId;
        this.Name = name;
        this.CreateDate = createDate;
    }

    public Status(String name, Date createDate, int accountId) {
        this.Name = name;
        this.CreateDate = createDate;
        this.AccountId= accountId;
    }

    public int getStatusId() {
        return StatusId;
    }

    public void setStatusId(int statusId) {
        this.StatusId = statusId;
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

    public int getAccountId() {
        return AccountId;
    }

    public void setAccountId(int accountid) {
        this.AccountId = accountid;
    }
}
