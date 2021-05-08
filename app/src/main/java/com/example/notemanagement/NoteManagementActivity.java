package com.example.notemanagement;

import android.content.Intent;
import android.os.Bundle;

import android.view.MenuItem;
import android.view.View;
import android.view.Menu;
import android.widget.TextView;

import com.example.notemanagement.Entity.Account;
import com.example.notemanagement.userstore.UserLocalStore;

import com.google.android.material.navigation.NavigationView;

import androidx.annotation.NonNull;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

public class NoteManagementActivity extends AppCompatActivity {

    private AppBarConfiguration mAppBarConfiguration;
    private UserLocalStore userLocalStore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note_management);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_home, R.id.nav_category,
                R.id.nav_priority, R.id.nav_status,
                R.id.nav_note,
                R.id.nav_edit_profile, R.id.nav_change_password)
                .setDrawerLayout(drawer)
                .build();
        //Set up navigation
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);
        View hView = navigationView.getHeaderView(0);
        TextView tvUserName = hView.findViewById(R.id.textViewUserName);
        userLocalStore = new UserLocalStore(this);
        if (userLocalStore.checkRememberPass()) {
            RoomDB.databaseWriteExecutor.execute(() -> {
                Account u = new Account(userLocalStore.getLoginUser());
                if (u != null) {
                    runOnUiThread(new Runnable() {
                        public void run() {
                            tvUserName.setText(u.getUserName());
                        }
                    });
                }
            });
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.note_management, menu);
        return true;
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        switch (item.getItemId()) {
            case R.id.action_logout:
                if (userLocalStore.checkUserLogin()) {
                    RoomDB.databaseWriteExecutor.execute(() -> {
                        userLocalStore.clearUser();
                        userLocalStore.setRememberPass(false);
                        userLocalStore.setUserLogined(false);

                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Intent intent = new Intent(NoteManagementActivity.this, SignInActivity.class);
                                startActivity(intent);
                            }
                        });
                    });
                }
                break;
        }

        return super.onOptionsItemSelected(item);
    }
}