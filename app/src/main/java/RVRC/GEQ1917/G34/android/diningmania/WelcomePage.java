package RVRC.GEQ1917.G34.android.diningmania;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class WelcomePage extends AppCompatActivity{

    private static final String message = "Thank you for using Dining Mania. To register, " +
            "enter your NUS email address and Matric number as the password.\n" +
            "After logging in, follow these steps: \n" +
            "1) Click on menu items to pre-indicate at least one day before the meal itself.\n" +
            "2) Scan your the meal using your matric card then scan the QR code at the counter " +
            "which you’re taking from. Remember to continue with paper tickets during this trial!\n" +
            "3) For each meal that you consume that tallies with your pre-indicated choice, " +
            "you will earn one point.\n" +
            "4) You can write reviews to earn one point per feedback.\n" +
            "5) You can only scan the QR code once per day.\n" +
            "6) At the end of this week, you will receive notifications to " +
            "redeem some gifts according to your points!\n" +
            "Let’s start!";

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
