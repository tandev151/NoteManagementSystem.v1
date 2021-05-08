package com.example.notemanagement;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import com.example.notemanagement.userstore.UserLocalStore;

public class MainActivity extends AppCompatActivity {

    UserLocalStore userLocalStore;
    Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        context = this;
        userLocalStore = new UserLocalStore(this);


        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                if (userLocalStore.checkRememberPass()) {
                    Intent signInActivity = new Intent(context, SignInActivity.class);
                    if (userLocalStore.getLoginUser() != null) {
                        signInActivity.putExtra("Remember", true);

                    }
                    context.startActivity(signInActivity);
                } else {
                    Intent signinActivity = new Intent(context, SignInActivity.class);
                    context.startActivity(signinActivity);
                }
            }
        }, 1500);
    }
}