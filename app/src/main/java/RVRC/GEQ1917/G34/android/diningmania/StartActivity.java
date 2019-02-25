package RVRC.GEQ1917.G34.android.diningmania;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.google.firebase.FirebaseApp;

import static RVRC.GEQ1917.G34.android.diningmania.Login.filename;
import static RVRC.GEQ1917.G34.android.diningmania.Login.isLoggedIn;

public class StartActivity extends AppCompatActivity {

    private static final String TAG = "StartActivity";
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FirebaseApp.initializeApp(this);

        Intent intent;
        SharedPreferences sp = getSharedPreferences(filename, Context.MODE_PRIVATE);
        boolean loggedIn = sp.getBoolean(isLoggedIn,false);
        Log.i(TAG,Boolean.toString(loggedIn));
        if (loggedIn) {
            intent = new Intent(this, MainActivity.class);
        } else {
            intent = new Intent(this, Login.class);
        }
        startActivity(intent);
        finish();
    }
}

