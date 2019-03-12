package RVRC.GEQ1917.G34.android.diningmania;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Map;

import ViewHolder.MenuHolder;

import static RVRC.GEQ1917.G34.android.diningmania.Login.filename;

public class Home extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener,
        DatePickerDialog.OnDateSetListener {

    private static final String TAG = "Home";

    private TextView tv_dUsedCredit;
    private TextView tv_dLeftCredit;
    private TextView tv_usedPoint;
    private TextView tv_leftPoint;
    private Button b_calendar;
    private RecyclerView recyclerMenu;
    private RecyclerView.LayoutManager layoutManager;

    protected static User user;
    private static FirebaseAuth mAuth;
    private static DatabaseReference mDatabase;
    private static DatabaseReference userRef;
    private static DatabaseReference breakfastChoice;
    private static String currUserId;
    private static String studentId;
    SharedPreferences sp;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("Menu");
        setSupportActionBar(toolbar);

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        //App-use functions
        View headerView = navigationView.getHeaderView(0);
        tv_dUsedCredit = headerView.findViewById(R.id.nav_tv_dUsedCredit);
        tv_dLeftCredit = headerView.findViewById(R.id.nav_tv_dLeftCredit);
        tv_usedPoint = headerView.findViewById(R.id.nav_tv_usedPoint);
        tv_leftPoint = headerView.findViewById(R.id.nav_tv_leftPoint);
        b_calendar = findViewById(R.id.home_b_calender);
        b_calendar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogFragment calendar = new CalendarFragment();
                calendar.show(getSupportFragmentManager(), "date pick");
            }
        } );
        //lv_transactionList = headerView.findViewById(R.id.transaction_lv_container);


        mAuth = FirebaseAuth.getInstance();
        Log.i(TAG,"Got database reference for Users");
        currUserId = mAuth.getCurrentUser().getUid();
        mDatabase= FirebaseDatabase.getInstance().getReference();
        userRef = mDatabase.child("Users");
        sp = getSharedPreferences(filename, Context.MODE_PRIVATE);
        breakfastChoice = mDatabase.child("Breakfast Choice");
        Log.d(TAG,"Got breakfast reference" + breakfastChoice.toString());

        userRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(!dataSnapshot.hasChild(currUserId)){
                    studentId = sp.getString("studentId","A111");
                    user = new User(studentId);
                    Log.i(TAG,"Create a new user object");
                    userRef.child(currUserId).setValue(user);
                } else {
                    user = dataSnapshot.child(currUserId).getValue(User.class);
                    Log.d(TAG, "Got the user from Firebase database" + user);
                    updateUI();
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.w(TAG, "Failed to read value.", databaseError.toException());
            }
        });

        //load menu
        recyclerMenu = findViewById(R.id.recycler_menu);
        recyclerMenu.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        recyclerMenu.setLayoutManager(layoutManager);
        loadMenu("27_02_2019");
    }


    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.YEAR, year);
        calendar.set(Calendar.MONTH,month);
        calendar.set(Calendar.DAY_OF_MONTH,dayOfMonth);
        SimpleDateFormat dfDate = new SimpleDateFormat("dd_MM_yyyy");
        String date = dfDate.format(calendar.getTime());
        loadMenu(date);
    }

    private void loadMenu(final String date) {
        FirebaseRecyclerAdapter<FoodChoice, MenuHolder> adapter = new FirebaseRecyclerAdapter<FoodChoice, MenuHolder>
                (FoodChoice.class, R.layout.menu_items, MenuHolder.class, breakfastChoice) {
            @Override
            protected void populateViewHolder(MenuHolder viewHolder, FoodChoice model, int position) {
                viewHolder.tv_choice.setText(model.getChoice());
                viewHolder.tv_content.setText(model.getDaily_menu().get(date));
                Log.d(TAG, "Got url for images " + model.getImage());
                viewHolder.iv_foodImage.setImageResource(R.drawable.food_choice_western);
                //Picasso.with(getBaseContext()).load(model.getImage()).into(viewHolder.iv_foodImage);
                final FoodChoice clickItem = model;
                viewHolder.setItemClickListener(new ItemClickListener() {
                    @Override
                    public void onClick(View view, int position, boolean isLongClick) {
                        Map<String, String> brIndications = user.getBreakfastIndications();
                        if ( !brIndications.isEmpty() && brIndications.containsKey(date)) {
                            Toast.makeText(Home.this, "You have already chosen the meal ;)",
                                    Toast.LENGTH_LONG).show();
                        } else {
                            showConfirmationDialog(date, clickItem.getChoice());
                        }
                    }
                });
            }
        };
        recyclerMenu.setAdapter(adapter);
    }

    public void showConfirmationDialog(final String date, final String choice) {
        AlertDialog.Builder builder = new AlertDialog.Builder(Home.this);
        builder.setCancelable(true);
        builder.setTitle("Are you sure to eat " + choice + " next ?");
        builder.setMessage("\nLet's make wise choice =)");
        builder.setPositiveButton("Confirm",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        user.addBreakfastIndication(date, choice);
                    }
                });
        builder.setNegativeButton(android.R.string.cancel,
                new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
            }
        });

        AlertDialog dialog = builder.create();
        dialog.show();
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.home, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        Intent intent;

        switch (item.getItemId()){
            case R.id.nav_scan:
                intent = new Intent(this,Scan.class);
            case R.id.nav_transaction:
                intent = new Intent(this,ShowTransaction.class);
                break;
            case R.id.nav_notification:
                intent = new Intent(this,Notification.class);
                break;
            case R.id.nav_giftRedeem:
                intent = new Intent(this,GiftRedeem.class);
                break;
            case R.id.nav_settings:
                intent =new Intent(this,Settings.class);
                break;
            case R.id.nav_feedback:
                intent =new Intent(this,Feedback.class);
                break;
            case R.id.nav_logout:
                mAuth.signOut();
                sp.edit().putBoolean("isLoggedIn",false).apply();
                Log.i(TAG,"Write isLogged in: " + sp.getBoolean("isLoggedIn",false));
                intent = new Intent(this,Login.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                break;
            default:
                intent = null;
                Log.e(TAG,"Cannot find correct view Id");
        }

        startActivity(intent);
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void updateUI(){
        tv_dUsedCredit.setText(Integer.toString(user.getDUsedCredit()));
        tv_dLeftCredit.setText(Integer.toString(user.getDLeftCredit()));
        tv_usedPoint.setText(Integer.toString(user.getUsedPoint()));
        tv_leftPoint.setText(Integer.toString(user.getLeftPoint()));
    }

}
