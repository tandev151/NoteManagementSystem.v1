package com.example.notemanagement;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.room.Room;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import com.example.notemanagement.DAO.AccountDAO;
import com.example.notemanagement.Entity.Account;
import com.example.notemanagement.userstore.UserLocalStore;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class SignInActivity extends AppCompatActivity {

    Button btnSignIn,btnExit;
    EditText edtUserName,edtPassWord;
    FloatingActionButton fabSignUp;
    Context context;
    AccountDAO accountDAO;
    CheckBox cbremember;
    UserLocalStore userLocalStore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);

        userLocalStore= new UserLocalStore(this);
        context= this;
        /*if(userLocalStore.checkRememberPass())
        {
            RoomDB.databaseWriteExecutor.execute(()-> {
                if (userLocalStore.getLoginUser()!=null)
                {
                    runOnUiThread(new Runnable() {
                        public void run() {
                            Intent mainActivity = new Intent(context, NoteManagementActivity.class);
                            context.startActivity(mainActivity);
                        }
                    });
                }
            });
            // return;
        }*/

        fabSignUp = findViewById(R.id.floatingActionButtonSignUp);
        fabSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(SignInActivity.this,SignUpActivity.class);
                startActivity(i);
            }
        });

        edtUserName = findViewById(R.id.editTextUserNameSignIn);
        edtPassWord = findViewById(R.id.editTextPasswordSignIn);
        btnSignIn = findViewById(R.id.buttonSignIn);
        cbremember= findViewById(R.id.checkBox);
        btnSignIn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                String username = edtUserName.getText().toString();
                String password = edtPassWord.getText().toString();

                if(username.isEmpty() || password.isEmpty()){
                    createDialog("Bạn chưa điền tên đăng nhập hay mật khẩu","Thông báo");
                }
                else
                {
                    //      Account account = new Account(username,password);
                    RoomDB roomDB = RoomDB.getDatabase(getApplicationContext());
                    accountDAO = roomDB.accountDAO();
                    RoomDB.databaseWriteExecutor.execute(()->{

                        Account checkuser = new Account();
                        checkuser = accountDAO.getUserByMail(edtUserName.getText().toString().trim());

                        if (checkuser == null) {
                            runOnUiThread(new Runnable() {
                                public void run() {
                                    Toast.makeText(getApplicationContext(), "Email đăng nhập sai", Toast.LENGTH_SHORT).show();
                                }
                            });
                        }
                        else if (checkuser.getPassWord().equals(edtPassWord.getText().toString())) {
                            userLocalStore.setUserLogined(true);
                            userLocalStore.storeUserData(checkuser);

                            if(cbremember.isChecked())
                            {
                                userLocalStore.setRememberPass(true);
                            }
                            else
                                userLocalStore.setRememberPass(false);
                            //Announce on screen that success
                            runOnUiThread(new Runnable() {
                                public void run() {
                                    Toast.makeText(getApplicationContext(), "Đăng nhập thành công", Toast.LENGTH_SHORT).show();

                                    Intent mainActivity = new Intent(context, NoteManagementActivity.class);
                                    context.startActivity(mainActivity);
                                }
                            });
                        }
                        else {
                            runOnUiThread(new Runnable() {
                                public void run() {
                                    Toast.makeText(getApplicationContext(), "Mật khẩu sai", Toast.LENGTH_SHORT).show();
                                }
                            });
                        }
                    });
                    //---------------------------
                }

            }
        });

        //Exist app
        btnExit = findViewById(R.id.buttonExit);
        btnExit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    public void createDialog(String text, String title){
        AlertDialog alertDialog = new AlertDialog.Builder(this)
                .setPositiveButton("OK",null)
                .setTitle(title)
                .setMessage(text)
                .create();
        alertDialog.show();
    }
}