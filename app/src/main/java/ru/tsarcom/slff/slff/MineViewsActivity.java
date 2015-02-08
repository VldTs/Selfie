package ru.tsarcom.slff.slff;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RadioGroup;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import android.database.Cursor;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.LoaderManager.LoaderCallbacks;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v4.widget.SimpleCursorAdapter;
import android.view.View.OnClickListener;

//public class MineViewsActivity extends Activity  implements View.OnClickListener {
public class MineViewsActivity extends FragmentActivity implements LoaderCallbacks<Cursor>, OnClickListener {
    Button btnOthers,btnCreate,btnSertviceStart;
    TextView tvNickname;
    Intent intent;
    ArrayList<Map<String, Object>> data4list;
    SimpleAdapter sAdapter;
    int data4listPosition;
    String id_account,strError,strCCError;
    JSONObject jObj = null;
    JSONObject jCCObj = null;
    JSONArray jaAccount = null;
    JSONArray jaError = null;
    JSONArray jaCCError = null;
    JSONArray jaCompare = null;
    JSONArray jaPhoto = null;
    JSONArray jaCCompare = null;

    final int MENU_VIEW = 1;
    final int MENU_VOITE_BLUE_LEFT = 2;
    final int MENU_VOITE_RED_RIGHT = 3;
    final int MENU_DELETE = 4;

    final int MENU_SIZE_26 = 5;
    final int MENU_SIZE_30 = 6;

    Bitmap bm;
    LinearLayout llMain;
    RadioGroup rgGravity;
    EditText etName;
    Button btnCreateI;
    Button btnClear;
    public final static String FILE_NAME = "filename";
    int wrapContent = LinearLayout.LayoutParams.WRAP_CONTENT;

    final String LOG_TAG = "myLogs";

    ListView lvMain;
    // имена атрибутов для Map
    final String ATTRIBUTE_COMPARE_ID = "txtId";
    final String ATTRIBUTE_NAME_DATE_CRT = "txtDateCrt";
//    final String ATTRIBUTE_NAME_CHECKED = "checked";
    final String ATTRIBUTE_NAME_LEFT = "imLeft";
    final String ATTRIBUTE_NAME_RIGHT = "imRight";
    final String ATTRIBUTE_ORNT_LEFT = "OrientationLeft";
    final String ATTRIBUTE_ORNT_RIGHT = "OrientationRight";
    final String ATTRIBUTE_VOITE_LEFT = "tvVoiteLeft";
    final String ATTRIBUTE_VOITE_RIGHT = "tvVoiteRight";
    final String ATTRIBUTE_NAME_PB = "pb";
    ImageView imagetest;
    ListView lvSimple;
    int loader;
    MineViewsActivity activity;
    Class<CreateActivity> createActivityClass;
    int strGlobalCid;

    private File baseDir;

    private static final int CM_DELETE_ID = 1;
    ListView lvData;
    DB_MineCompare db_MC;
//    public static SimpleCursorAdapter scAdapter;
    public static MySimpleCursorAdapter scAdapter;
//    private static MySimpleCursorAdapter scAdapter;
    Cursor cursorGlobal;

    BroadcastReceiver br;

    File pathf0;

    public final static String BROADCAST_ACTION = "ru.tsarcom.slff.slff";
            public ImageLoaderSmall ImageLoaderSmall0;

    @Deprecated
    public static final int FLAG_AUTO_REQUERY = 0x01;
    public static final int FLAG_REGISTER_CONTENT_OBSERVER = 0x02;
//    ProgressDialog progress;
//    @BeforeCreate
//    public void getProgressDialog(){
//        progress = new ProgressDialog(this);
//        progress.setMessage("Loading...");
//    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mine_views);

        if (android.os.Environment.getExternalStorageState().equals(android.os.Environment.MEDIA_MOUNTED))
            pathf0=new File(android.os.Environment.getExternalStorageDirectory(),"sssss");
        else
            pathf0=getCacheDir();
        if(!pathf0.exists())
            pathf0.mkdirs();
//        if (android.os.Environment.getExternalStorageState().equals(android.os.Environment.MEDIA_MOUNTED))
//            baseDir=new File(android.os.Environment.getExternalStorageDirectory(),"Selfie");
//        else
//            baseDir=getCacheDir();
//        if(!baseDir.exists())
//            baseDir.mkdirs();

        Intent intent = getIntent();
        id_account = intent.getStringExtra("id_account");

        String fileName = intent.getStringExtra(FILE_NAME);
        activity = this;

//        // открываем подключение к БД
        db_MC = new DB_MineCompare(this);
        db_MC.open();
        String max_id = db_MC.getLastIdMC();

//        Toast.makeText(getBaseContext(), "max_id = "+max_id, Toast.LENGTH_LONG).show();
        if(isConnected()) {
            String url0;
            url0 = "http://95.78.234.20:81/atest/jsonMine.php?id_account="+id_account+"&max_mcid="+max_id;
            new HttpAsyncTask().execute(url0);
        }else{
                   Toast.makeText(getBaseContext(), "Нет Соединения с интернетом :(", Toast.LENGTH_LONG).show();
        }

        tvNickname = (TextView)findViewById(R.id.tvNickname);
        tvNickname.setText(id_account);

        btnOthers = (Button) findViewById(R.id.btnOthers);
        btnOthers.setOnClickListener(this);
        btnCreate = (Button) findViewById(R.id.btnCreate);
        btnCreate.setOnClickListener(this);

        // находим список
        lvSimple = (ListView) findViewById(R.id.lvSimple);

        // для tvColor и tvSize необходимо создавать контекстное меню
        registerForContextMenu(lvSimple);

        lvSimple.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {

                TextView tvCid = (TextView) view.findViewById(R.id.tvCid);
                String strCid = tvCid.getText().toString();
//                Toast.makeText(getBaseContext(), "strCid = "+strCid, Toast.LENGTH_LONG).show();
                Intent intent = new Intent(activity, ViewActivity.class);
                intent.putExtra("id_account", id_account);
                intent.putExtra("id_compare", strCid);
                startActivity(intent);
            }
        });


        // формируем столбцы сопоставления

        String[] from = new String[] { DB_MineCompare.C_MC_CID, DB_MineCompare.C_MC_DATE_CRT,
                DB_MineCompare.C_MC_PHOTO_LEFT,  DB_MineCompare.C_MC_PHOTO_RIGHT,
                DB_MineCompare.C_MC_VOITE_LEFT,  DB_MineCompare.C_MC_VOITE_RIGHT,
//                DB_MineCompare.C_MC_ORNT_LEFT,  DB_MineCompare.C_MC_ORNT_RIGHT
        };
        int[] to = new int[] { R.id.tvCid, R.id.tvCdate, R.id.ivLeft,R.id.ivRight,
                R.id.tvVoiteLeft, R.id.tvVoiteRight,
//                R.id.tvVoiteLeft, R.id.tvVoiteLeft
        };

        // создааем адаптер и настраиваем список
        scAdapter = new MySimpleCursorAdapter(this, R.layout.mine_list_view_cursor, null, from, to, FLAG_AUTO_REQUERY);
//        scAdapter = new MySimpleCursorAdapter(this, R.layout.mine_list_view_cursor, null, from, to, FLAG_REGISTER_CONTENT_OBSERVER);
        lvSimple.setAdapter(scAdapter);
        // создаем лоадер для чтения данных
        getSupportLoaderManager().initLoader(0, null, this);
//        getSupportLoaderManager().restartLoader(0, null, this);
//        android.content.Loader<Object> objectLoader = getLoaderManager().restartLoader(0, null, this);

        // создаем BroadcastReceiver
        br = new BroadcastReceiver() {
            // действия при получении сообщений
            public void onReceive(Context context, Intent intent) {
                int task = intent.getIntExtra("PARAM_TASK", 0);
                int status = intent.getIntExtra("PARAM_STATUS", 0);
                Log.d(LOG_TAG, "onReceive: task = " + task + ", status = " + status);

//                Toast.makeText(getBaseContext(), "BroadcastReceiver ", Toast.LENGTH_LONG).show();

                getSupportLoaderManager().restartLoader(0, null, activity);
                // Ловим сообщения о старте задач
                if (status  == 100) {//STATUS_START
                    switch (task) {
                        case 1:
//                Toast.makeText(getBaseContext(), "strCid = "+strCid, Toast.LENGTH_LONG).show();
//                            tvTask1.setText("Task1 start");
                            break;
//                        case TASK2_CODE:
//                            tvTask2.setText("Task2 start");
//                            break;
//                        case TASK3_CODE:
//                            tvTask3.setText("Task3 start");
//                            break;
                    }
                }

//                // Ловим сообщения об окончании задач
//                if (status == STATUS_FINISH) {
//                    int result = intent.getIntExtra(PARAM_RESULT, 0);
//                    switch (task) {
//                        case TASK1_CODE:
//                            tvTask1.setText("Task1 finish, result = " + result);
//                            break;
//                        case TASK2_CODE:
//                            tvTask2.setText("Task2 finish, result = " + result);
//                            break;
//                        case TASK3_CODE:
//                            tvTask3.setText("Task3 finish, result = " + result);
//                            break;
//                    }
//                }
            }
        };
        // создаем фильтр для BroadcastReceiver
        IntentFilter intFilt = new IntentFilter(BROADCAST_ACTION);
        // регистрируем (включаем) BroadcastReceiver
        registerReceiver(br, intFilt);
    }
public static void update(){
    scAdapter.notifyDataSetChanged();
//        getSupportLoaderManager().restartLoader(0, null, this);
}

//    @Override
//    public synchronized void onStart(Intent intent, int startId) {
//        super.onStart(intent, startId);
//
//
//        if(!isRunning){
//            mythread.start();
//            isRunning = true;
//        }
//    }
//    if(!isRunning){
//        mythread.start();
//        isRunning = true;
//    }

    // check network connection
    public boolean isConnected(){
        ConnectivityManager connMgr = (ConnectivityManager) getSystemService(this.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.isConnected())
            return true;
        else
            return false;
    }
    class MyViewBinder implements SimpleAdapter.ViewBinder {

        @Override
        public boolean setViewValue(View view, Object data,
                                    String textRepresentation) {
            int i = 0;
            switch (view.getId()) {
                // LinearLayout
                // ProgressBar
                case R.id.pbLoad:
                    i = ((Integer) data).intValue();
                    ((ProgressBar)view).setProgress(i);
                    return true;
            }
            return false;
        }
    }
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnOthers:
                // кнопка
                intent = new Intent(this, OthersViewsActivity.class);
                intent.putExtra("id_account", id_account);
                startActivity(intent);
                break;
            case R.id.btnCreate:
                // кнопка
//                db_MC.addRecMC(999, "9999-02-25 ", 1 ,1 ,R.drawable.plus_left, R.drawable.ic_launcher, 0, 0, 0);
                createActivityClass = CreateActivity.class;

                if(isConnected()) {
                    String urlCC;
                    urlCC = "http://95.78.234.20:81/atest/jsonCreateCompare.php?id_account="+id_account;
                    new CreateCompareHttpAsyncTask().execute(urlCC);
                }else{
                    Toast.makeText(getBaseContext(), "Нет Соединения с интернетом :(", Toast.LENGTH_LONG).show();
                }
                break;
        }
    }

    private class HttpAsyncTask extends AsyncTask<String, Void, String> {
        public String JsonString_t;
        @Override
        protected String doInBackground(String... urls) {
            GetFromURL oGetURL = new GetFromURL();
            return oGetURL.GET(urls[0]);
        }
        // onPostExecute displays the results of the AsyncTask.
        @Override
        protected void onPostExecute(String result) {
            //   Toast.makeText(getBaseContext(), "Received!", Toast.LENGTH_LONG).show();

            try {
                jObj = new JSONObject(result);
                // Getting JSON Array
                jaError = jObj.getJSONArray("error");
                JSONObject joError = jaError.getJSONObject(0);
                strError = joError.getString("status");
                if (strError.equals("none")) {
                    jaAccount = jObj.getJSONArray("account");
                    JSONObject joAccount = jaAccount.getJSONObject(0);
                    String strNickname = joAccount.getString("nickname");
//                    tvNickname.setText(strNickname);

                    activity.setTitle("Selfie - " + strNickname);
                    jaCompare = jObj.getJSONArray("compare");
                    Integer iCompareLen = jaCompare.length();
                    JSONObject joCompare0 = jaCompare.getJSONObject(0);
                    String description0 = joCompare0.getString("description");

                    //Toast.makeText(getBaseContext(), "description0 "+ description0, Toast.LENGTH_LONG).show();
                    if (description0.equals("notset")) {
                         Toast.makeText(getBaseContext(), "Для начала нажмите 'Создать' ", Toast.LENGTH_LONG).show();
                    }else{
                        String strCompareLen = iCompareLen.toString();
                        // массивы данных
                        int img = R.drawable.ic_launcher;
                        // Image url
                        String image_url = "http://api.androidhive.info/images/sample.jpg";

                        loader = R.drawable.ic_launcher;
                        data4list = new ArrayList<Map<String, Object>>(
                                iCompareLen);
                        Map<String, Object> m;
                        for (int iC = 0; iC < iCompareLen; iC = iC + 1) {
                            JSONObject joCompare = jaCompare.getJSONObject(iC);
                            int id = joCompare.getInt("id");
                            String date_crt = joCompare.getString("date_crt");

                            jaPhoto = joCompare.getJSONArray("photo");

                            JSONObject joPhotoLeft = jaPhoto.getJSONObject(0);
                            String pathLeft = joPhotoLeft.getString("path");
                            int idLeft =  joPhotoLeft.getInt("id");
                            int orientationLeft =  joPhotoLeft.getInt("Orientation");
                            int all_voiteLeft = joPhotoLeft.getInt("all_voite");

                            JSONObject joPhotoRight = jaPhoto.getJSONObject(1);
                            String pathRight = joPhotoRight.getString("path");
                            int idRight =  joPhotoRight.getInt("id");
                            int orientationRight = joPhotoRight.getInt("Orientation");
                            int all_voiteRight = joPhotoRight.getInt("all_voite");
    //                        Toast.makeText(getBaseContext(), "pathRight "+pathRight, Toast.LENGTH_LONG).show();

                            db_MC.addRecMC(id, date_crt, all_voiteLeft ,all_voiteRight ,
                                    idLeft, idRight, orientationLeft, orientationRight, pathLeft, pathRight, 0);

                        }
                    }

                }else{
                    Toast.makeText(getBaseContext(), "простите, не получается прочитать ваши Selfie :(", Toast.LENGTH_LONG).show();
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }
            if (strError.equals("none")) {
                Toast.makeText(getBaseContext(), "ваших Selfie загружены", Toast.LENGTH_LONG).show();
            }else{
                Toast.makeText(getBaseContext(), "простите, не могу загрузить список ваших Selfie :(", Toast.LENGTH_LONG).show();
            }
        }


    }

    //----
    private class CreateCompareHttpAsyncTask extends AsyncTask<String, Void, String> {
        public String JsonString_t;
        @Override
        protected String doInBackground(String... urls) {
            GetFromURL oGetURL = new GetFromURL();
            return oGetURL.GET(urls[0]);
        }
        // onPostExecute displays the results of the AsyncTask.
        @Override
        protected void onPostExecute(String result) {
            //   Toast.makeText(getBaseContext(), "Received!", Toast.LENGTH_LONG).show();
            try {
                jCCObj = new JSONObject(result);
                // Getting JSON Array
                jaCCError = jCCObj.getJSONArray("error");
                JSONObject joCCError = jaCCError.getJSONObject(0);
                strCCError = joCCError.getString("status");
                if (strCCError.equals("none")) {
                    jaCCompare = jCCObj.getJSONArray("compare");
                    JSONObject joCCompare = jaCCompare.getJSONObject(0);
                    int id_compare = joCCompare.getInt("id");
                    String id_compare_s = joCCompare.getString("id");
                    String date_crt = joCCompare.getString("date_crt");

                    db_MC.addRecMC(id_compare, date_crt, 0 ,0 ,
                            0, 0, 0, 0, "", "", 0);
                    intent = new Intent(activity, createActivityClass);
                    intent.putExtra("id_compare", id_compare_s);
                    intent.putExtra("id_account", id_account);
                    startActivity(intent);

                }else{
                    Toast.makeText(getBaseContext(), "простите, не вышло создать Selfie :(", Toast.LENGTH_LONG).show();
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            // Toast.makeText(getBaseContext(), "2222222222 status_user "+status_user, Toast.LENGTH_LONG).show();
            if (strError.equals("none")) {
              //  Toast.makeText(getBaseContext(), "+++Good", Toast.LENGTH_LONG).show();
            }else{
                Toast.makeText(getBaseContext(), "простите, не смог создать Selfie :(", Toast.LENGTH_LONG).show();
            }
        }
    }
    //---- Delete
    private class DeleteCompareHttpAsyncTask extends AsyncTask<String, Void, String> {
        public String JsonString_t;
        @Override
        protected String doInBackground(String... urls) {
            GetFromURL oGetURL = new GetFromURL();
            return oGetURL.GET(urls[0]);
        }
        // onPostExecute displays the results of the AsyncTask.
        @Override
        protected void onPostExecute(String result) {
            //   Toast.makeText(getBaseContext(), "Received!", Toast.LENGTH_LONG).show();
            try {
                jCCObj = new JSONObject(result);
                // Getting JSON Array
                jaCCError = jCCObj.getJSONArray("error");
                JSONObject joCCError = jaCCError.getJSONObject(0);
                strCCError = joCCError.getString("status");
                if (strCCError.equals("none")) {

//                    data4list.remove(data4listPosition);
//                    db_MC.delRec(strGlobalCid);
//                    Toast.makeText(getBaseContext(), "data4listPosition = "+data4listPosition, Toast.LENGTH_LONG).show();
                    // уведомляем, что данные изменились
//                    Bundle b=null;
//                    MineViewsActivity.this.onCreate(b);

                    new Thread(new Runnable() {
                        public void run() {
                            runOnUiThread(new Runnable() {
                                public void run() {

                                    db_MC.delRec(strGlobalCid);
//                                    cursorGlobal.requery();
                                    scAdapter.getCursor().requery();
                                    scAdapter.notifyDataSetChanged();

//                                    Log.d(TAG, "Button Click ");
//                                    messageText.setText("Загрузка начата.....");
                                }
                            });
//                            uploadFile(selectedImagePath,imageOrient, imageLR);
                        }
                    }).start();


                    Toast.makeText(getBaseContext(), "удалено ваше Selfie :(", Toast.LENGTH_LONG).show();
                }else{
                    Toast.makeText(getBaseContext(), "простите, не вышло удалить Selfie :(", Toast.LENGTH_LONG).show();
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            // Toast.makeText(getBaseContext(), "2222222222 status_user "+status_user, Toast.LENGTH_LONG).show();
            if (strError.equals("none")) {
                //  Toast.makeText(getBaseContext(), "+++Good", Toast.LENGTH_LONG).show();
            }else{
                Toast.makeText(getBaseContext(), "простите, не смог удалить Selfie :(", Toast.LENGTH_LONG).show();
            }
        }

    }
    public void onBackPressed() {
        // вызываем диалог
//        Toast.makeText(getBaseContext(), "выход", Toast.LENGTH_LONG).show();
//        super.finish();
        finish();
//        showDialog(DIALOG_EXIT);
    }
    @Override
    public void onCreateContextMenu(ContextMenu menu, View v,
                                    ContextMenu.ContextMenuInfo menuInfo) {
        // TODO Auto-generated method stub
        switch (v.getId()) {
            case R.id.lvSimple:
                menu.add(0, MENU_VIEW, 0, "Посмотреть");
                menu.add(0, MENU_VOITE_BLUE_LEFT, 0, "Проголосавать за Синию");
                menu.add(0, MENU_VOITE_RED_RIGHT, 0, "Проголосавать за Красную");
                menu.add(0, MENU_DELETE, 0, "Удалить");
                break;
        }
    }
    @Override
    public boolean onContextItemSelected(MenuItem item) {
        // TODO Auto-generated method stub
        // получаем инфу о пункте списка
        AdapterView.AdapterContextMenuInfo acmi = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        TextView tvCid = (TextView) acmi.targetView.findViewById(R.id.tvCid);
        String strCid = tvCid.getText().toString();

        try {
            strGlobalCid = Integer.parseInt(strCid);
        } catch(NumberFormatException nfe) {
            System.out.println("Could not parse " + strCid);
        }

        switch (item.getItemId()) {
            case MENU_VIEW:
                // удаляем Map из коллекции, используя позицию пункта в списке
//                data4list.remove(acmi.position);
//                // уведомляем, что данные изменились
//                sAdapter.notifyDataSetChanged();
                Toast.makeText(getBaseContext(), "view "+acmi.id+" - "+strCid, Toast.LENGTH_LONG).show();
                Intent intent = new Intent(activity, ViewActivity.class);
                intent.putExtra("id_account", id_account);
                intent.putExtra("id_compare", strCid);
                startActivity(intent);
                break;
            case MENU_VOITE_BLUE_LEFT:
                Toast.makeText(getBaseContext(), "left", Toast.LENGTH_LONG).show();
                break;
            case MENU_VOITE_RED_RIGHT:
                Toast.makeText(getBaseContext(), "right", Toast.LENGTH_LONG).show();
                break;
            case MENU_DELETE:
                data4listPosition = acmi.position;
//                Toast.makeText(getBaseContext(), "data4listPosition = "+data4listPosition+"  strCid = "+strCid, Toast.LENGTH_LONG).show();
                String urlCC;
                urlCC = "http://95.78.234.20:81/atest/jsonDeleteCompare.php?id_compare="+strCid+"&id_account="+id_account;
                new DeleteCompareHttpAsyncTask().execute(urlCC);
                break;
            case MENU_SIZE_26:
                break;
            case MENU_SIZE_30:
                break;
        }
        return super.onContextItemSelected(item);
    }

    protected void onDestroy() {
        super.onDestroy();
        // закрываем подключение при выходе
        db_MC.close();
    }

//    @Override
//    public Loader<Cursor> onCreateLoader(int id, Bundle bndl) {
//        return new MyCursorLoader(this, db_MC);
//    }
//
//    @Override
//    public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
//        scAdapter.swapCursor(cursor);
//    }
//
//    @Override
//    public void onLoaderReset(Loader<Cursor> loader) {
//    }
    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
//        CursorLoader cursorLoader =
//                new CursorLoader(this,
//                        YOUR_URI,
//                        YOUR_PROJECTION, null, null, null);
//        return cursorLoader;

        return new MyCursorLoader(this, db_MC);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        scAdapter.swapCursor(data);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        scAdapter.swapCursor(null);
    }

    static class MyCursorLoader extends CursorLoader {

        DB_MineCompare db_MC_;

        public MyCursorLoader(Context context, DB_MineCompare db_MC_i) {
            super(context);
            this.db_MC_ = db_MC_i;
        }

        @Override
        public Cursor loadInBackground() {
            Cursor cursor = db_MC_.getAllDataMC();
            try {
                TimeUnit.SECONDS.sleep(0);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return cursor;
        }

    }

    public class MySimpleCursorAdapter extends SimpleCursorAdapter{
        private Context mContext;
        public ImageLoaderSmallAndSave ImageLoaderSmallAndSave;
        public LayoutInflater inflater=null;
        public MySimpleCursorAdapter(Context context, int layout, Cursor c, String[] from, int[] to, int flags)
        {
            super(context, layout, c, from, to,flags);
            mContext = context;
            inflater = (LayoutInflater)mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            ImageLoaderSmallAndSave=new ImageLoaderSmallAndSave(mContext.getApplicationContext());
        }
        @Override
        public void changeCursor(Cursor cursor) {
            // TODO Auto-generated method stub
            super.changeCursor(cursor);
        }
        // The newView method is used to inflate a new view and return it,
        // you don't bind any data to the view at this point.
        @Override
        public View newView(Context context, Cursor cursor, ViewGroup parent) {
            return LayoutInflater.from(context).inflate(R.layout.mine_list_view_cursor, parent, false);
        }
        public View getView(int position, View convertView, ViewGroup parent) {
            if (!mDataValid) {
                throw new IllegalStateException("this should only be called when the cursor is valid");
            }
            if (!mCursor.moveToPosition(position)) {
                throw new IllegalStateException("couldn't move cursor to position " + position);
            }
            View v;
            if (convertView == null) {
                v = newView(mContext, mCursor, parent);
            } else {
                v = convertView;
            }
            bindView(v, mContext, mCursor);
            return v;
        }
        @Override
        public void bindView(View v, Context context, Cursor c) {
            cursorGlobal = c;
            int cid = c.getInt(c.getColumnIndexOrThrow(DB_MineCompare.C_MC_CID));
            String cdate = c.getString(c.getColumnIndexOrThrow(DB_MineCompare.C_MC_DATE_CRT));
            int cVoiteLeft = c.getInt(c.getColumnIndexOrThrow(DB_MineCompare.C_MC_VOITE_LEFT));
            int cVoiteRight = c.getInt(c.getColumnIndexOrThrow(DB_MineCompare.C_MC_VOITE_RIGHT));
            int cIdLeft = c.getInt(c.getColumnIndexOrThrow(DB_MineCompare.C_MC_PHOTO_LEFT));
            int cIdRight = c.getInt(c.getColumnIndexOrThrow(DB_MineCompare.C_MC_PHOTO_RIGHT));

            int imgOrientation_left_i = c.getInt(c.getColumnIndexOrThrow(DB_MineCompare.C_MC_ORNT_LEFT));
            int imgOrientation_right_i = c.getInt(c.getColumnIndexOrThrow(DB_MineCompare.C_MC_ORNT_RIGHT));
            String path_left = c.getString(c.getColumnIndexOrThrow(DB_MineCompare.C_MC_PATH_LEFT));
            String path_right = c.getString(c.getColumnIndexOrThrow(DB_MineCompare.C_MC_PATH_RIGHT));
            TextView tvCid = (TextView) v.findViewById(R.id.tvCid);
            TextView tvCdate = (TextView) v.findViewById(R.id.tvCdate);
            ImageView ivLeft = (ImageView) v.findViewById(R.id.ivLeft);
            ImageView ivRight = (ImageView) v.findViewById(R.id.ivRight);
            TextView tvVoiteLeft = (TextView) v.findViewById(R.id.tvVoiteLeft);
            TextView tvVoiteRight = (TextView) v.findViewById(R.id.tvVoiteRight);
            ProgressBar pbLoad = (ProgressBar) v.findViewById(R.id.pbLoad);
            tvCid.setText(""+cid);
            tvCdate.setText(cdate);
            tvVoiteLeft.setText(""+cVoiteLeft);
            tvVoiteRight.setText(""+cVoiteRight);

            int iall_voite = cVoiteLeft+cVoiteRight;
            pbLoad.setMax(iall_voite);
            pbLoad.setSecondaryProgress(cVoiteLeft);
            pbLoad.setProgress(iall_voite);

            String url_left;
            url_left = "uploads/"+id_account+"/"+cid+"/"+cIdLeft+"/img.jpg";
            if (id_account.isEmpty() || id_account.equals("0") || cid<=0 || cIdLeft <=0 ){
                url_left = "empty.png";
            }
//            url_left = "http://95.78.234.20:81/atest/"+path_left;
            url_left = "http://95.78.234.20:81/atest/"+url_left;

            String url_right;
            url_right = "uploads/"+id_account+"/"+cid+"/"+cIdRight+"/img.jpg";
            if (id_account.isEmpty() || id_account.equals("0") || cid<=0 || cIdRight <=0 ){
                url_right = "empty.png";
            }
//            url_right = "http://95.78.234.20:81/atest/"+path_right;
            url_right = "http://95.78.234.20:81/atest/"+url_right;

            int img = R.drawable.abc_ic_clear_mtrl_alpha;
//            ImageLoaderSmall.DisplayImage("http://95.78.234.20:81/atest/"+url_left, img, ivLeft, imgOrientation_left_i);

            String fname;
            fname = ""+cIdRight+"";
//            ImageLoaderSmall.DisplayImage(url_left, img, ivLeft, imgOrientation_left_i);
            ImageLoaderSmallAndSave.DisplayImageAndSave(url_left, img, ivLeft, imgOrientation_left_i,
                    pathf0,  cid,  cIdLeft, fname);
            fname = ""+cIdLeft+"";
//            ImageLoaderSmall.DisplayImage(url_right, img, ivRight, imgOrientation_right_i);
            ImageLoaderSmallAndSave.DisplayImageAndSave(url_right, img, ivRight, imgOrientation_right_i,
                    pathf0,  cid,  cIdRight, fname);

        }
    }

}
