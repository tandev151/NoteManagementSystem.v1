package com.example.notemanagement;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.notemanagement.DAO.AccountDAO;
import com.example.notemanagement.Entity.Account;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class SignInActivity extends AppCompatActivity {

    Button btnSignIn,btnExit;
    EditText edtUserName,edtPassWord;
    FloatingActionButton fabSignUp;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);
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
                    AccountDAO accountDAO = roomDB.accountDAO();
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            Account account = accountDAO.login(username,password);
                            if(account == null){
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        createDialog("Tên đăng nhập hoặc mật khẩu không chính xác","Thông báo");
                                    }
                                });
                            } else{

                                Intent intent = new Intent(SignInActivity.this, NoteManagementActivity.class)
                                        .putExtra("Username",username);
                                startActivity(intent);
                            }
                        }
                    }).start();


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

    public void createDialog(String text, String title){
        AlertDialog alertDialog = new AlertDialog.Builder(this)
                .setPositiveButton("OK",null)
                .setTitle(title)
                .setMessage(text)
                .create();
        alertDialog.show();
    }
}