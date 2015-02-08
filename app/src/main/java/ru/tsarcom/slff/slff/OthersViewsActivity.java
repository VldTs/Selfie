package ru.tsarcom.slff.slff;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
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

import ru.tsarcom.slff.slff.R;

public class OthersViewsActivity extends Activity  implements View.OnClickListener {
    Button btnMine;
    Button btnOthers,btnCreate;
    TextView tvNickname;
    Intent intent;
    String id_account,strError,strCCError;
    JSONObject jObj = null;
    JSONObject jCCObj = null;
    JSONArray jaAccount = null;
    JSONArray jaError = null;
    JSONArray jaCCError = null;
    JSONArray jaCompare = null;
    JSONArray jaPhoto = null;
    JSONArray jaCCompare = null;
    Bitmap bm;
    LinearLayout llMain;
    RadioGroup rgGravity;
    EditText etName;
    Button btnCreateI;
    Button btnClear;
    int wrapContent = LinearLayout.LayoutParams.WRAP_CONTENT;

    final String LOG_TAG = "myLogs";

    ListView lvMain;
    // имена атрибутов для Map
    final String ATTRIBUTE_COMPARE_ID = "txtId";
    final String ATTRIBUTE_NAME_DATE_CRT = "txtDateCrt";
    //    final String ATTRIBUTE_NAME_CHECKED = "checked";
    final String ATTRIBUTE_ID_LEFT = "idLeft";
    final String ATTRIBUTE_ID_RIGHT = "idRight";
    final String ATTRIBUTE_NAME_LEFT = "imLeft";
    final String ATTRIBUTE_NAME_RIGHT = "imRight";
    final String ATTRIBUTE_VOITE_LEFT = "tvVoiteLeft";
    final String ATTRIBUTE_VOITE_RIGHT = "tvVoiteRight";
    final String ATTRIBUTE_NICKNAME = "tvNickname";
    final String ATTRIBUTE_A_ID = "tvA_id";
    final String ATTRIBUTE_NAME_PB = "pb";
    final String ATTRIBUTE_ORNT_LEFT = "OrientationLeft";
    final String ATTRIBUTE_ORNT_RIGHT = "OrientationRight";
    ImageView imagetest;
    ListView lvSimple;
    int loader;
    OthersViewsActivity activity;
    Class<CreateActivity> createActivityClass;
    final String ID_ACCOUNT = "ID_ACCOUNT";
    SharedPreferences sPref;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.others_views);


        Intent intent = getIntent();
        id_account = intent.getStringExtra("id_account");
        activity = this;

//        Toast.makeText(getBaseContext(), "id_account "+id_account, Toast.LENGTH_LONG).show();
        if(isConnected()){
            String id_account_t = loadText();
//            Toast.makeText(getBaseContext(), "id_account_t "+id_account_t, Toast.LENGTH_LONG).show();
            if (id_account_t.equals("")){
                saveText("-1");
            }else{
                id_account = id_account_t;
                String url0;
                url0 = "http://95.78.234.20:81/atest/jsonOthers.php?id_account="+id_account;
                new HttpAsyncTask().execute(url0);
            }
        }else{
                Toast.makeText(getBaseContext(), "! Нет интернета :(", Toast.LENGTH_LONG).show();
        }
        btnMine = (Button) findViewById(R.id.btnMine);
        btnMine.setOnClickListener(this);
        btnCreate = (Button) findViewById(R.id.btnCreate);
        btnCreate.setOnClickListener(this);
        // находим список
        lvSimple = (ListView) findViewById(R.id.lvSimple);

        lvSimple.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {

                TextView tvCid = (TextView) view.findViewById(R.id.tvCid);
                String strCid = tvCid.getText().toString();

//                Toast.makeText(getBaseContext(), "itemClick: position = " + position + ", id = "
//                        + id+" "+strCid, Toast.LENGTH_LONG).show();

                Intent intent = new Intent(activity, ViewActivity.class);
                intent.putExtra("id_account", id_account);
                intent.putExtra("id_compare", strCid);
                startActivity(intent);
            }
        });
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

    void saveText(String id_account_t) {
//        sPref = getPreferences(MODE_PRIVATE);
        sPref = getSharedPreferences("MyPref",MODE_PRIVATE);
        SharedPreferences.Editor ed = sPref.edit();
        ed.putString(ID_ACCOUNT, id_account_t);
        ed.commit();
//        Toast.makeText(this, "Text saved", Toast.LENGTH_SHORT).show();
    }

    String  loadText() {
//        sPref = getPreferences(MODE_PRIVATE);
        sPref = getSharedPreferences("MyPref",MODE_PRIVATE);
        String savedText = sPref.getString(ID_ACCOUNT, "");
//        Toast.makeText(this, "ID_ACCOUNT loaded "+savedText, Toast.LENGTH_SHORT).show();
        return  savedText;
    }
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnMine:
                // кнопка
                intent = new Intent(this, MineViewsActivity.class);
                intent.putExtra("id_account", id_account);
                startActivity(intent);
                break;
            case R.id.btnCreate:
                // кнопка

                createActivityClass = CreateActivity.class;
                String urlCC;
                urlCC = "http://95.78.234.20:81/atest/jsonCreateCompare.php?id_account="+id_account;
                new CreateCompareHttpAsyncTask().execute(urlCC);
                break;
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

//                    Toast.makeText(getBaseContext(), "id_compare "+id_compare, Toast.LENGTH_LONG).show();
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
// --- load all compares

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
//                    jaAccount = jObj.getJSONArray("account");
//                    JSONObject joAccount = jaAccount.getJSONObject(0);
//                    String strNickname = joAccount.getString("nickname");
////                    tvNickname.setText(strNickname);
//
//                    activity.setTitle("Selfie - " + strNickname);
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

                        // Image url
//                        String image_url = "http://api.androidhive.info/images/sample.jpg";

                        // ImageLoader class instance
//                        ImageLoader imgLoader = new ImageLoader(getApplicationContext());

                        ArrayList<Map<String, Object>> data = new ArrayList<Map<String, Object>>(
                                iCompareLen);
                        Map<String, Object> m;
                        for (int iC = 0; iC < iCompareLen; iC = iC + 1) {
                            JSONObject joCompare = jaCompare.getJSONObject(iC);
                            String id = joCompare.getString("id");
                            String a_id = joCompare.getString("a_id");
//                            String id = joCompare.getString("nickname");
                            String date_crt = joCompare.getString("date_crt");
                            String nickname = joCompare.getString("nickname");

                            jaPhoto = joCompare.getJSONArray("photo");
                            JSONObject joPhotoLeft = jaPhoto.getJSONObject(0);
                            String idLeft = joPhotoLeft.getString("id");
                            String pathLeft = joPhotoLeft.getString("path");
                            String all_voiteLeft = joPhotoLeft.getString("all_voite");
                            String orientationLeft =  joPhotoLeft.getString("Orientation");
                            //                        Toast.makeText(getBaseContext(), "pathLeft "+pathLeft, Toast.LENGTH_LONG).show();
                            JSONObject joPhotoRight = jaPhoto.getJSONObject(1);
                            String idRight = joPhotoRight.getString("id");
                            String pathRight = joPhotoRight.getString("path");
                            String all_voiteRight = joPhotoRight.getString("all_voite");
                            String orientationRight = joPhotoRight.getString("Orientation");
                            //                        Toast.makeText(getBaseContext(), "pathRight "+pathRight, Toast.LENGTH_LONG).show();

                            m = new HashMap<String, Object>();
                            m.put(ATTRIBUTE_COMPARE_ID, id);
                            m.put(ATTRIBUTE_A_ID, a_id);
                            m.put(ATTRIBUTE_NICKNAME, nickname);
                            m.put(ATTRIBUTE_NAME_DATE_CRT, date_crt);

                            m.put(ATTRIBUTE_ID_LEFT, idLeft);
                            m.put(ATTRIBUTE_ID_RIGHT, idRight);
//                            ImageLoader imgLoader2 = new ImageLoader(getApplicationContext());
//                            m.put(ATTRIBUTE_NAME_LEFT, imgLoader.getBitmap(image_url));
                            m.put(ATTRIBUTE_VOITE_LEFT, all_voiteLeft);
                            m.put(ATTRIBUTE_VOITE_RIGHT, all_voiteRight);
                            m.put(ATTRIBUTE_NAME_LEFT, pathLeft);
                            //                        m.put(ATTRIBUTE_NAME_LEFT, bm);
                            m.put(ATTRIBUTE_NAME_RIGHT, pathRight);
                            m.put(ATTRIBUTE_NAME_PB, 2);
                            m.put(ATTRIBUTE_ORNT_LEFT, orientationLeft);
                            m.put(ATTRIBUTE_ORNT_RIGHT, orientationRight);
                            data.add(m);
                        }

                        // массив имен атрибутов, из которых будут читаться данные
                        String[] from = {ATTRIBUTE_COMPARE_ID,ATTRIBUTE_NICKNAME,
                                ATTRIBUTE_NAME_DATE_CRT,
                                //                ATTRIBUTE_NAME_CHECKED,
                                ATTRIBUTE_NAME_LEFT, ATTRIBUTE_NAME_RIGHT,ATTRIBUTE_VOITE_LEFT,ATTRIBUTE_VOITE_RIGHT,
                                ATTRIBUTE_NAME_PB, ATTRIBUTE_ORNT_LEFT,ATTRIBUTE_ORNT_RIGHT};
                        // массив ID View-компонентов, в которые будут вставлять данные
                        int[] to = {R.id.tvCid,R.id.tvNickname, R.id.tvCdate, R.id.ivLeft, R.id.ivRight, R.id.tvVoiteLeft, R.id.tvVoiteRight, R.id.pbLoad,};

                        // создаем адаптер
                        SimpleAdapter sAdapter = new MySimpleAdapter(activity, data, R.layout.others_list_view,
                                from, to);

                        // Указываем адаптеру свой биндер
                        sAdapter.setViewBinder(new MyViewBinder());
                        // к списоку присваиваем ему адаптер
                        lvSimple.setAdapter(sAdapter);
                        //                    JSONObject joCompare = jaCompare.getJSONObject(0);
                        //                    String date_crt = joCompare.getString("date_crt");
                    }

                }else{
                    Toast.makeText(getBaseContext(), "простите, не получается прочитать Selfie :(", Toast.LENGTH_LONG).show();
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }
            if (strError.equals("none")) {
                Toast.makeText(getBaseContext(), "Selfie загружены", Toast.LENGTH_LONG).show();
            }else{
                Toast.makeText(getBaseContext(), "простите, не могу загрузить список Selfie :(", Toast.LENGTH_LONG).show();
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
                    vi = inflater.inflate(R.layout.others_list_view, null);

                HashMap<String, Object> data = (HashMap<String, Object>) getItem(position);
                TextView tvCid = (TextView)vi.findViewById(R.id.tvCid);
                TextView tvNickname = (TextView)vi.findViewById(R.id.tvNickname);
                TextView tvCdate = (TextView)vi.findViewById(R.id.tvCdate);
                TextView tvVoiteLeft = (TextView)vi.findViewById(R.id.tvVoiteLeft);
                TextView tvVoiteRight = (TextView)vi.findViewById(R.id.tvVoiteRight);
                ProgressBar pbLoad = (ProgressBar)vi.findViewById(R.id.pbLoad);
                ImageView ivLeft=(ImageView)vi.findViewById(R.id.ivLeft);
                ImageView ivRight=(ImageView)vi.findViewById(R.id.ivRight);
                String strCid = (String) data.get(ATTRIBUTE_COMPARE_ID);
                String strNickname = (String) data.get(ATTRIBUTE_NICKNAME);
                String a_id = (String) data.get(ATTRIBUTE_A_ID);
                String strCdate = (String) data.get(ATTRIBUTE_NAME_DATE_CRT);
                String url_left = (String) data.get(ATTRIBUTE_NAME_LEFT);
                String url_right = (String) data.get(ATTRIBUTE_NAME_RIGHT);
                String idLeft = (String) data.get(ATTRIBUTE_ID_LEFT);
                String idRight = (String) data.get(ATTRIBUTE_ID_RIGHT);
                String all_voiteLeft = (String) data.get(ATTRIBUTE_VOITE_LEFT);
                String all_voiteRight = (String) data.get(ATTRIBUTE_VOITE_RIGHT);
                String imgOrientation_left = (String) data.get(ATTRIBUTE_ORNT_LEFT);
                String imgOrientation_right = (String) data.get(ATTRIBUTE_ORNT_RIGHT);

                tvCid.setText(strCid);
                tvNickname.setText(strNickname);
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



//            url_left = "uploads/3/113/81/img.png";
                url_left = "uploads/"+a_id+"/"+strCid+"/"+idLeft+"/img.png";
//                url_left = "http://95.78.234.20:81/atest/"+path_left;

                if (a_id.isEmpty() || a_id.equals("0")  || strCid.isEmpty() || strCid.equals("0") ||
                        idLeft.isEmpty() || idLeft.equals("0") ){
                    url_left = "empty.png";
                }
                url_left = "http://95.78.234.20:81/atest/"+url_left;

//                Toast.makeText(getBaseContext(), "url_left = "+url_left,
//                        Toast.LENGTH_SHORT).show();
//            String url_right = "uploads/3/167/157/img.png";
                url_right = "uploads/"+a_id+"/"+strCid+"/"+idRight+"/img.jpg";

                if (a_id.isEmpty() || a_id.equals("0") || strCid.isEmpty() || strCid.equals("0") ||
                        idRight.isEmpty() || idRight.equals("0") ){
                    url_right = "empty.png";
                }
                url_right = "http://95.78.234.20:81/atest/"+url_right;

//            Toast.makeText(getBaseContext(), "url_right = "+url_right,
//                    Toast.LENGTH_SHORT).show();
//                url_right = "http://95.78.234.20:81/atest/"+path_right;
//                int img = R.drawable.abc_ic_clear_normal;
                int img = R.drawable.abc_ic_clear_mtrl_alpha;
                ImageLoaderSmall.DisplayImage(url_left, img, ivLeft,imgOrientation_left_i);
                ImageLoaderSmall.DisplayImage(url_right, img, ivRight,imgOrientation_right_i);
                return vi;
            }
        }

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
}
