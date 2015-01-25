package ru.tsarcom.slff.slff;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by USRSLM on 18.01.2015.
 */
public class DB {

    private static final String DB_NAME = "SelfyTest2";
    private static final int DB_VERSION = 4;

    private static final String DBT_MC = "my_compare";
    public static final String C_MC_ID = "_id";
    public static final String C_MC_CID = "cid";
    public static final String C_MC_DATE_CRT = "date_crt";
    public static final String C_MC_VOITE_LEFT = "voite_left";
    public static final String C_MC_VOITE_RIGHT = "voite_right";
    public static final String C_MC_PHOTO_LEFT = "imgOrientation_left";
    public static final String C_MC_PHOTO_RIGHT = "imgOrientation_right";
    public static final String C_MC_ORNT_LEFT = "imgOrientation_left";
    public static final String C_MC_ORNT_RIGHT = "imgOrientation_right";
    public static final String C_MC_STATUS = "status";


    private static final String DB_MC_CREATE =
            "create table " + DBT_MC + " (" +
                    C_MC_ID + " integer primary key autoincrement,  " +
                    C_MC_CID + " integer,  " +
                    C_MC_DATE_CRT + " text, " +
                    C_MC_VOITE_LEFT + " integer, " +
                    C_MC_VOITE_RIGHT + " integer, " +
                    C_MC_PHOTO_LEFT + " integer, " +
                    C_MC_PHOTO_RIGHT + " integer, " +
                    C_MC_ORNT_LEFT + " integer, " +
                    C_MC_ORNT_RIGHT + " integer, " +
                    C_MC_STATUS + " integer " +
                    "); ";


    public static final String COLUMN_ID = "_id";
    public static final String COLUMN_IMG = "img";
    public static final String COLUMN_TXT = "txt";
    public static final String COLUMN_TXT2 = "txt2";
    public static final String COLUMN_I = "i";

    private static final String DB_TABLE = "mytab";
    private static final String DB_CREATE =
            "create table " + DB_TABLE + "(" +
                    COLUMN_ID + " integer primary key autoincrement, " +
                    COLUMN_IMG + " integer, " +
                    COLUMN_TXT + " text," +
                    COLUMN_TXT2 + " text," +
                    COLUMN_I + " text" +
                    "); ";


    private final Context mCtx;

    private DBHelper mDBHelper;
    private SQLiteDatabase mDB;

    public DB(Context ctx) {
        mCtx = ctx;
    }

    // открыть подключение
    public void open() {
        mDBHelper = new DBHelper(mCtx, DB_NAME, null, DB_VERSION);
        mDB = mDBHelper.getWritableDatabase();
    }

    // закрыть подключение
    public void close() {
        if (mDBHelper!=null) mDBHelper.close();
    }

    // получить все данные из таблицы DBT_MC
    public Cursor getAllDataMC() {
        return mDB.query(DBT_MC, null,C_MC_ID+ " > "+this.getLastIdMC(), null, null, null, null);
    }
    // получить max id из таблицы DBT_MC
    public String getLastIdMC() {
        Cursor cursor = mDB.rawQuery("SELECT MAX("+C_MC_ID+") as "+C_MC_ID+" FROM "+DBT_MC, null);
        return cursor.getString(cursor.getColumnIndex(C_MC_ID));
    }

    // получить все данные из таблицы DB_TABLE
    public Cursor getAllData() {
        return mDB.query(DB_TABLE, null, null, null, null, null, null);
    }

    // добавить запись в DBT_MC
    public void addRecMC(int CID, String DATE_CRT, int VOITE_LEFT, int VOITE_RIGHT, int PHOTO_LEFT, int PHOTO_RIGHT,
                         int ORNT_LEFT, int ORNT_RIGHT, int STATUS ) {

        ContentValues cv = new ContentValues();
        cv.put(C_MC_ID, CID);
        cv.put(C_MC_DATE_CRT, DATE_CRT);
        cv.put(C_MC_VOITE_LEFT, VOITE_LEFT);
        cv.put(C_MC_VOITE_RIGHT, VOITE_RIGHT);
        cv.put(C_MC_VOITE_RIGHT, PHOTO_LEFT);
        cv.put(C_MC_VOITE_RIGHT, PHOTO_RIGHT);
        cv.put(C_MC_VOITE_RIGHT, ORNT_LEFT);
        cv.put(C_MC_VOITE_RIGHT, ORNT_RIGHT);
        cv.put(C_MC_STATUS, STATUS);
        mDB.insert(DBT_MC, null, cv);
    }



    // класс по созданию и управлению БД
    private class DBHelper extends SQLiteOpenHelper {

        public DBHelper(Context context, String name, SQLiteDatabase.CursorFactory factory,
                        int version) {
            super(context, name, factory, version);
        }

        // создаем и заполняем БД
        @Override
        public void onCreate(SQLiteDatabase db) {
            // создаем БД
            db.execSQL(DB_CREATE);
            db.execSQL(DB_MC_CREATE);

//            db.execSQL(DB_MA_CREATE);
//            db.execSQL(DB_OC_CREATE);
//            db.execSQL(DB_PHOTO_CREATE);

            // заполняем БД
            ContentValues cv = new ContentValues();
            for (int i = 1; i < 10; i++) {
                addRecMC(i,"2015-01-25 "+i,1,1,R.drawable.ic_launcher,R.drawable.ic_launcher,0,0,0);
                cv.put(COLUMN_I,  "# "+ i + " - ");
                cv.put(COLUMN_TXT,   " - тест DB ver. ");
                cv.put(COLUMN_TXT2,   " " + DB_VERSION);
                cv.put(COLUMN_IMG, R.drawable.ic_launcher);
                db.insert(DB_TABLE, null, cv);
            }

        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

            db.execSQL(" DROP TABLE IF EXISTS " + DB_TABLE+"; ");
            db.execSQL(" DROP TABLE IF EXISTS " + DBT_MC+"; ");

//            db.execSQL("DROP TABLE IF EXISTS " + DBT_MA);
//            db.execSQL("DROP TABLE IF EXISTS " + DBT_OC);
//            db.execSQL("DROP TABLE IF EXISTS " + DBT_PHOTO);
            onCreate(db);
        }
    }
}

















