package com.example.notemanagement.userstore;

import android.content.Context;
import android.content.SharedPreferences;

import com.example.notemanagement.DAO.AccountDAO;
import com.example.notemanagement.Entity.Account;
import com.example.notemanagement.RoomDB;

public class UserLocalStore {

    public static final String SP_NAME="UserID";
    private SharedPreferences userLocalStore;
    private AccountDAO accountDAO;
    public UserLocalStore(Context context){

        accountDAO= RoomDB.getDatabase(context).accountDAO();
        userLocalStore= context.getSharedPreferences(SP_NAME,0);

    }

    public void storeUserData(Account user){
        SharedPreferences.Editor editor= userLocalStore.edit();
        editor.putInt("idUser",user.getId());
        editor.putString("emailUser", user.getUserName());
        editor.putString("lastnameUser", user.getLastName());
        editor.putString("firstnameUser",user.getFirstName());
        editor.putString("passwordUser", user.getPassword());
        editor.commit();
    }

    public Account getLoginUser()
    {
        int idUser= userLocalStore.getInt("idUser",0);
        String emailUser =  userLocalStore.getString("emailUser","");
        String  lastname= userLocalStore.getString("lastnameUser","");
        String firstname= userLocalStore.getString("firstnameUser","");
        String pass = userLocalStore.getString("passwordUser","");
        return new Account(idUser,emailUser,pass,firstname,lastname);
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
