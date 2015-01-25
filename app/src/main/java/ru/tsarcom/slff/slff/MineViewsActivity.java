package ru.tsarcom.slff.slff;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
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


    private static final int CM_DELETE_ID = 1;
    ListView lvData;
    DB_MineCompare db_MC;
    SimpleCursorAdapter scAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mine_views);

        Intent intent = getIntent();
        id_account = intent.getStringExtra("id_account");

        String fileName = intent.getStringExtra(FILE_NAME);
        activity = this;
        if(isConnected()) {

            String url0;
            url0 = "http://95.78.234.20:81/atest/jsonMine.php?id_account="+id_account;
//            new HttpAsyncTask().execute(url0);
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

//        // открываем подключение к БД
        db_MC = new DB_MineCompare(this);
        db_MC.open();

        // формируем столбцы сопоставления

//        String[] from = new String[] { DB_MineCompare.C_MC_CID,DB_MineCompare.C_MC_PHOTO_LEFT ,DB_MineCompare.C_MC_PHOTO_RIGHT, DB_MineCompare.C_MC_DATE_CRT };
//        int[] to = new int[] { R.id.tvCid, R.id.ivLeft,R.id.ivRight, R.id.tvCdate };
//
//        // создааем адаптер и настраиваем список
//        scAdapter = new MySimpleCursorAdapter(this, R.layout.item_test, null, from, to, 0);
//        lvSimple.setAdapter(scAdapter);
////
////
////        // создаем лоадер для чтения данных
//        getSupportLoaderManager().initLoader(0, null, this);


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

//        int red = getResources().getColor(R.color.Red);
//        int orange = getResources().getColor(R.color.Orange);
//        int green = getResources().getColor(R.color.Green);

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

                createActivityClass = CreateActivity.class;

                if(isConnected()) {
                    String urlCC;
                    urlCC = "http://95.78.234.20:81/atest/jsonCreateCompare.php?id_account="+id_account;
                    new CreateCompareHttpAsyncTask().execute(urlCC);
                }else{

                    Toast.makeText(getBaseContext(), "Нет Соединения с интернетом :(", Toast.LENGTH_LONG).show();
                }
                break;
//            case R.id.btnSertviceStart:
//                // кнопка
//                startService(new Intent(this, SelfieServiceF.class).putExtra("id_account", id_account));
//                break;
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
//                        Toast.makeText(getBaseContext(), "strCompareLen = " + strCompareLen, Toast.LENGTH_LONG).show();

                        // массивы данных
                        int img = R.drawable.ic_launcher;
                        // Image url
                        String image_url = "http://api.androidhive.info/images/sample.jpg";

                        // ImageLoaderSmall class instance
//                        ImageLoaderSmall imgLoader = new ImageLoaderSmall(getApplicationContext());


                        loader = R.drawable.ic_launcher;

//                        imagetest.setImageBitmap(imgLoader.getBitmap(image_url));
                        // упаковываем данные в понятную для адаптера структуру
//                        bm = imgLoader.getBitmap(image_url);
                        data4list = new ArrayList<Map<String, Object>>(
                                iCompareLen);
                        Map<String, Object> m;
                        for (int iC = 0; iC < iCompareLen; iC = iC + 1) {
                            JSONObject joCompare = jaCompare.getJSONObject(iC);
                            String id = joCompare.getString("id");
                            String date_crt = joCompare.getString("date_crt");

                            jaPhoto = joCompare.getJSONArray("photo");
                            JSONObject joPhotoLeft = jaPhoto.getJSONObject(0);
                            String pathLeft = joPhotoLeft.getString("path");
                            String orientationLeft =  joPhotoLeft.getString("Orientation");
                            String all_voiteLeft = joPhotoLeft.getString("all_voite");
    //                        Toast.makeText(getBaseContext(), "pathLeft "+pathLeft, Toast.LENGTH_LONG).show();
                            JSONObject joPhotoRight = jaPhoto.getJSONObject(1);
                            String pathRight = joPhotoRight.getString("path");
                            String orientationRight = joPhotoRight.getString("Orientation");
                            String all_voiteRight = joPhotoRight.getString("all_voite");
    //                        Toast.makeText(getBaseContext(), "pathRight "+pathRight, Toast.LENGTH_LONG).show();

                            m = new HashMap<String, Object>();
                            m.put(ATTRIBUTE_COMPARE_ID, id);
                            m.put(ATTRIBUTE_NAME_DATE_CRT, date_crt);
//                            ImageLoaderSmall imgLoader2 = new ImageLoaderSmall(getApplicationContext());
//                            m.put(ATTRIBUTE_NAME_LEFT, imgLoader.getBitmap(image_url));
                            m.put(ATTRIBUTE_VOITE_LEFT, all_voiteLeft);
                            m.put(ATTRIBUTE_VOITE_RIGHT, all_voiteRight);
                            m.put(ATTRIBUTE_NAME_LEFT, pathLeft);
    //                        m.put(ATTRIBUTE_NAME_LEFT, bm);
                            m.put(ATTRIBUTE_NAME_RIGHT, pathRight);
                            m.put(ATTRIBUTE_NAME_PB, 2);
                            m.put(ATTRIBUTE_ORNT_LEFT, orientationLeft);
                            m.put(ATTRIBUTE_ORNT_RIGHT, orientationRight);
                            data4list.add(m);
                        }

                        // массив имен атрибутов, из которых будут читаться данные
                        String[] from = {ATTRIBUTE_COMPARE_ID,
                                ATTRIBUTE_NAME_DATE_CRT,
    //                ATTRIBUTE_NAME_CHECKED,
                                ATTRIBUTE_NAME_LEFT, ATTRIBUTE_NAME_RIGHT,ATTRIBUTE_VOITE_LEFT,ATTRIBUTE_VOITE_RIGHT, ATTRIBUTE_NAME_PB,
                                ATTRIBUTE_ORNT_LEFT,ATTRIBUTE_ORNT_RIGHT};
                        // массив ID View-компонентов, в которые будут вставлять данные
                        int[] to = {R.id.tvCid, R.id.tvCdate, R.id.ivLeft, R.id.ivRight, R.id.tvVoiteLeft, R.id.tvVoiteRight, R.id.pbLoad,};

                        // создаем адаптер
                        sAdapter = new MySimpleAdapter(activity, data4list, R.layout.mine_list_view,
                                from, to);

                        // Указываем адаптеру свой биндер
                        sAdapter.setViewBinder(new MyViewBinder());
                        // к списоку присваиваем ему адаптер
                        lvSimple.setAdapter(sAdapter);


//                        // формируем столбцы сопоставления
//                        String[] from_c = new String[] { DB.COLUMN_IMG, DB.COLUMN_TXT };
////                        int[] to = new int[] { R.id.ivImg, R.id.tvText };
//
//                        // создааем адаптер и настраиваем список
//                        scAdapter = new SimpleCursorAdapter(activity, R.layout.mine_list_view, null, from, to, 0);
////                        lvData = (ListView) findViewById(R.id.lvData);
//                        lvSimple.setAdapter(scAdapter);
    //                    JSONObject joCompare = jaCompare.getJSONObject(0);
    //                    String date_crt = joCompare.getString("date_crt");
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
//                pbLoad.setMax(5);
//                pbLoad.setSecondaryProgress(3);
//                pbLoad.setProgress(5);

                int img = R.drawable.abc_ic_clear_mtrl_alpha;
//                if (url_left.equals("empty")) {
//                    url_left = "empty.png";
//                }
//                if (url_right.equals("empty")) {
//                    url_right = "empty.png";
//                }
                ImageLoaderSmall.DisplayImage("http://95.78.234.20:81/atest/"+url_left, img, ivLeft, imgOrientation_left_i);
                ImageLoaderSmall.DisplayImage("http://95.78.234.20:81/atest/"+url_right, img, ivRight, imgOrientation_right_i);
                return vi;
            }
        }



        /*
        class MySimpleAdapter2 extends SimpleAdapter {

            ArrayList<MineCompare> objects;
            private Context mContext;
            public ImageLoader imageLoader;
            public LayoutInflater inflater=null;
            public MySimpleAdapter2(Context context,
                                    ArrayList<MineCompare> MineCompares, int resource,
                                   String[] from, int[] to) {
                super(context, objects, resource, from, to);
                mContext = context;
                inflater = (LayoutInflater)mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                imageLoader=new ImageLoader(mContext.getApplicationContext());
            }

            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
//                View vi=convertView;
//                if(convertView==null)
//                    vi = inflater.inflate(R.layout.mine_list_view, null);
//
//                HashMap<String, Object> data = (HashMap<String, Object>) getItem(position);
//                TextView tvCid = (TextView)vi.findViewById(R.id.tvCid);
//                TextView tvCdate = (TextView)vi.findViewById(R.id.tvCdate);
//                ImageView ivLeft=(ImageView)vi.findViewById(R.id.ivLeft);
//                ImageView ivRight=(ImageView)vi.findViewById(R.id.ivRight);
//                String strCid = (String) data.get(ATTRIBUTE_COMPARE_ID);
//                String strCdate = (String) data.get(ATTRIBUTE_NAME_DATE_CRT);
//                String url_left = (String) data.get(ATTRIBUTE_NAME_LEFT);
//                String url_right = (String) data.get(ATTRIBUTE_NAME_RIGHT);
//                tvCid.setText(strCid);
//                tvCdate.setText(strCdate);
//
//                int img = R.drawable.abc_ic_clear_normal;
//                imageLoader.DisplayImage(url_left, img, ivLeft);
//                imageLoader.DisplayImage(url_right, img, ivRight);
//                return vi;
            }
        }
        */
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
//            case R.id.tvSize:
//                menu.add(0, MENU_SIZE_22, 0, "22");
//                menu.add(0, MENU_SIZE_26, 0, "26");
//                menu.add(0, MENU_SIZE_30, 0, "30");
//                break;
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

        public MyCursorLoader(Context context, DB_MineCompare db_MC) {
            super(context);
            this.db_MC_ = db_MC;
        }

        @Override
        public Cursor loadInBackground() {
            Cursor cursor = db_MC_.getAllDataMC();
            try {
                TimeUnit.SECONDS.sleep(3);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return cursor;
        }

    }


    public class MySimpleCursorAdapter extends SimpleCursorAdapter{
        private Context mContext;
        public ImageLoaderSmall ImageLoaderSmall;
        public Cursor  cur_t;
        public LayoutInflater inflater=null;
        public MySimpleCursorAdapter(Context context, int layout, Cursor c, String[] from, int[] to, int flags)
        {
            super(context, layout, c, from, to,0);
            mContext = context;
            this.cur_t = c;
            inflater = (LayoutInflater)mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            ImageLoaderSmall=new ImageLoaderSmall(mContext.getApplicationContext());

//            this.cur_t.moveToFirst();
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
//                View view = super.getView(position, convertView, parent);
//                long id=getItemId(position);
//                view.setTag(id);
//                return view;
            View vi=convertView;
            if(convertView==null)
                vi = inflater.inflate(R.layout.mine_list_view, null);
            Cursor cur_i;
            cur_i = this.cur_t;
//            int cnt = cur_i.getCount();
//            final int rowID = cur_t.getInt(cur_t.getColumnIndex("_id"));  //Добавил это
//            this.cur_t.moveToPosition(position);
//            int rowID = this.cur_t.getInt(cur_t.getColumnIndex("img"));  //Добавил это
//            this.cur_t.moveToPosition(position);
//                HashMap<String, Object> data = (HashMap<String, Object>) getItem(position);
                TextView tvCid = (TextView)vi.findViewById(R.id.tvCid);
                String strCid = "9999";
                tvCid.setText(strCid);

            return vi;
        }
    }


}
