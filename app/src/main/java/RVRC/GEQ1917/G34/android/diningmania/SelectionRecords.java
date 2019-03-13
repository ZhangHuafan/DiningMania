package RVRC.GEQ1917.G34.android.diningmania;

import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;

public class SelectionRecords extends AppCompatActivity {

    private DatabaseHelper mySQDatabase;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_selection_records);

        ListView lv_records = findViewById(R.id.lv_records);
        mySQDatabase = new DatabaseHelper(this);

        ArrayList<String> recordList = new ArrayList<>();
        Cursor data = mySQDatabase.getListContents(DatabaseHelper.TABLE_RECORDS);

        if(data.getCount() == 0) {
            Toast.makeText(this, "You haven't indicated anything :(", Toast.LENGTH_LONG);
        } else {
            while (data.moveToNext()) {
                recordList.add(data.getString(0) + "\n" + data.getString(1));
                }
            ListAdapter listAdapter = new ArrayAdapter<>(this,
                    android.R.layout.simple_list_item_1, recordList);
            lv_records.setAdapter(listAdapter);
        }
    }



}
