package rvrc.geq1917.g34.android.diningmania;

import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Collections;

public class Feedback extends AppCompatActivity {

    private DatabaseHelper mySQDatabase = new DatabaseHelper(this);
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feedback);

        ListView lv_reviews = findViewById(R.id.feedback_lv_container);
        ArrayList<String> reviewList = new ArrayList<>();
        Cursor data = mySQDatabase.getListContents(DatabaseHelper.TABLE_REVIEW);
        if(data.getCount() == 0) {
            Toast.makeText(this, "You don't have any reviews :(", Toast.LENGTH_LONG).show();
        } else {
            while (data.moveToNext()) {
                reviewList.add(data.getString(0) + "   " + data.getString(1)
                    + "\n" + data.getString(2));
            }
            Collections.sort(reviewList);
            ListAdapter listAdapter = new ArrayAdapter<>(this,
                    android.R.layout.simple_list_item_1, reviewList);
            lv_reviews.setAdapter(listAdapter);
        }
    }

}