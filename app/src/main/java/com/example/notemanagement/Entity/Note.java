package com.example.notemanagement.Entity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.room.ColumnInfo;
import androidx.room.Dao;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;
import androidx.room.TypeConverter;
import androidx.room.TypeConverters;

import com.example.notemanagement.Convert;

import java.sql.Time;
import java.util.Date;

@Entity(tableName = "Note")
//@Entity(tableName = "Note", foreignKeys = {@ForeignKey
//        (entity = Status.class,
//        parentColumns = "StatusId",
//        childColumns = "StatusId"
//
//), @ForeignKey
//        (entity = Category.class,
//                parentColumns = "CategoryId",
//                childColumns = "CategoryId"
//
//        ),
//
//        @ForeignKey
//                (entity = Priority.class,
//                        parentColumns = "PriorityId",
//                        childColumns = "PriorityId"
//
//                ),
//
//        @ForeignKey
//                (entity = User.class,
//                        parentColumns = "UserId",
//                        childColumns = "UserId"
//
//                )
//}
//)
//

public class Note {

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "NoteId")
    Integer NoteId;

    @ColumnInfo(name = "Name")
    String Name;

    @ColumnInfo(name = "CategoryId")
    Integer CategoryId;

    @ColumnInfo(name = "PriorityId")
    Integer PriorityId;

    @ColumnInfo(name = "StatusId")
    Integer StatusId;

    @Nullable
    @ColumnInfo(name = "PlanDate")
    Date PlanDate;

    @Nullable
    @ColumnInfo(name = "CreateDate")
    Date CreateDate;

    @ColumnInfo(name = "AccountId")
    Integer AccountId;

    public Integer getAccountId() {     return AccountId;  }

    public void setAccountId(Integer accountId) {   AccountId = accountId; }

    public Integer getNoteId() {
        return NoteId;
    }

    public void setNoteId(Integer noteId) {
        NoteId = noteId;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public Integer getCategoryId() {
        return CategoryId;
    }

    public void setCategoryId(Integer categoryId) {
        CategoryId = categoryId;
    }

    public Integer getPriorityId() {
        return PriorityId;
    }

    public void setPriorityId(Integer priorityId) {
        PriorityId = priorityId;
    }

    public Integer getStatusId() {
        return StatusId;
    }

    public void setStatusId(Integer statusId) {
        StatusId = statusId;
    }

    public Date getPlanDate() {
        return PlanDate;
    }

    public void setPlanDate(Date planDate) {
        PlanDate = planDate;
    }


    public Date getCreateDate() {
        return CreateDate;
    }

    public void setCreateDate(Date createDate) {
        CreateDate = createDate;
    }

    public String getUserName(Account user){
        return "";
    }

    public Note(){}

    public Note(String name, Integer categoryId, Integer priorityId, Integer statusId, Date planDate, Date createDate, Integer accountId) {

        Name = name;
        CategoryId = categoryId;
        PriorityId = priorityId;
        StatusId = statusId;
        PlanDate = planDate;
        CreateDate = createDate;
        AccountId = accountId;
    }
}
