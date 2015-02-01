package ru.tsarcom.slff.slff;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by USRSLM on 18.01.2015.
 */
public class DB_MineCompare {

    private static final String DB_NAME = "SelfyTest4";
    private static final int DB_VERSION = 29;

    private static final String DBT_MC = "my_compare";

    public static final String C_MC_ID = "_id";
    public static final String C_MC_CID = "cid";
    public static final String C_MC_DATE_CRT = "date_crt";
    public static final String C_MC_VOITE_LEFT = "voite_left";
    public static final String C_MC_VOITE_RIGHT = "voite_right";
    public static final String C_MC_PHOTO_LEFT = "img_left";
    public static final String C_MC_PHOTO_RIGHT = "img_right";
    public static final String C_MC_PATH_LEFT = "path_left";
    public static final String C_MC_PATH_RIGHT = "path_right";
    public static final String C_MC_ORNT_LEFT = "imgOrientation_left";
    public static final String C_MC_ORNT_RIGHT = "imgOrientation_right";
    public static final String C_MC_STATUS = "status";

    private static final String DB_MC_CREATE =
            "create table " + DBT_MC + " (" +
                    C_MC_ID + " integer primary key autoincrement,  " +
                    C_MC_CID + " integer,  " +
                    C_MC_DATE_CRT + " text, " +
                    C_MC_PATH_LEFT + " text, " +
                    C_MC_PATH_RIGHT + " text, " +
                    C_MC_VOITE_LEFT + " integer, " +
                    C_MC_VOITE_RIGHT + " integer, " +
                    C_MC_PHOTO_LEFT + " integer, " +
                    C_MC_PHOTO_RIGHT + " integer, " +
                    C_MC_ORNT_LEFT + " integer, " +
                    C_MC_ORNT_RIGHT + " integer, " +
                    C_MC_STATUS + " integer " +
                    "); ";

    private final Context mCtx;

    private DBHelper mDBHelper;
    private SQLiteDatabase mDB;

    public DB_MineCompare(Context ctx) {
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
        return mDB.query(DBT_MC, null,null, null, null, null,  C_MC_DATE_CRT +" DESC");
    }

        // удалить запись из DB_TABLE
    public void delRec(long id) {
        mDB.delete(DBT_MC, C_MC_CID + " = " + id, null);
    }
    // получить max id из таблицы DBT_MC
    public String getLastIdMC() {
        String qr ="SELECT MAX("+C_MC_CID+") as "+C_MC_CID+" FROM "+DBT_MC;
        Cursor cursor = mDB.rawQuery(qr, null);
        cursor.moveToFirst(); // переходим на первую строку
        String maxid = cursor.getString(cursor.getColumnIndex(C_MC_CID));
        if (maxid == null) {
            maxid ="0";
        }
        return maxid;
    }
    // добавить запись в DBT_MC
    public void addRecMC(int CID, String DATE_CRT, int VOITE_LEFT, int VOITE_RIGHT, int PHOTO_LEFT, int PHOTO_RIGHT,
                         int ORNT_LEFT, int ORNT_RIGHT, String PATH_LEFT,  String PATH_RIGHT,  int STATUS ) {

        ContentValues cv = new ContentValues();
        cv.put(C_MC_CID, CID);
        cv.put(C_MC_DATE_CRT, DATE_CRT);
        cv.put(C_MC_VOITE_LEFT, VOITE_LEFT);
        cv.put(C_MC_VOITE_RIGHT, VOITE_RIGHT);
        cv.put(C_MC_PHOTO_LEFT, PHOTO_LEFT);
        cv.put(C_MC_PHOTO_RIGHT, PHOTO_RIGHT);
        cv.put(C_MC_PATH_LEFT, PATH_LEFT);
        cv.put(C_MC_PATH_RIGHT, PATH_RIGHT);
        cv.put(C_MC_ORNT_LEFT, ORNT_LEFT);
        cv.put(C_MC_ORNT_RIGHT, ORNT_RIGHT);
        cv.put(C_MC_STATUS, STATUS);
        mDB.insert(DBT_MC, null, cv);
    }
    // Updating single contact
    public int updateMC(int CID, int VOITE_LEFT, int VOITE_RIGHT, int PHOTO_LEFT, int PHOTO_RIGHT,
                             int ORNT_LEFT, int ORNT_RIGHT, String PATH_LEFT,  String PATH_RIGHT,  int STATUS) {


        ContentValues cv = new ContentValues();
//        cv.put(C_MC_CID, CID);
//        cv.put(C_MC_DATE_CRT, DATE_CRT);
        cv.put(C_MC_VOITE_LEFT, VOITE_LEFT);
        cv.put(C_MC_VOITE_RIGHT, VOITE_RIGHT);
        if(PHOTO_LEFT != -1) {
            cv.put(C_MC_PHOTO_LEFT, PHOTO_LEFT);
        }
        if(PHOTO_LEFT != -1) {
            cv.put(C_MC_PHOTO_RIGHT, PHOTO_RIGHT);
        }
        cv.put(C_MC_PATH_LEFT, PATH_LEFT);
        cv.put(C_MC_PATH_RIGHT, PATH_RIGHT);
        cv.put(C_MC_ORNT_LEFT, ORNT_LEFT);
        cv.put(C_MC_ORNT_RIGHT, ORNT_RIGHT);
        cv.put(C_MC_STATUS, STATUS);

        // updating row
        return mDB.update(DBT_MC, cv, C_MC_CID + " = " + CID,
                null);
    }
    // Updating single contact
    public int updateLeftMC(int CID, int VOITE_LEFT,  int PHOTO_LEFT,
                             int ORNT_LEFT,  String PATH_LEFT,   int STATUS) {


        ContentValues cv = new ContentValues();
        cv.put(C_MC_VOITE_LEFT, VOITE_LEFT);
        cv.put(C_MC_PHOTO_LEFT, PHOTO_LEFT);
        cv.put(C_MC_PATH_LEFT, PATH_LEFT);
        cv.put(C_MC_ORNT_LEFT, ORNT_LEFT);
        cv.put(C_MC_STATUS, STATUS);

        // updating row
        return mDB.update(DBT_MC, cv, C_MC_CID + " = " + CID,
                null);
    }
    // Updating single contact
    public int updateRightMC(int CID, int VOITE_RIGHT,  int PHOTO_RIGHT,
                              int ORNT_RIGHT,  String PATH_RIGHT,  int STATUS) {


        ContentValues cv = new ContentValues();
        cv.put(C_MC_VOITE_RIGHT, VOITE_RIGHT);
        cv.put(C_MC_PHOTO_RIGHT, PHOTO_RIGHT);
        cv.put(C_MC_PATH_RIGHT, PATH_RIGHT);
        cv.put(C_MC_ORNT_RIGHT, ORNT_RIGHT);
        cv.put(C_MC_STATUS, STATUS);

        // updating row
        return mDB.update(DBT_MC, cv, C_MC_CID + " = " + CID,
                null);
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
            db.execSQL(DB_MC_CREATE);

            // заполняем БД
            for (int i = 1; i < 0; i++) {
                ContentValues cv = new ContentValues();
                cv.put(C_MC_CID, i);
                cv.put(C_MC_DATE_CRT,  "2015-01-25 "+i);
                cv.put(C_MC_VOITE_LEFT, 1);
                cv.put(C_MC_VOITE_RIGHT, 1);
                cv.put(C_MC_PHOTO_LEFT, R.drawable.ic_launcher);
                cv.put(C_MC_PHOTO_RIGHT, R.drawable.ic_launcher);
                cv.put(C_MC_PATH_LEFT, "uploads/3/161/153/img.jpg");
                cv.put(C_MC_PATH_RIGHT, "uploads/3/167/156/img.jpg");
                cv.put(C_MC_ORNT_LEFT, 0);
                cv.put(C_MC_ORNT_RIGHT, 0);
                cv.put(C_MC_STATUS, 0);
                db.insert(DBT_MC, null, cv);
            }

        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

            db.execSQL(" DROP TABLE IF EXISTS " + DBT_MC+" ");
            onCreate(db);
        }
    }
}

















