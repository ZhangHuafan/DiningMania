package RVRC.GEQ1917.G34.android.diningmania;


import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import java.util.regex.Pattern;

public class Login extends AppCompatActivity implements View.OnClickListener{

    protected static final String filename = "login Status";
    protected static final String isLoggedIn = "isLoggedIn";
    private static final String TAG = "Login";
    private ProgressBar b_progress;


    protected static boolean isNewUser;
    private String emailId;
    protected static String stuId;

    private EditText et_email;
    private EditText et_password;
    private Button b_loginButton;
    //private Button b_forgetPasswordButton;


    private FirebaseAuth mAuth;
    private SharedPreferences sp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        et_email = findViewById(R.id.login_tv_user_email);
        et_password = findViewById(R.id.login_tv_password);
        b_loginButton = findViewById(R.id.login_b_login);
        //b_forgetPasswordButton = findViewById(R.id.login_b_forgetPassword);
        b_loginButton.setOnClickListener(this);
        //b_forgetPasswordButton.setOnClickListener(this);
        b_progress = findViewById(R.id.login_b_progress);

        mAuth = FirebaseAuth.getInstance();
        sp = getSharedPreferences(filename, Context.MODE_PRIVATE);

    }

    @Override
    public void onClick(View v){
        int viewId = v.getId();
        emailId = et_email.getText().toString();
        if(!isValidEmail(emailId)){
           Toast.makeText(this,"Please enter a correct email form!", Toast.LENGTH_LONG);
        }else {
            if (viewId == R.id.login_b_login) {
                logIn(et_password.getText().toString());
                b_progress.setVisibility(View.VISIBLE);
            } //else if (viewId == R.id.login_b_forgetPassword) {
              //  resetPassword();
              //}
        }
    }

    public static boolean isValidEmail(String email)
    {
        final String EMAIL_REGEX = "^[\\w-\\+]+(\\.[\\w]+)*@[\\w-]" +
                "+(\\.[\\w]+)*(\\.[a-z]{2,})$";
        Pattern pattern = Pattern.compile(EMAIL_REGEX, Pattern.CASE_INSENSITIVE);
        if (email == null)
            return false;

        return pattern.matcher(email).matches();
    }

    private void logIn(final String passwd) {


        mAuth.signInWithEmailAndPassword(emailId, passwd)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            isNewUser = task.getResult().getAdditionalUserInfo().isNewUser();
                            Log.d(TAG, "email logged in: " + isNewUser + " " + emailId);
                            stuId = passwd;
                            sp.edit().putBoolean(isLoggedIn,true).apply();
                            Intent goToMainPage = new Intent(Login.this, Home.class);
                            startActivity(goToMainPage);

                        } else {
                            Log.d(TAG, "email failed to log in: " + emailId);
                            Toast.makeText(Login.this, "Invalid Email or Password",
                                    Toast.LENGTH_LONG).show();
                        }
                    }
                }
                );

    }

    private void resetPassword(){
        mAuth.sendPasswordResetEmail(emailId);
    }

}
