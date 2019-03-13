package RVRC.GEQ1917.G34.android.diningmania;

import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Calendar;

import static RVRC.GEQ1917.G34.android.diningmania.Utility.formatDate;

public class MatchingResult extends AppCompatActivity {

    private static String msgForMatching = "Thanks for following up your choice!\n" +
            "One point has been credited to you ;)";
    private static String msgForNotMatching = "Try to follow up your choice next time!\n" +
            "You can earn points by giving reviews ;)";
    private ImageView im_matching_result;
    private TextView tv_msg;
    private boolean matches;
    private DatabaseHelper mySQDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_matching_result);

        tv_msg = findViewById(R.id.matchingResult_tv_msg);
        im_matching_result = findViewById(R.id.matchingResult_im_msg);
        mySQDatabase = new DatabaseHelper(this);

        if(android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.HONEYCOMB) {
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_SECURE, WindowManager.LayoutParams.FLAG_SECURE);
        }

        SharedPreferences sp = getSharedPreferences("receipt info", MODE_PRIVATE);
        String chosenFood = sp.getString("food","Food");

        Cursor data = mySQDatabase.getListContents(DatabaseHelper.TABLE_RECORDS);
        while (data.moveToNext() && !data.getString(0).equals(formatDate
                (Calendar.getInstance().getTime()))) {
            String date = data.getString(0);
        }
        matches = data.getString(1).equals(chosenFood);
        getMessage();
    }

    private void getMessage(){
        if(matches) {
            tv_msg.setText(msgForMatching);
            im_matching_result.setImageResource(R.drawable.matching_result_true);
        } else {
            tv_msg.setText(msgForNotMatching);
            im_matching_result.setImageResource(R.drawable.matching_result_false);
        }
    }
}
