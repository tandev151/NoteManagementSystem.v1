package com.example.notemanagement.userstore;

import android.content.Context;
import android.content.SharedPreferences;

import androidx.room.RoomDatabase;

import com.example.notemanagement.DAO.AccountDAO;
import com.example.notemanagement.Entity.Account;
import com.example.notemanagement.RoomDB;

public class UserLocalStore {

    public static final String SP_NAME="UserID";
    private SharedPreferences userLocalStore;
    private AccountDAO accountDAO;
    public UserLocalStore(Context context){

        //database= RoomDB.getDatabase(context);
        accountDAO= RoomDB.getDatabase(context).accountDAO();
        userLocalStore= context.getSharedPreferences(SP_NAME,0);

    }

    public void storeUserData(Account user){
        SharedPreferences.Editor editor= userLocalStore.edit();
        //User user1= mrepository.getUserByEmail(user.getEmail());
        editor.putInt("idUser",user.getID());
        editor.putString("emailUser", user.getUserName());
        editor.putString("lastnameUser", user.getLastName());
        editor.putString("firstnameUser",user.getFirstName());
        editor.commit();
    }

    public Account getLoginUser()
    {
        String emailUser =  userLocalStore.getString("emailUser","");
        return accountDAO.getUserByMail(emailUser);
    }

    public void setUserLogined(boolean bool){
        SharedPreferences.Editor editor= userLocalStore.edit();
        editor.putBoolean("logined",bool);
        editor.commit();
    }

    public void setRememberPass(boolean rememberme){
        SharedPreferences.Editor editor= userLocalStore.edit();
        editor.putBoolean("rememberpass",rememberme);
        editor.commit();
    }

    public boolean checkRememberPass(){
        if(userLocalStore.getBoolean("rememberpass",false))
            return true;
        return false;
    }

    public boolean checkUserLogin(){
        if(userLocalStore.getBoolean("logined",false))
            return true;
        return false;
    }

    public void clearUser(){
        SharedPreferences.Editor editor= userLocalStore.edit();
        editor.clear();
        editor.commit();
    }
}
