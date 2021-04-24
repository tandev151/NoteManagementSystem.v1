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
    @ColumnInfo(name = "id")
    private int iD;

    public void setID(int iD) {
        this.iD = iD;
    }

    public int getID() {
        return iD;
    }

    @NonNull
    @ColumnInfo(name ="user_name")
    private String userName;

    public void setUserName(@NonNull String userName) {
        this.userName = userName;
    }

    @NonNull
    public String getUserName() {
        return userName;
    }

    @NonNull
    @ColumnInfo(name ="pass_word")
    private String passWord;

    public void setPassWord(@NonNull String passWord) {
        this.passWord = passWord;
    }

    @NonNull
    public String getPassWord() {
        return passWord;
    }

    @Nullable
    @ColumnInfo(name="first_name")
    private String firstName;

    public void setFirstName( String firstName) {
        this.firstName = firstName;
    }


    public String getFirstName() {
        return firstName;
    }

    @Nullable
    @ColumnInfo(name="last_name")
    private String lastName;

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }


    public String getLastName() {
        return lastName;
    }
    public  Account(){

    }
    public Account(@NonNull String userName, @NonNull String passWord){
        this.userName = userName;
        this.passWord = passWord;
        this.lastName="";
        this.firstName="";
    }
    public Account(@NonNull String userName, @NonNull String passWord, @Nullable  String firstName,@Nullable  String lastName){

        this.userName = userName;
        this.passWord = passWord;
        this.firstName = firstName;
        this.passWord = passWord;
    }

    public Account(@NonNull int uid, @NonNull String userName, @NonNull String passWord,
                   @Nullable  String firstName,@Nullable  String lastName){
        this.iD= uid;
        this.userName = userName;
        this.passWord = passWord;
        this.firstName = firstName;
        this.lastName= lastName;
        this.passWord = passWord;
    }

    public Account(Account account)
    {
        this.iD= account.getID();
        this.lastName= account.getLastName();
        this.firstName= account.getFirstName();
        this.userName= account.getUserName();
        this.passWord= account.passWord;
    }

}
