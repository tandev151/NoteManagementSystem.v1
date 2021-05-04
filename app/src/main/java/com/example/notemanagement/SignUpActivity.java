package com.example.notemanagement;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.Context;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
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

//                                    Toast.makeText(getApplicationContext(), "Đăng kí thành công", Toast.LENGTH_SHORT).show();
                                    createDialog("Đăng ký tài khoản thành công","Thông báo");

                                    Intent mainActivity = new Intent(context, NoteManagementActivity.class);
                                    context.startActivity(mainActivity);
                                    mainActivity.setFlags(FLAG_ACTIVITY_NEW_TASK);
                                }
                            });

                        }
                        else
                        {
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
//                                    Toast.makeText(getApplicationContext(), "Email đã tồn tại", Toast.LENGTH_LONG).show();
                                    createDialog("Email đã tồn tại","Thông báo");
                                }
                            });

                        }
                    });
                    //****
                  /*  Account account = new Account();
                    account.setUserName(username);
                    account.setPassWord(password);
                    RoomDB roomDB = RoomDB.getDatabase(getApplicationContext());
                    AccountDAO accountDAO = roomDB.accountDAO();
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            accountDAO.register(username,password);
                        }
                    }).start();


                    createDialog("Đăng ký tài khoản thành công","Thông báo");
                    edtUserName.setText("");
                    edtPass.setText("");
                    edtConfirmPass.setText("");*/
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
        int usersize = edtUserName.getText().length();
        int passsize = edtPass.getText().length();
        if(usersize < 5 && passsize <5)
        {
            createDialog("Độ dài tên đăng nhập hoặc mật khẩu chưa đủ","Thông báo");
            return false;
        }
        else {
            String username = edtUserName.getText().toString();
            String password = edtPass.getText().toString();
            String confirmpass = edtConfirmPass.getText().toString();
            if (!TextUtils.equals(password, confirmpass)) {
                createDialog("Mật khẩu không trùng khớp", "Thông báo");
                return false;
            }
        }
        return true;
    }

}