package com.example.notemanagement.Entity;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;
import androidx.room.TypeConverters;

import com.example.notemanagement.Convert;

import java.util.Date;

@Entity(tableName = "Priority")
@TypeConverters(Convert.class)
public class Priority {

    @PrimaryKey(autoGenerate = true)
    @NonNull
    @ColumnInfo(name="priorityId")
    private int priorityId;

    @ColumnInfo(name = "name")
    private String name;

    @ColumnInfo(name = "createDate")
    private Date createDate;

    @ColumnInfo(name = "userId")
    @ForeignKey(entity = User.class, parentColumns = "userId", childColumns = "userId", onDelete = ForeignKey.CASCADE)
    private int userId;

    public Priority() {
    }

    public Priority(String name, Date createDate, int idUser) {
        this.name = name;
        this.createDate = createDate;
        this.userId=idUser;
    }

    public int getPriorityId() {
        return priorityId;
    }
    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }
    public void setPriorityId(int priorityId) {
        this.priorityId = priorityId;
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

}
