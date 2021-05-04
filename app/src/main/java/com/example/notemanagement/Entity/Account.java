package com.example.notemanagement.Entity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "Account")
public class Account {
    @PrimaryKey(autoGenerate = true)
    @NonNull
    @ColumnInfo(name = "Id")
    private int Id;

    @NonNull
    @ColumnInfo(name ="UserName")
    private String UserName;

    @NonNull
    @ColumnInfo(name ="Password")
    private String Password;

    @Nullable
    @ColumnInfo(name="FirstName")
    private String FirstName;

    @Nullable
    @ColumnInfo(name="LastName")
    private String LastName;

    public  Account(){

    }
    public Account(@NonNull String userName, @NonNull String passWord){
        this.UserName = userName;
        this.Password = passWord;
        this.LastName="";
        this.FirstName="";

    }
    public Account(@NonNull String userName, @NonNull String passWord, @Nullable  String firstName,@Nullable  String lastName){

        this.UserName = userName;
        this.Password = passWord;
        this.FirstName = firstName;
        this.LastName = lastName;
    }

    public Account(@NonNull int uid, @NonNull String userName, @NonNull String passWord,
                   @Nullable  String firstName,@Nullable  String lastName){
        this.Id= uid;
        this.UserName = userName;
        this.Password = passWord;
        this.FirstName = firstName;
        this.LastName= lastName;
        this.Password = passWord;
    }

    public int getId() {
        return Id;
    }

    public void setId(int id) {
        Id = id;
    }

    @NonNull
    public String getUserName() {
        return UserName;
    }

    public void setUserName(@NonNull String userName) {
        UserName = userName;
    }

    @NonNull
    public String getPassword() {
        return Password;
    }

    public void setPassword(@NonNull String password) {
        Password = password;
    }

    @Nullable
    public String getFirstName() {
        return FirstName;
    }

    public void setFirstName(@Nullable String firstName) {
        FirstName = firstName;
    }

    @Nullable
    public String getLastName() {
        return LastName;
    }

    public void setLastName(@Nullable String lastName) {
        LastName = lastName;
    }

    public Account(Account account)
    {
        this.Id= account.getId();
        this.LastName= account.getLastName();
        this.FirstName= account.getFirstName();
        this.UserName= account.getUserName();
        this.Password= account.getPassword();
    }

}
