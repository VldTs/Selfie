package ru.tsarcom.slff.slff;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
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

    private File baseDir;

    private static final int CM_DELETE_ID = 1;
    ListView lvData;
    DB_MineCompare db_MC;
    SimpleCursorAdapter scAdapter;

            public ImageLoaderSmall ImageLoaderSmall0;

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
            baseDir=new File(android.os.Environment.getExternalStorageDirectory(),"Selfie");
        else
            baseDir=getCacheDir();
        if(!baseDir.exists())
            baseDir.mkdirs();

        Intent intent = getIntent();
        id_account = intent.getStringExtra("id_account");

        String fileName = intent.getStringExtra(FILE_NAME);
        activity = this;


        File pathf;
        String path;
        String fname;
        pathf = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
/*

        if (android.os.Environment.getExternalStorageState().equals(android.os.Environment.MEDIA_MOUNTED))
            pathf=new File(android.os.Environment.getExternalStorageDirectory(),"sssss");
        else
            pathf=getCacheDir();
        if(!pathf.exists())
            pathf.mkdirs();
//        pathf = new File(pathf.getAbsolutePath() + "/" + "tttt");
//        // создаем каталог
//        pathf.mkdirs();
        path = baseDir.getPath();
        path = pathf.getPath();
        path = path + "";
//        File file = new File(path, "savedBitmap3_2.png");

        Toast.makeText(getBaseContext(), "00000 path = "+path, Toast.LENGTH_LONG).show();
        Bitmap bitmap0;
        Bitmap bitmap;
        Paint paint;
//        String url ="http://95.78.234.20:81/atest/uploads/3/114/83/img.jpg";
        String url ="http://95.78.234.20:81/atest/uploads/3/112/79/img.jpg";

//        public Bitmap getBitmap(String url);

        ImageLoaderSmall0 = new ImageLoaderSmall(getApplicationContext());
        fname = "11";
        bitmap0 = ImageLoaderSmall0.getBitmapWeb(url,pathf,fname);
        if (null ==bitmap0){

            Toast.makeText(getBaseContext(), "bitmap0 = null"+path, Toast.LENGTH_LONG).show();
        }
        bitmap = bitmap0;
        */
//        bitmap = Bitmap.createBitmap(500, 500, Bitmap.Config.RGB_565);
//        MemoryCache memoryCache=new MemoryCache();
//        Bitmap bitmap=memoryCache.get(url);
//        paint = new Paint(Paint.ANTI_ALIAS_FLAG);
//        paint.setTextSize(40);
//
//        Bitmap bmpIcon = BitmapFactory.decodeResource(getResources(), R.drawable.ic_launcher);
//        bmpIcon = Bitmap.createScaledBitmap(bmpIcon, 500, 500, true);
//
//        bitmap = Bitmap.createBitmap(500, 500, Bitmap.Config.RGB_565);
//        Canvas canvas = new Canvas(bitmap);
//        canvas.drawColor(Color.WHITE);
//        canvas.drawBitmap(bmpIcon, 0,0, paint);
//        canvas.drawText("Saved bitmap", 100, 50, paint);

//        try {
//            // отрываем поток для записи
//            BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(
//                    openFileOutput("FILENAME", MODE_PRIVATE)));
//            // пишем данные
//            bw.write("Содержимое файла");
//            // закрываем поток
//            bw.close();
////            Log.d(LOG_TAG, "Файл записан");
//        } catch (FileNotFoundException e) {
//            e.printStackTrace();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//        try {
//            FileOutputStream fos = null;
//            try {
//                fos = new FileOutputStream(file);
//                Toast.makeText(getBaseContext(), "FileOutputStream", Toast.LENGTH_LONG).show();
//                bitmap.compress(Bitmap.CompressFormat.PNG, 100, fos);
//                Toast.makeText(getBaseContext(), "----- файл сохранён компресс", Toast.LENGTH_LONG).show();
//            } finally {
//                if (fos != null){
//                    Toast.makeText(getBaseContext(), "файл сохранён ", Toast.LENGTH_LONG).show();
//                    fos.close();
//                }
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//        }


//        // открываем подключение к БД
        db_MC = new DB_MineCompare(this);
        db_MC.open();
        String max_id = db_MC.getLastIdMC();

        Toast.makeText(getBaseContext(), "max_id = "+max_id, Toast.LENGTH_LONG).show();
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
        scAdapter = new MySimpleCursorAdapter(this, R.layout.mine_list_view_cursor, null, from, to, 0);
        lvSimple.setAdapter(scAdapter);
        // создаем лоадер для чтения данных
        getSupportLoaderManager().initLoader(0, null, this);


    }
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
                                    R.drawable.plus_left, R.drawable.ic_launcher, orientationLeft, orientationRight, 0);
                            File pathf;
                            String path;
//                            pathf = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);

                            pathf = new File(baseDir.getAbsolutePath() + "/" + id);
                            // создаем каталог
                            pathf.mkdirs();
                            path = pathf.getPath();

                            Bitmap bitmap;
                            ImageLoaderSmall0 = new ImageLoaderSmall(getApplicationContext());

//                            pathf = new File(path + "/" + idLeft);
//                            // создаем каталог
//                            pathf.mkdirs();

                            path = path + "";
                            String url;
                            String file_path;
                            File file;

                            file_path = "small"+idLeft+".jpg";
                            file = new File(path,file_path);
//                            Toast.makeText(getBaseContext(), "path = "+path+"/"+file_path, Toast.LENGTH_LONG).show();

                            url ="http://95.78.234.20:81/atest/uploads/"+id_account+"/"+id+"/"+idLeft+"/img.png";

                            bitmap = ImageLoaderSmall0.getBitmap(url);
                            try {
                                FileOutputStream fos = null;
                                try {
                                    fos = new FileOutputStream(file);
                                    bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fos);
                                } finally {
                                    if (fos != null){
//                                        Toast.makeText(getBaseContext(), "файл сохранён ", Toast.LENGTH_LONG).show();
                                        fos.close();
                                    }
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                            file_path = "small"+idRight+".jpg";
                            file = new File(path, file_path);
//                            Toast.makeText(getBaseContext(), "path = "+path+" "+file_path, Toast.LENGTH_LONG).show();

                            url ="http://95.78.234.20:81/atest/uploads/"+id_account+"/"+id+"/"+idRight+"/img.png";

                            bitmap = ImageLoaderSmall0.getBitmap(url);
                            try {
                                FileOutputStream fos = null;
                                try {
                                    fos = new FileOutputStream(file);
                                    bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fos);
                                } finally {
                                    if (fos != null){
//                                        Toast.makeText(getBaseContext(), "файл сохранён ", Toast.LENGTH_LONG).show();
                                        fos.close();
                                    }
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
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
        class MySimpleAdapter extends SimpleAdapter {

            private Context mContext;
            public ImageLoaderSmall ImageLoaderSmall;
            public LayoutInflater inflater=null;
            public MySimpleAdapter(Context context,
                                   List<? extends Map<String, ?>> data, int resource,
                                   String[] from, int[] to) {
                super(context, data, resource, from, to);
                mContext = context;
                inflater = (LayoutInflater)mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                ImageLoaderSmall=new ImageLoaderSmall(mContext.getApplicationContext());
            }

            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                View vi=convertView;
                if(convertView==null)
                    vi = inflater.inflate(R.layout.mine_list_view, null);

                HashMap<String, Object> data = (HashMap<String, Object>) getItem(position);
                TextView tvCid = (TextView)vi.findViewById(R.id.tvCid);
                TextView tvCdate = (TextView)vi.findViewById(R.id.tvCdate);
                TextView tvVoiteLeft = (TextView)vi.findViewById(R.id.tvVoiteLeft);
                TextView tvVoiteRight = (TextView)vi.findViewById(R.id.tvVoiteRight);
                ProgressBar pbLoad = (ProgressBar)vi.findViewById(R.id.pbLoad);
                ImageView ivLeft=(ImageView)vi.findViewById(R.id.ivLeft);
                ImageView ivRight=(ImageView)vi.findViewById(R.id.ivRight);
                String strCid = (String) data.get(ATTRIBUTE_COMPARE_ID);
                String strCdate = (String) data.get(ATTRIBUTE_NAME_DATE_CRT);
                String url_left = (String) data.get(ATTRIBUTE_NAME_LEFT);
                String url_right = (String) data.get(ATTRIBUTE_NAME_RIGHT);
                String all_voiteLeft = (String) data.get(ATTRIBUTE_VOITE_LEFT);
                String all_voiteRight = (String) data.get(ATTRIBUTE_VOITE_RIGHT);
                String imgOrientation_left = (String) data.get(ATTRIBUTE_ORNT_LEFT);
                String imgOrientation_right = (String) data.get(ATTRIBUTE_ORNT_RIGHT);
                tvCid.setText(strCid);
                tvCdate.setText(strCdate);
                tvVoiteLeft.setText(all_voiteLeft);
                tvVoiteRight.setText(all_voiteRight);

                int imgOrientation_left_i = 1;
                try {
                    imgOrientation_left_i = Integer.parseInt(imgOrientation_left);
                } catch(NumberFormatException nfe) {
                    System.out.println("Could not parse " + imgOrientation_left);
                }
                int imgOrientation_right_i = 1;
                try {
                    imgOrientation_right_i = Integer.parseInt(imgOrientation_right);
                } catch(NumberFormatException nfe) {
                    System.out.println("Could not parse " + imgOrientation_right);
                }
//            Toast.makeText(getBaseContext(), "imgOrientation_left = "+imgOrientation_left,
//                    Toast.LENGTH_SHORT).show();
//            Toast.makeText(getBaseContext(), "imgOrientation_right = "+imgOrientation_right,
//                    Toast.LENGTH_SHORT).show();
                int iall_voiteLeft = 1;
                try {
                    iall_voiteLeft = Integer.parseInt(all_voiteLeft);
                } catch(NumberFormatException nfe) {
                    System.out.println("Could not parse " + all_voiteLeft);
                }
                int iall_voiteRight = 0;
                int iall_voite = 0;
                try {
                    iall_voiteRight = Integer.parseInt(all_voiteRight);
                } catch(NumberFormatException nfe) {
                    System.out.println("Could not parse " + all_voiteRight);
                }
                iall_voite = iall_voiteLeft+iall_voiteRight;
                pbLoad.setMax(iall_voite);
                pbLoad.setSecondaryProgress(iall_voiteLeft);
                pbLoad.setProgress(iall_voite);

                int img = R.drawable.abc_ic_clear_mtrl_alpha;
                ImageLoaderSmall.DisplayImage("http://95.78.234.20:81/atest/"+url_left, img, ivLeft, imgOrientation_left_i);
                ImageLoaderSmall.DisplayImage("http://95.78.234.20:81/atest/"+url_right, img, ivRight, imgOrientation_right_i);


                return vi;
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
                    String id_compare = joCCompare.getString("id");

                    intent = new Intent(activity, createActivityClass);
                    intent.putExtra("id_compare", id_compare);
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

                    data4list.remove(data4listPosition);
                    Toast.makeText(getBaseContext(), "data4listPosition = "+data4listPosition, Toast.LENGTH_LONG).show();
                    // уведомляем, что данные изменились
                    sAdapter.notifyDataSetChanged();
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
        Toast.makeText(getBaseContext(), "выход", Toast.LENGTH_LONG).show();
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
                String urlCC;
                urlCC = "http://95.78.234.20:81/atest/jsonDeleteCompare.php?id_compare="+strCid;
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

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle bndl) {
        return new MyCursorLoader(this, db_MC);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
        scAdapter.swapCursor(cursor);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
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
        public ImageLoaderSmall ImageLoaderSmall;
//        public Cursor  cur_t;
        public LayoutInflater inflater=null;
        public MySimpleCursorAdapter(Context context, int layout, Cursor c, String[] from, int[] to, int flags)
        {
            super(context, layout, c, from, to,0);
            mContext = context;
//            this.cur_t = c;
            inflater = (LayoutInflater)mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            ImageLoaderSmall=new ImageLoaderSmall(mContext.getApplicationContext());
        }
        @Override
        public void bindView(View v, Context context, Cursor c) {
            int cid = c.getInt(c.getColumnIndexOrThrow(DB_MineCompare.C_MC_CID));
            String cdate = c.getString(c.getColumnIndexOrThrow(DB_MineCompare.C_MC_DATE_CRT));
            int cVoiteLeft = c.getInt(c.getColumnIndexOrThrow(DB_MineCompare.C_MC_VOITE_LEFT));
            int cVoiteRight = c.getInt(c.getColumnIndexOrThrow(DB_MineCompare.C_MC_VOITE_RIGHT));
            int imgLeft = c.getInt(c.getColumnIndexOrThrow(DB_MineCompare.C_MC_VOITE_LEFT));
            int imgRight = c.getInt(c.getColumnIndexOrThrow(DB_MineCompare.C_MC_VOITE_RIGHT));

            int imgOrientation_left_i = c.getInt(c.getColumnIndexOrThrow(DB_MineCompare.C_MC_ORNT_LEFT));
            int imgOrientation_right_i = c.getInt(c.getColumnIndexOrThrow(DB_MineCompare.C_MC_ORNT_RIGHT));
            TextView tvCid = (TextView) v.findViewById(R.id.tvCid);
            TextView tvCdate = (TextView) v.findViewById(R.id.tvCdate);
            ImageView ivLeft = (ImageView) v.findViewById(R.id.ivLeft);
            ImageView ivRight = (ImageView) v.findViewById(R.id.ivRight);
            TextView tvVoiteLeft = (TextView) v.findViewById(R.id.tvVoiteLeft);
            TextView tvVoiteRight = (TextView) v.findViewById(R.id.tvVoiteRight);
            ProgressBar pbLoad = (ProgressBar) v.findViewById(R.id.pbLoad);
//            Bitmap btm;
//            btm = (Bitmap) imgLeft;
            tvCid.setText(""+cid);
            tvCdate.setText(cdate);
//            ivLeft.setImageResource(0x7f020037);
//            ivLeft.setImageResource(imgLeft);
//            ivLeft.setImageBitmap(btm);
//            ivRight.setImageBitmap(imgRight);
            tvVoiteLeft.setText(""+cVoiteLeft);
            tvVoiteRight.setText(""+cVoiteRight);

            int iall_voite = cVoiteLeft+cVoiteRight;
            pbLoad.setMax(iall_voite);
            pbLoad.setSecondaryProgress(cVoiteLeft);
            pbLoad.setProgress(iall_voite);

            String url_left = "uploads/3/113/81/img.png";
//            String url_right = "uploads/3/167/157/img.png";
//            String url_right = "uploads/3/161/153/img.jpg";
            String url_right = "uploads/3/167/156/img.jpg";
//            http://95.78.234.20:81/atest/uploads/3/167/157/img.png
            int img = R.drawable.abc_ic_clear_mtrl_alpha;
            ImageLoaderSmall.DisplayImage("http://95.78.234.20:81/atest/"+url_left, img, ivLeft, imgOrientation_left_i);
//            ImageLoaderSmall.DisplayImage("http://95.78.234.20:81/atest/"+url_right, img, ivRight, imgOrientation_right_i);
//
//
//
            File pathf0;
//            String path0;
            String fname;
            if (android.os.Environment.getExternalStorageState().equals(android.os.Environment.MEDIA_MOUNTED))
                pathf0=new File(android.os.Environment.getExternalStorageDirectory(),"sssss");
            else
                pathf0=getCacheDir();
            if(!pathf0.exists())
                pathf0.mkdirs();
//            path0 = pathf0.getPath();
//            path0 = path0 + "";
////        File file = new File(path, "savedBitmap3_2.png");
//
////            Toast.makeText(getBaseContext(), "00000 path = "+path0, Toast.LENGTH_LONG).show();
//            Bitmap bitmap00;
////        String url ="http://95.78.234.20:81/atest/uploads/3/114/83/img.jpg";
//            String url0 ="http://95.78.234.20:81/atest/uploads/3/154/149/img.jpg";
//
////        public Bitmap getBitmap(String url);
//
//            ImageLoaderSmall0 = new ImageLoaderSmall(getApplicationContext());
//            bitmap00 = ImageLoaderSmall0.getBitmapWeb(url0,pathf0,fname);
//
            int pid =1;
            fname = ""+pid;
            ImageLoaderSmall.DisplayImageAndSave("http://95.78.234.20:81/atest/"+url_right, img, ivRight,
                    imgOrientation_right_i, pathf0,  cid,  pid, fname);
//            if (null ==bitmap00){
////                Toast.makeText(getBaseContext(), "bitmap00 = null", Toast.LENGTH_LONG).show();
//            }

        }
    }

}
