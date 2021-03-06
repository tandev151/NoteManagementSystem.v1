package com.example.notemanagement;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

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
    //Init
    Button btnSignIn, btnExit;
    EditText edtUserName, edtPassWord;
    FloatingActionButton fabSignUp;
    Context context;
    AccountDAO accountDAO;
    CheckBox cbremember;
    UserLocalStore userLocalStore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);

        edtUserName = findViewById(R.id.editTextUserNameSignIn);
        edtPassWord = findViewById(R.id.editTextPasswordSignIn);
        btnSignIn = findViewById(R.id.buttonSignIn);
        cbremember = findViewById(R.id.checkBox);

        userLocalStore = new UserLocalStore(this);
        context = this;
        Intent t = this.getIntent();
        Boolean remember = t.getBooleanExtra("Remember", false);
        if (remember) {
            Account account = new Account();
            account = userLocalStore.getLoginUser();
            edtUserName.setText(account.getUserName());
            edtPassWord.setText(account.getPassword());
            cbremember.setChecked(true);
        }

        fabSignUp = findViewById(R.id.floatingActionButtonSignUp);

        fabSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(SignInActivity.this, SignUpActivity.class);
                startActivity(i);
            }
        });
        btnSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String username = edtUserName.getText().toString();
                String password = edtPassWord.getText().toString();

                if (username.isEmpty() || password.isEmpty()) {
                    createDialog("You must enter your email and password", "Notification");
                } else {
                    RoomDB roomDB = RoomDB.getDatabase(getApplicationContext());
                    accountDAO = roomDB.accountDAO();
                    RoomDB.databaseWriteExecutor.execute(() -> {

                        Account checkuser = new Account();
                        checkuser = accountDAO.getUserByMail(edtUserName.getText().toString().trim());

                        if (checkuser == null) {
                            runOnUiThread(new Runnable() {
                                public void run() {
                                    Toast.makeText(getApplicationContext(), "Email was incorrect", Toast.LENGTH_SHORT).show();
                                }
                            });
                        } else if (checkuser.getPassword().equals(edtPassWord.getText().toString())) {
                            userLocalStore.setUserLogined(true);
                            userLocalStore.storeUserData(checkuser);
                            if (cbremember.isChecked()) {
                                userLocalStore.setRememberPass(true);
                            } else
                                userLocalStore.setRememberPass(false);
                            //Announce on screen that success
                            runOnUiThread(new Runnable() {
                                public void run() {
                                    Toast.makeText(getApplicationContext(), "Sign In successfully", Toast.LENGTH_SHORT).show();

                                    Intent mainActivity = new Intent(context, NoteManagementActivity.class);
                                    context.startActivity(mainActivity);
                                }
                            });
                        } else {
                            runOnUiThread(new Runnable() {
                                public void run() {
                                    Toast.makeText(getApplicationContext(), "Password was incorrect", Toast.LENGTH_SHORT).show();
                                }
                            });
                        }
                    });
                }
            }
        });

        btnExit = findViewById(R.id.buttonExit);
        btnExit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
    public void createDialog(String text, String title) {
        AlertDialog alertDialog = new AlertDialog.Builder(this)
                .setPositiveButton("OK", null)
                .setTitle(title)
                .setMessage(text)
                .create();
        alertDialog.show();
    }
}