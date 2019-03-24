package rvrc.geq1917.g34.android.diningmania;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class WelcomePage extends AppCompatActivity{

    private static final String message = "Thank you for using Dining Mania.\n" +
            "To begin, follow these simple steps!\n" +
            "\n" +
            "1) Click SIGN UP. \n" +
            "▪ USERNAME: eXXXXXXX@u.nus.edu \n" +
            "▪ PASSWORD: Matriculation Number\n" +
            "\n" +
            "2) Click LOGIN. Case sensitive.\n" +
            "\n" +
            "3) You are good to go! Note:\n" +
            "▪ Click CALENDAR \uD83D\uDCC5 to choose the date you want to pre-select your meal\n" +
            "▪ Click SCAN \uD83D\uDCF7 to scan the QR code at the respective meal counters. Remember to allow the app to access your camera!\n" +
            "▪ Click FEEDBACK \uD83D\uDCDD to write reviews and earn one incentive point\n" +
            "\n" +
            "Happy dining! :)\n" +
            "\n" +
            "(⛰ a rvrc geq project)";

    private Button b_login;
    private Button b_sign_up;
    private TextView tv_msg;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome_page);

        b_login = findViewById(R.id.welcomePage_b_login);
        b_sign_up = findViewById(R.id.welcomePage_b_sign_up);
        tv_msg = findViewById(R.id.welcomePage_tv_msg);

        tv_msg.setText(message);

        b_login.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(WelcomePage.this, Login.class);
                startActivity(intent);
            }
        });

        b_sign_up.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(WelcomePage.this, SignUp.class);
                startActivity(intent);
            }
        });


    }
}
