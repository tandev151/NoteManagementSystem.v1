package com.example.notemanagement.ui.category;

import androidx.room.TypeConverters;

import com.example.notemanagement.Convert;

import java.sql.Time;
import java.util.Date;

public class Category {
    private String name;
    @TypeConverters(Convert.class)
    private Date createDate;

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

    public Category(String name,Date createDate){
        this.name=name;
        this.createDate=createDate;
    }
}
