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
    @ColumnInfo(name="statusId")
    private int statusId;

    @ColumnInfo(name = "name")
    private String name;

    @ColumnInfo(name = "createDate")
    @TypeConverters(Convert.class)
    private Date  createDate;

    @ColumnInfo(name="accountid")
    private int accountid;

    public Status() {
    }

    public Status(int statusId, String name, Date createDate) {
        this.statusId = statusId;
        this.name = name;
        this.createDate = createDate;
    }

    public Status(String name, Date createDate, int accountid) {
        this.name = name;
        this.createDate = createDate;
        this.accountid= accountid;
    }

    public int getStatusId() {
        return statusId;
    }

    public void setStatusId(int statusId) {
        this.statusId = statusId;
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

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    @NonNull
    @Override
    public String toString() {
        return getName();
    }

    public int getAccountid() {
        return accountid;
    }

    public void setAccountid(int accountid) {
        this.accountid = accountid;
    }
}
