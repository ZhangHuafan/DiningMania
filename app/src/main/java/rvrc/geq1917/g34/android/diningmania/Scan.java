package rvrc.geq1917.g34.android.diningmania;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import org.apache.commons.codec.DecoderException;
import org.apache.commons.codec.net.BCodec;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.SetOptions;
import com.google.zxing.Result;

import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import me.dm7.barcodescanner.zxing.ZXingScannerView;

import static rvrc.geq1917.g34.android.diningmania.Home.user;
import static rvrc.geq1917.g34.android.diningmania.Utility.formatDate;
import static rvrc.geq1917.g34.android.diningmania.Utility.formatTime;

public class Scan extends AppCompatActivity implements ZXingScannerView.ResultHandler {

    private static final String TAG = "Scan";
    private ZXingScannerView scannerView;
    private static FirebaseAuth mAuth;
    private static DatabaseReference mDatabase;
    private static DatabaseReference currentUserRef;
    private static String currUserId;
    private DatabaseHelper mySQDatabase;
    private int CAMERA_PERMISSION_CODE = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scan);

        if(ContextCompat.checkSelfPermission(Scan.this, Manifest.permission.CAMERA)
                != PackageManager.PERMISSION_GRANTED) {
            requestCameraPermission();
        }

        scannerView = new ZXingScannerView(Scan.this);
        setContentView(scannerView);
        scannerView.setResultHandler(Scan.this);
        scannerView.startCamera();
    }

    private void requestCameraPermission() {
        if (ActivityCompat.shouldShowRequestPermissionRationale(Scan.this, Manifest.permission.CAMERA)) {
            new AlertDialog.Builder(this)
                    .setTitle("Permission Needed")
                    .setMessage("This permission is needed for scanning meals.")
                    .setPositiveButton("Okay", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            ActivityCompat.requestPermissions(Scan.this, new String[]
                                    {Manifest.permission.CAMERA}, CAMERA_PERMISSION_CODE);
                        }
                    })
                    .setNegativeButton("Deny", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    })
                    .create().show();
        } else {
            ActivityCompat.requestPermissions(this, new String[] {Manifest.permission.CAMERA}
                    , CAMERA_PERMISSION_CODE);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == CAMERA_PERMISSION_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, "Permission Granted", Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(this, "Permission Denied", Toast.LENGTH_LONG).show();
                startActivity(new Intent(Scan.this, Home.class));
            }
        }
    }

    @Override
    public void handleResult(Result rawResult){
        BCodec bCodec = new BCodec();
        String result = "";
        boolean isSuccessful;


        try {
            result = bCodec.decode(rawResult.getText().trim());
            isSuccessful = true;
        } catch (DecoderException e){
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
                    builder.setTitle("Scanning fails");
                    builder.setMessage("This QR code is not registered under Dining Mania");
                    AlertDialog alert2 = builder.create();
                    alert2.show();
            isSuccessful = false;
        }
        if(!isSuccessful){
            scannerView.resumeCameraPreview(this);
        }else {
            Date date = Calendar.getInstance().getTime();
            processResult(date, result);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        scannerView.stopCamera();
    }

    @Override
    protected void onResume() {
        super.onResume();
        scannerView.setResultHandler(Scan.this);
        scannerView.startCamera();
    }

    private void makeToast(String msg) {
        Toast.makeText(Scan.this, msg, Toast.LENGTH_LONG).show();
    }

    public void processResult(Date date, String transactionName){
        mAuth = FirebaseAuth.getInstance();
        currUserId = mAuth.getCurrentUser().getUid();
        mDatabase= FirebaseDatabase.getInstance().getReference();
        currentUserRef = mDatabase.child("Users").child(currUserId);
        String type;
        String chosenFood;
        if(transactionName.contains("Breakfast")) {
            user.addTransaction(new BreakfastTransaction(transactionName, date));
            type = "Breakfast";
            chosenFood = transactionName.substring(10);
        }else if (transactionName.contains("Dinner")) {
            user.addTransaction(new DinnerTransaction(transactionName, date));
            type = "Dinner";
            chosenFood = transactionName.substring(7);
        }else{
            user.addTransaction(new PointTransaction(transactionName, date));
            type = "Gift";
            chosenFood = transactionName;
        }
        currentUserRef.setValue(user);
        addLocalDate(date, chosenFood);
        updateCloudRecords(type, chosenFood, date);
        showMatchingResult(type, chosenFood, date);
    }

    private void addLocalDate(Date date, String choice) {
        mySQDatabase = new DatabaseHelper(this);
        boolean insertDate = mySQDatabase.addTransaction(formatDate(date), formatTime(date), choice);
        if(insertDate) {
        } else {
            makeToast("Something went wrong. Please try again.");
        }
    }

    private void updateCloudRecords(String type, String food, Date date){

        String formattedDate = formatDate(date);
        String formattedTime = formatTime(date);

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        Map<String, String> record = new HashMap<>();
        record.put(food, formattedTime);
        db.collection(String.format("%s Records", type)).document(formattedDate).set(record,
                SetOptions.merge());
    }

    private void showMatchingResult(String type, String food, Date date){
        SharedPreferences sp = getSharedPreferences("receipt info", MODE_PRIVATE);
        sp.edit().putString("food", food).apply();
        sp.edit().putString("date", formatDate(date)).apply();
        Intent goForMatchingResult = new Intent(Scan.this, MatchingResult.class);
        startActivity(goForMatchingResult);
    }
}
