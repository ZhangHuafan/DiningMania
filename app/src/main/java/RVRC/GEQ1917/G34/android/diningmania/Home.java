package RVRC.GEQ1917.G34.android.diningmania;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.view.menu.MenuView;
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
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Map;

import ViewHolder.MenuHolder;

import static RVRC.GEQ1917.G34.android.diningmania.Login.filename;

public class Home extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, View.OnClickListener{

    private static final String TAG = "Home";
    private Button b_selectFodd;
    private Button b_scan;
    private Button b_review;

    private TextView tv_bUsedCredit;
    private TextView tv_bLeftCredit;
    private TextView tv_dUsedCredit;
    private TextView tv_dLeftCredit;
    private TextView tv_usedPoint;
    private TextView tv_leftPoint;
    private ListView lv_transactionList;
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

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        //App-use functions
        View headerView = navigationView.getHeaderView(0);
        (b_selectFodd = findViewById(R.id.home_b_selectFood)).setOnClickListener(this);
        (b_scan = findViewById(R.id.home_b_scan)).setOnClickListener(this);
        (b_review = findViewById(R.id.home_b_review)).setOnClickListener(this);
        tv_bUsedCredit = headerView.findViewById(R.id.nav_tv_bUsedCredit);
        tv_bLeftCredit = headerView.findViewById(R.id.nav_tv_bLeftCredit);
        tv_dUsedCredit = headerView.findViewById(R.id.nav_tv_dUsedCredit);
        tv_dLeftCredit = headerView.findViewById(R.id.nav_tv_dLeftCredit);
        tv_usedPoint = headerView.findViewById(R.id.nav_tv_usedPoint);
        tv_leftPoint = headerView.findViewById(R.id.nav_tv_leftPoint);
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
                }
                user = dataSnapshot.child(currUserId).getValue(User.class);
                Log.i(TAG,"Got user" + user.getBLeftCredit());
                Log.d(TAG, "Got the user from Firebase database");
                updateUI();
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
        loadMenu();
    }

    private void loadMenu() {
        FirebaseRecyclerAdapter<FoodChoice, MenuHolder> adapter = new FirebaseRecyclerAdapter<FoodChoice, MenuHolder>
                (FoodChoice.class, R.layout.menu_items, MenuHolder.class, breakfastChoice) {
                    @Override
                    protected void populateViewHolder(MenuHolder viewHolder, FoodChoice model, int position) {
                        SimpleDateFormat dfDate = new SimpleDateFormat("dd_MM_yyyy");
                        String date = dfDate.format(Calendar.getInstance().getTime());
                        viewHolder.tv_choice.setText(model.getChoice());
                        viewHolder.tv_content.setText(model.getDaily_menu().get(date));
                        Log.d(TAG,"Got url for images "+model.getImage());
                        viewHolder.iv_foodImage.setImageResource(R.drawable.food_choice_western);
                        //Picasso.with(getBaseContext()).load(model.getImage()).into(viewHolder.iv_foodImage);
                        final FoodChoice clickItem = model;
                        viewHolder.setItemClickListener(new ItemClickListener() {
                            @Override
                            public void onClick(View view, int position, boolean truth) {
                                Toast.makeText(Home.this, clickItem.getChoice(), Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
        };
        recyclerMenu.setAdapter(adapter);
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
    public void onClick(View v){
        Intent intent;
        switch (v.getId()){
            case R.id.home_b_selectFood:
                intent = new Intent(this, SelectFood.class);
                break;
            case R.id.home_b_scan:
                intent = new Intent(this, Scan.class);
                break;
            case R.id.home_b_review:
                intent = new Intent(this, Review.class);
                break;
            default:
                intent = null;
                Log.e(TAG,"Cannot find view id");

        }
        startActivity(intent);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        Intent intent;

        switch (item.getItemId()){
            case R.id.nav_home:
                intent = new Intent(this,Home.class);
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
        //ArrayAdapter<String> adapter = new ArrayAdapter<>(Home.this,
        //        R.layout.activity_show_transaction,user.getTransactions());
        tv_bUsedCredit.setText(Integer.toString(user.getBUsedCredit()));
        tv_bLeftCredit.setText(Integer.toString(user.getBLeftCredit()));
        tv_dUsedCredit.setText(Integer.toString(user.getDUsedCredit()));
        tv_dLeftCredit.setText(Integer.toString(user.getDLeftCredit()));
        tv_usedPoint.setText(Integer.toString(user.getUsedPoint()));
        tv_leftPoint.setText(Integer.toString(user.getLeftPoint()));
        //lv_transactionList.setAdapter(adapter);
    }

}
