package RVRC.GEQ1917.G34.android.diningmania;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

public class Receipt extends AppCompatActivity {

    private TextView tv_type;
    private TextView tv_food;
    private TextView tv_dateAndTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_receipt);
        tv_type = findViewById(R.id.receipt_tv_type);
        tv_food = findViewById(R.id.receipt_tv_food);
        tv_dateAndTime = findViewById(R.id.receipt_tv_dateAndTime);

        getReceipt();
    }

    private void getReceipt(){
        SharedPreferences sp = getSharedPreferences("receipt info", MODE_PRIVATE);
        tv_type.setText(sp.getString("type", "Meal"));
        tv_food.setText(sp.getString("food","Food"));
        tv_dateAndTime.setText(sp.getString("date", "00 00 0000\n00:00:00"));
    }
}
