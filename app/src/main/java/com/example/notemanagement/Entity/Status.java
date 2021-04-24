package com.example.notemanagement.Entity;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;
import androidx.room.TypeConverters;

import com.example.notemanagement.Convert;

import java.util.Calendar;
import java.util.Date;

@Entity(tableName="Status")
public class Status {
    @PrimaryKey(autoGenerate = true)
    @NonNull
    @ColumnInfo(name="statusid")
    private int statusid;

    @ColumnInfo(name = "name")
    private String name;

    @ColumnInfo(name = "createDate")
    @TypeConverters(Convert.class)
    private Date  createDate;

    public Status() {
    }

    public Status(int statusid, String name, Date createDate) {
        this.statusid = statusid;
        this.name = name;
        this.createDate = createDate;
    }

    public Status(String name, Date createDate) {
        this.name = name;
        this.createDate = createDate;
    }

    public int getStatusid() {
        return statusid;
    }

    public void setStatusid(int statusid) {
        this.statusid = statusid;
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
}
