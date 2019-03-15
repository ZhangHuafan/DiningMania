package RVRC.GEQ1917.G34.android.diningmania;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static String TAG = "DatabaseHelper";

    private static final int DATABASE_VERSION = 1;
    static final String DATABASE_NAME = "myDatabase";
    static final String TABLE_RECORDS = "indications";
    static final String TABLE_TRANSACTIONS = "transactions";
    static final String TABLE_POINTS_EARNING= "points_earning";
    static final String TABLE_REVIEW = "review";
    static final String KEY_POINT_ID = "point_id";
    static final String KEY_REVIEW_ID = "review_id";
    static final String KEY_DATE = "date";
    static final String KEY_INDICATION = "indication";
    static final String KEY_CHOSEN_FOOD = "chosen_food";
    static final String KEY_TIME = "time";
    static final String KEY_POINT_CHANGE = "point_change";
    static final String KEY_REMARK = "remark";
    static final String KEY_CONTENT = "content";

    private static final String CREATE_TABLE_RECORDS = "CREATE TABLE "
            + TABLE_RECORDS + "(" + KEY_DATE
            + " TEXT PRIMARY KEY," + KEY_INDICATION + " TEXT );";
    private static final String CREATE_TABLE_TRANSACTIONS = "CREATE TABLE " +
            TABLE_TRANSACTIONS + "(" +
            KEY_DATE + " TEXT PRIMARY KEY," +
            KEY_TIME + " TEXT," +
            KEY_CHOSEN_FOOD + " TEXT );";
    private static final String CREATE_TABLE_POINTS_EARNING = "CREATE TABLE " +
            TABLE_POINTS_EARNING + "(" +
            KEY_POINT_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
            KEY_DATE + " TEXT," +
            KEY_POINT_CHANGE + " INTEGER," +
            KEY_REMARK + " TEXT );";
    private static final String CREATE_TABLE_REVIEW = "CREATE TABLE " +
            TABLE_REVIEW + "(" +
            KEY_REVIEW_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
            KEY_DATE + " TEXT," +
            KEY_CHOSEN_FOOD + "TEXT," +
            KEY_CONTENT + " TEXT );";


    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE_RECORDS);
        db.execSQL(CREATE_TABLE_TRANSACTIONS);
        db.execSQL(CREATE_TABLE_POINTS_EARNING);
        db.execSQL(CREATE_TABLE_REVIEW);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_RECORDS);
        db.execSQL("DROP TABLE IF EXISTS '" + TABLE_TRANSACTIONS + "'");
        db.execSQL("DROP TABLE IF EXISTS '" + TABLE_POINTS_EARNING + "'");
        db.execSQL("DROP TABLE IF EXISTS '" + TABLE_REVIEW + "'");
        onCreate(db);
    }

    public boolean addRecord(String date, String indication) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(KEY_DATE, date);
        contentValues.put(KEY_INDICATION, indication);
        Log.d(TAG, "Added records!");
        long result = db.insertWithOnConflict(TABLE_RECORDS, null, contentValues,
                SQLiteDatabase.CONFLICT_REPLACE);
        return result != -1;
    }

    public boolean addTransaction(String date, String time, String chosenFood) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(KEY_DATE, date);
        contentValues.put(KEY_TIME, time);
        contentValues.put(KEY_CHOSEN_FOOD, chosenFood);

        long result = db.insertWithOnConflict(TABLE_TRANSACTIONS, null, contentValues,
                SQLiteDatabase.CONFLICT_REPLACE);
        return result != -1;
    }

    public boolean addPointEarning(String date, int pointChange, String remark) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(KEY_DATE, date);
        contentValues.put(KEY_POINT_CHANGE, pointChange);
        contentValues.put(KEY_REMARK, remark);

        long result = db.insertWithOnConflict(TABLE_POINTS_EARNING, null, contentValues,
                SQLiteDatabase.CONFLICT_REPLACE);
        return result != -1;
    }

    public boolean addReview(String date, String chosenFood, String content) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(KEY_DATE, date);
        contentValues.put(KEY_CHOSEN_FOOD, chosenFood);
        contentValues.put(KEY_CONTENT, content);

        long result = db.insertWithOnConflict(TABLE_REVIEW, null, contentValues,
                SQLiteDatabase.CONFLICT_REPLACE);
        return result != -1;
    }

    public Cursor getListContents(String tableName) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor data = null;
        switch (tableName) {
            case TABLE_RECORDS:
                data = db.rawQuery("SELECT * FROM " + TABLE_RECORDS, null);
                break;
            case TABLE_TRANSACTIONS:
                data = db.rawQuery("SELECT * FROM " + TABLE_TRANSACTIONS, null);
                break;
            case TABLE_POINTS_EARNING:
                data = db.rawQuery("SELECT * FROM " + TABLE_POINTS_EARNING, null);
                break;
            case TABLE_REVIEW:
                data = db.rawQuery("SELECT * FROM " + TABLE_REVIEW, null);
                break;
        }

        return data;
    }
}
