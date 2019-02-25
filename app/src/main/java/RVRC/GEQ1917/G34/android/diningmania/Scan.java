package RVRC.GEQ1917.G34.android.diningmania;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DialogTitle;
import android.util.Log;
import android.widget.Toast;

import org.apache.commons.codec.DecoderException;
import org.apache.commons.codec.net.BCodec;

import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.SetOptions;
import com.google.zxing.Result;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import me.dm7.barcodescanner.zxing.ZXingScannerView;

import static RVRC.GEQ1917.G34.android.diningmania.MainActivity.user;

public class Scan extends AppCompatActivity implements ZXingScannerView.ResultHandler {

    private static final String TAG = "Scan";
    private ZXingScannerView scannerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scan);

        scannerView = new ZXingScannerView(Scan.this);
        setContentView(scannerView);
        scannerView.setResultHandler(Scan.this);
        scannerView.startCamera();
    }

    @Override
    public void handleResult(Result rawResult){
        Log.i(TAG, rawResult.getText());
        Log.i(TAG, rawResult.getBarcodeFormat().toString());
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
            Log.e(TAG,e.getMessage());
            isSuccessful = false;
        }
        if(!isSuccessful){
            scannerView.resumeCameraPreview(this);
        }else {
            updateData(result);
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

    public void updateData(String transactionName){
        Date date = Calendar.getInstance().getTime();
        String type;
        String chosenFood;
        Log.i(TAG, "updateData");
        if(transactionName.contains("Breakfast")) {
    //        user.addTransaction(new BreakfastTransaction(transactionName, date));
            type = "Breakfast";
            chosenFood = transactionName.substring(10);
        }else if (transactionName.contains("Dinner")) {
            user.addTransaction(new DinnerTransaction(transactionName, date));
            type = "Dinner";
            chosenFood = transactionName.substring(7);
        }else{
  //          user.addTransaction(new PointTransaction(transactionName, date));
            type = "Gift";
            chosenFood = transactionName;
        }
        updateRecords(date, chosenFood);
        generateReceipt(date, type, chosenFood);
    }

    private void updateRecords(Date date, String food){
        Log.i(TAG, "updateRecords " + date.toString() + food);
        SimpleDateFormat dfDate = new SimpleDateFormat("dd_MM_yyyy");
        SimpleDateFormat dfTime = new SimpleDateFormat("HH:mm:ss");
        String formattedDate = dfDate.format(date);
        String formattedTime = dfTime.format(date);

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        Map<String, String> record = new HashMap<>();
        record.put(food, formattedTime);
        db.collection("Daily Breakfast Records").document(formattedDate).set(record,
                SetOptions.merge());
    }

    private void generateReceipt(Date date, String type, String food){
        SimpleDateFormat df = new SimpleDateFormat("dd/MM/yy\nHH:mm:ss");
        String dateAndTime = df.format(date);
        SharedPreferences sp = getSharedPreferences("receipt info", MODE_PRIVATE);
        sp.edit().putString("date", dateAndTime).apply();
        sp.edit().putString("type", type).apply();
        sp.edit().putString("food", food).apply();
        Intent goForReceipt = new Intent(Scan.this, Receipt.class);
        startActivity(goForReceipt);
    }
}
