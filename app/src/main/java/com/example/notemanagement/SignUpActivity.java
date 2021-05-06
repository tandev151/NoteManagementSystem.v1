package com.example.notemanagement;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.Context;

import android.content.Intent;
import android.os.Bundle;
import android.os.PatternMatcher;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.notemanagement.RoomDB;
import com.example.notemanagement.DAO.AccountDAO;
import com.example.notemanagement.Entity.Account;
import com.example.notemanagement.userstore.UserLocalStore;

import static android.content.Intent.FLAG_ACTIVITY_NEW_TASK;


public class SignUpActivity extends AppCompatActivity {

    RoomDB db;
    Button btnSignIn;
    EditText edtUserName;
    EditText edtPass;
    EditText edtConfirmPass;
    Button btnSignUp;
    private UserLocalStore userLocalStore;
    private Context context;
    private AccountDAO accountDAO;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        context=getApplicationContext();
        btnSignIn = findViewById(R.id.buttonSignIn);
        edtUserName = findViewById(R.id.editTextUserNameSignUp);
        edtPass = findViewById(R.id.editTextPassword);
        edtConfirmPass = findViewById(R.id.editTextConfirmPassword);
        btnSignUp = findViewById(R.id.buttonSignUp);
        userLocalStore = new UserLocalStore(this);

        btnSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SignUpActivity.this,SignInActivity.class);
                startActivity(intent);
            }

        });

        btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(checkInput() ){

                    RoomDB.databaseWriteExecutor.execute(()->
                    {
                        accountDAO= RoomDB.getDatabase(context).accountDAO();
                        Account checkuser= new Account();
                        checkuser = accountDAO.getUserByMail(edtUserName.getText().toString().trim());
                        if(checkuser==null)
                        {
                            Account user =new Account( edtUserName.getText().toString(), edtPass.getText().toString());
                            accountDAO.insert(user);

                            Account user1= new Account();
                            user1= accountDAO.getUserByMail(edtUserName.getText().toString());

                            userLocalStore.setUserLogined(true);
                            userLocalStore.storeUserData(user1);

                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    createDialog("Successfully create your register!!!","Notification");

                                    Intent mainActivity = new Intent(context, NoteManagementActivity.class);
//                                    context.startActivity(mainActivity);
//                                    mainActivity.setFlags(FLAG_ACTIVITY_NEW_TASK);
                                }
                            });

                        }
                        else
                        {
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    createDialog("Email already exists","Notification");
                                }
                            });

                        }
                    });
                }

            }
        });
    }


    private void createDialog(String text,String title) {
        AlertDialog ad = new AlertDialog.Builder(this)
                .setPositiveButton("OK", null)
                .setTitle(title)
                .setMessage(text)
                .create();
        ad.show();
    }

    private boolean checkInput(){
        String email= edtUserName.getText().toString().trim();
        int usersize = edtUserName.getText().length();
        int passsize = edtPass.getText().length();
        if(usersize < 5 && passsize <5)
        {
            createDialog("Username or password must be longer than 6","Notification");
            return false;
        }
        else if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            createDialog("Email invalid","Notification");
            return false;
        }
        else {
            String username = edtUserName.getText().toString();
            String password = edtPass.getText().toString();
            String confirmpass = edtConfirmPass.getText().toString();
            if (!TextUtils.equals(password, confirmpass)) {
                createDialog("Password is not match", "Notification");
                return false;
            }
        }
        return true;
    }

}