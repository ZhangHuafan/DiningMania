package rvrc.geq1917.g34.android.diningmania;

import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Calendar;

import static rvrc.geq1917.g34.android.diningmania.Home.user;
import static rvrc.geq1917.g34.android.diningmania.Utility.formatDate;

public class MatchingResult extends AppCompatActivity {

    private static final String MSG_MATCHES = "Thanks for following up your choice! " +
            "One point has been credited to you ;)";
    private static final String MSG_NOT_MATCHES = "Try to follow up your choice next time! " +
            "However, you can also earn points by giving reviews ;)";
    private static final String MSG_NOT_INDICATES = "Try to indicate your choice next time! " +
            "However, you can also earn points by giving reviews ;)";
    protected static final int POINT_AMOUNT = 1;
    private static final int MATCHES = 1;
    private static final int NOT_MATCHES = 2;
    private static final int NOT_INDICATES = 3;
    private ImageView im_matching_result;
    private DatabaseHelper mySQDatabase;
    private boolean foundDate = false;

    private static FirebaseAuth mAuth;
    private static DatabaseReference mDatabase;
    private static DatabaseReference currentUserRef;
    private static String currUserId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_matching_result);

        im_matching_result = findViewById(R.id.matchingResult_im_msg);
        mySQDatabase = new DatabaseHelper(this);

        //if(android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.HONEYCOMB) {
        //    getWindow().setFlags(WindowManager.LayoutParams.FLAG_SECURE, WindowManager.LayoutParams.FLAG_SECURE);
        //}

        SharedPreferences sp = getSharedPreferences("receipt info", MODE_PRIVATE);
        String chosenFood = sp.getString("food","Food");

        Cursor data = mySQDatabase.getListContents(DatabaseHelper.TABLE_RECORDS);
        while (data.moveToNext()) {
            if(data.getString(0).equals(formatDate(Calendar.getInstance().getTime()))) {
                String date = data.getString(0);
                if (data.getString(1).equals(chosenFood)) {
                    getMessage(MATCHES);
                    addPoint();
                } else {
                    getMessage(NOT_MATCHES);
                }
                foundDate = true;
                break;
            }
        }
        if(!foundDate) {
            getMessage(NOT_INDICATES);
        }
    }

    private void getMessage(int result){
        switch (result)
        {
            case MATCHES:
                im_matching_result.setImageResource(R.drawable.matching_result_true);
                Toast.makeText(this, MSG_MATCHES,Toast.LENGTH_LONG).show();
                break;
            case NOT_MATCHES:
                Toast.makeText(this, MSG_NOT_MATCHES,Toast.LENGTH_LONG).show();
                im_matching_result.setImageResource(R.drawable.matching_result_false);
                break;
            case NOT_INDICATES:
                Toast.makeText(this, MSG_NOT_INDICATES,Toast.LENGTH_LONG).show();
                im_matching_result.setImageResource(R.drawable.matching_result_not_indicated);
        }
    }

    public void addPoint(){
        mAuth = FirebaseAuth.getInstance();
        currUserId = mAuth.getCurrentUser().getUid();
        mDatabase= FirebaseDatabase.getInstance().getReference();
        currentUserRef = mDatabase.child("Users").child(currUserId);
        user.earnPoint(POINT_AMOUNT);
        currentUserRef.setValue(user);
    }
}
