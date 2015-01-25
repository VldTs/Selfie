package ru.tsarcom.slff.slff;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
/**
 * Created by USRSLM on 13.12.2014.
 */
//public class DBSelfie  extends SQLiteOpenHelper {
public class DBSelfie {

        private static final String DB_NAME = "Selfy2";
        private static final int DB_VERSION = 6;

        private static final String DBT_MA = "my_account";
        public static final String C_MA_ID = "_id";
        public static final String C_MA_ID_USER = "id_user";
        public static final String C_MA_NICKNAME = "id_user";

        private static final String DB_MA_CREATE =
            "create table " + DBT_MA + "(" +
                    C_MA_ID + " integer primary key, " +
                    C_MA_ID_USER + " integer, " +
                    C_MA_NICKNAME + " text" +
                    ");";

        private static final String DBT_MC = "my_compare";
        public static final String C_MC_ID = "_id";
        public static final String C_MC_DATE_CRT = "date_crt";
        public static final String C_MC_VOITE_LEFT = "voite_left";
        public static final String C_MC_VOITE_RIGHT = "voite_right";
        public static final String C_MC_STATUS = "status";

        private static final String DB_MC_CREATE =
            "create table " + DBT_MC + "(" +
                    C_MC_ID + " integer primary key, " +
                    C_MC_VOITE_LEFT + " integer, " +
                    C_MC_VOITE_RIGHT + " integer, " +
                    C_MC_DATE_CRT + " text," +
                    C_MC_STATUS + " integer " +
                    ");";

        private static final String DBT_OC = "oth_compare";
        public static final String C_OC_ID = "_id";
        public static final String C_OC_ID_ACCOUNT = "id_account";
        public static final String C_OC_DATE_CRT = "date_crt";
        public static final String C_OC_VOITE_LEFT = "voite_left";
        public static final String C_OC_VOITE_RIGHT = "voite_right";
        public static final String C_OC_STATUS = "status";

        private static final String DB_OC_CREATE =
                "create table " + DBT_OC + "(" +
                        C_OC_ID + " integer primary key, " +
                        C_OC_VOITE_LEFT + " integer, " +
                        C_OC_VOITE_RIGHT + " integer, " +
                        C_OC_ID_ACCOUNT + " integer," +
                        C_OC_DATE_CRT + " text," +
                        C_OC_STATUS + " integer " +
                        ");";

        private static final String DBT_PHOTO = "photo";
        public static final String C_PHOTO_ID = "_id";
        public static final String C_PHOTO_ID_COMPARE = "id_compare";
        public static final String C_PHOTO_ORIENTATION = "Orientation";
        public static final String C_PHOTO_LOR = "LeftOrRight";

        private static final String DB_PHOTO_CREATE =
            "create table " + DBT_PHOTO + "(" +
                    C_PHOTO_ID + " integer primary key, " +
                    C_PHOTO_ID_COMPARE + " integer," +
                    C_PHOTO_ORIENTATION + " integer," +
                    C_PHOTO_LOR + " integer " +
                    ");";

        public static final String COLUMN_ID = "_id";
        public static final String COLUMN_IMG = "img";
        public static final String COLUMN_TXT = "txt";

    private static final String DB_TABLE = "mytab";
    private static final String DB_CREATE =
            "create table " + DB_TABLE + "(" +
                    COLUMN_ID + " integer primary key autoincrement, " +
                    COLUMN_IMG + " integer, " +
                    COLUMN_TXT + " text" +
                    ");";


    public static final String C_COMPARE_ID = "Cid";
    public static final String C_NAME_DATE_CRT = "Cdate";
    public static final String C_NAME_LEFT = "url_left";
    public static final String C_NAME_RIGHT = "url_right";
    public static final String C_VOITE_LEFT = "all_voiteLeft";
    public static final String C_VOITE_RIGHT = "all_voiteRight";
    public static final String C_ORNT_LEFT = "imgOrientation_left";
    public static final String C_ORNT_RIGHT = "imgOrientation_right";

    private static final String DBT_TEST = "test";
    private static final String DBT_CREATE =
            "create table " + DBT_TEST + "(" +
                    C_COMPARE_ID + " integer primary key autoincrement, " +
                    C_NAME_DATE_CRT + " text," +
                    C_NAME_LEFT + " text," +
                    C_NAME_RIGHT + " text," +
                    C_VOITE_LEFT + " integer, " +
                    C_VOITE_RIGHT + " integer, " +
                    C_ORNT_LEFT + " integer, " +
                    C_ORNT_RIGHT + " integer " +
                    ");";


    private final Context mCtx;


    private DBHelper mDBHelper;
    private SQLiteDatabase mDB;

    public DBSelfie(Context ctx) {
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
//    public Cursor getAllDataMC() {
////        return mDB.query(DBT_MC, null,C_MC_ID+ " > "+this.getLastIdMC(), null, null, null, null);
//        return mDB.query(DBT_MC, null,null, null, null, null, null);
//    }
//    // получить max id из таблицы DBT_MC
//    public String getLastIdMC() {
//        Cursor cursor = mDB.rawQuery("SELECT MAX("+C_MC_ID+") as "+C_MC_ID+" FROM "+DBT_MC, null);
//        return cursor.getString(cursor.getColumnIndex(C_MC_ID));
//    }
//    // получить все данные из таблицы DBT_OC
//    public Cursor getAllDataOC() {
//        return mDB.query(DBT_OC, null, null, null, null, null, null);
//    }
////    // получить  данные из таблицы DBT_PHOTO LEFT
////    public Cursor getAllDataPhotoLeft(String id_compare) {
////        return mDB.query(DBT_PHOTO, null, "id_compare = "+id_compare+" and LeftOrRight = 1", null, null, null, null);
////    }
////    // получить  данные из таблицы DBT_PHOTO RIGHT
////    public Cursor getAllDataPhotoRight(String id_compare) {
////        return mDB.query(DBT_PHOTO, null, "id_compare = "+id_compare+" and LeftOrRight = 2", null, null, null, null);
////    }
    // получить все данные из таблицы DB_TABLE
    public Cursor getAllData() {
        return mDB.query(DB_TABLE, null, null, null, null, null, null);
    }

//    // добавить запись в DBT_MC
    public void addRecMC(int id, int voite_left, int voite_right, String date_ctr, int status ) {
        ContentValues cv = new ContentValues();
        cv.put(C_MC_ID, id);
        cv.put(C_MC_VOITE_LEFT, voite_left);
        cv.put(C_MC_VOITE_RIGHT, voite_right);
        cv.put(C_MC_DATE_CRT, date_ctr);
        cv.put(C_MC_STATUS, status);
        mDB.insert(DBT_MC, null, cv);
    }

//    // добавить запись в DBT_PHOTO
//    public void addRecPhoto(int id, int id_compare,int ornt, int lor) {
//        ContentValues cv = new ContentValues();
//        cv.put(C_PHOTO_ID, id);
//        cv.put(C_PHOTO_ID_COMPARE, id_compare);
//        cv.put(C_PHOTO_ORIENTATION, ornt);
//        cv.put(C_PHOTO_LOR, lor);
//        mDB.insert(DBT_PHOTO, null, cv);
//    }
//    // добавить запись в DB_TABLE
//    public void addRec(String txt, int img) {
//        ContentValues cv = new ContentValues();
//        cv.put(COLUMN_TXT, txt);
//        cv.put(COLUMN_IMG, img);
//        mDB.insert(DB_TABLE, null, cv);
//    }
//
//    // удалить запись из DB_TABLE
//    public void delRec(long id) {
//        mDB.delete(DB_TABLE, COLUMN_ID + " = " + id, null);
//    }

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
//            db.execSQL(DBT_CREATE);
//            db.execSQL(DB_CREATE);
//            db.execSQL(DB_MA_CREATE);
//            db.execSQL(DB_MC_CREATE);
//            db.execSQL(DB_OC_CREATE);
//            db.execSQL(DB_PHOTO_CREATE);

            // заполняем БД
//            ContentValues cv = new ContentValues();
//            cv.put(C_MC_ID, 9);
//            cv.put(C_MC_VOITE_LEFT, 8);
//            cv.put(C_MC_VOITE_RIGHT, 7);
//            cv.put(C_MC_DATE_CRT, "88");
//            cv.put(C_MC_STATUS, 0);
//            db.insert(DBT_MC, null, cv);
//            ContentValues cv = new ContentValues();
//            for (int i = 1; i < 5; i++) {
//                cv.put(COLUMN_TXT, "sometext " + i);
//                cv.put(COLUMN_IMG, R.drawable.ic_launcher);
//                db.insert(DB_TABLE, null, cv);
//            }
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

//            db.execSQL("DROP TABLE IF EXISTS" + DBT_CREATE);
//            db.execSQL("DROP TABLE IF EXISTS" + DB_TABLE);
//            db.execSQL("DROP TABLE IF EXISTS" + DBT_MA);
//            db.execSQL("DROP TABLE IF EXISTS" + DBT_MC);
//            db.execSQL("DROP TABLE IF EXISTS" + DBT_OC);
//            db.execSQL("DROP TABLE IF EXISTS" + DBT_PHOTO);
//            onCreate(db);
        }
    }
}
