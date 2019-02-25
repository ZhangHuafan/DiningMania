package RVRC.GEQ1917.G34.android.diningmania;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;

import static RVRC.GEQ1917.G34.android.diningmania.Login.filename;


public class MainActivity extends AppCompatActivity implements View.OnClickListener{

    private static final String TAG = "MainActivity";
    private NavigationView navigationView;
    private Button b_selectFodd;
    private Button b_scan;
    private Button b_review;

    private TextView tv_usedCredit;
    private TextView tv_remainingCredit;
    private TextView tv_usedPoint;
    private TextView tv_remainingPoint;
    private ListView lv_transactionList;

    private static User user;
    private static FirebaseAuth mAuth;
    private static DatabaseReference mDatabase;
    private static DatabaseReference userRef;
    private static String currUserId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        navigationView = findViewById(R.id.main_navigation);
        View headerView = navigationView.getHeaderView(0);

        (b_selectFodd = findViewById(R.id.main_b_selectFood)).setOnClickListener(this);
        (b_scan = findViewById(R.id.main_b_scan)).setOnClickListener(this);
        (b_review = findViewById(R.id.main_b_review)).setOnClickListener(this);
        tv_usedCredit = headerView.findViewById(R.id.nav_tv_usedCredit);
        tv_remainingCredit = headerView.findViewById(R.id.nav_tv_RemainingCredit);
        tv_usedPoint = headerView.findViewById(R.id.nav_tv_usedPoint);
        tv_remainingPoint = headerView.findViewById(R.id.nav_tv_remainingPoint);
        lv_transactionList = headerView.findViewById(R.id.transaction_lv_container);
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                userMenuSelector(menuItem);
                return false;
            }
        });

        mAuth = FirebaseAuth.getInstance();
        Log.i(TAG,"Got database reference for Users");
        currUserId = mAuth.getCurrentUser().getUid();
        mDatabase= FirebaseDatabase.getInstance().getReference();
        userRef = mDatabase.child("Users");


        userRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(!dataSnapshot.hasChild(currUserId)){
                    user = new User();
                    Log.i(TAG,"Create a new user object");
                    userRef.child(currUserId).setValue(user);
                }
                user = dataSnapshot.child(currUserId).getValue(User.class);
                Log.i(TAG, "Used credit"+user.getUsedCredit());
                updateUI();
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.w(TAG, "Failed to read value.", databaseError.toException());
            }
        });

        FirebaseInstanceId.getInstance().getInstanceId()
                .addOnCompleteListener(new OnCompleteListener<InstanceIdResult>() {
                    @Override
                    public void onComplete(@NonNull Task<InstanceIdResult> task) {
                        if (!task.isSuccessful()) {
                            Log.w(TAG, "getInstanceId failed", task.getException());
                            return;
                        }
                        String token = task.getResult().getToken();

                    }
                });

    }

    private void updateUI(){
        //ArrayAdapter<String> adapter = new ArrayAdapter<>(MainActivity.this,
        //        R.layout.activity_show_transaction,user.getTransactions());
        tv_usedCredit.setText(Integer.toString(user.getUsedCredit()));
        tv_remainingCredit.setText(Integer.toString(user.getRemainingCredit()));
        tv_usedPoint.setText(Integer.toString(user.getUsedPoint()));
        tv_remainingPoint.setText(Integer.toString(user.getRemainingPoint()));
        //lv_transactionList.setAdapter(adapter);
    }



    @Override
    public void onClick(View v){
        Intent intent;
        switch (v.getId()){
            case R.id.main_b_selectFood:
                intent = new Intent(MainActivity.this, SelectFood.class);
                break;
            case R.id.main_b_scan:
                intent = new Intent(MainActivity.this, Scan.class);
                break;
            case R.id.main_b_review:
                intent = new Intent(MainActivity.this, Review.class);
                break;
            default:
                intent = null;
                Log.e(TAG,"Cannot find view id");

        }
        startActivity(intent);
    }

    private void userMenuSelector(MenuItem item){

        Intent intent;

        switch (item.getItemId()){
            case R.id.nav_home:
                intent = new Intent(this,MainActivity.class);
            case R.id.nav_transaction:
                intent = new Intent(MainActivity.this,ShowTransaction.class);
                break;
            case R.id.nav_notification:
                intent = new Intent(MainActivity.this,Notification.class);
                break;
            case R.id.nav_giftRedeem:
                intent = new Intent(MainActivity.this,GiftRedeem.class);
                break;
            case R.id.nav_settings:
                intent =new Intent(MainActivity.this,Settings.class);
                break;
            case R.id.nav_feedback:
                intent =new Intent(MainActivity.this,Feedback.class);
                break;
            case R.id.nav_logout:
                mAuth.signOut();
                SharedPreferences sp = getSharedPreferences(filename, Context.MODE_PRIVATE);
                sp.edit().putBoolean("isLoggedIn",false).apply();
                Log.i(TAG,"Write isLogged in: " + sp.getBoolean("isLoggedIn",false));
                intent = new Intent(MainActivity.this,Login.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                break;
            default:
                intent = null;
                Log.e(TAG,"Cannot find correct view Id");
        }

        startActivity(intent);
    }
}
