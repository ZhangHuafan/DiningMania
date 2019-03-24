package rvrc.geq1917.g34.android.diningmania;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.google.firebase.FirebaseApp;

import static rvrc.geq1917.g34.android.diningmania.Login.filename;
import static rvrc.geq1917.g34.android.diningmania.Login.isLoggedIn;

public class StartActivity extends AppCompatActivity {

    private static final String TAG = "StartActivity";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);
        FirebaseApp.initializeApp(this);


        Intent intent;
        SharedPreferences sp = getSharedPreferences(filename, Context.MODE_PRIVATE);
        boolean loggedIn = sp.getBoolean(isLoggedIn,false);



        if (loggedIn) {
            intent = new Intent(this, Home.class);
        } else {
            intent = new Intent(this, WelcomePage.class);
        }
        startActivity(intent);
        finish();
    }


}

