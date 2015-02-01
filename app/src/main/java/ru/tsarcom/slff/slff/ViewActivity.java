package ru.tsarcom.slff.slff;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import ru.tsarcom.slff.slff.R;

public class ViewActivity extends Activity  implements View.OnClickListener {
    Button btnOthers,btnMine,btnCreate,btnVoiteLeft,btnVoiteRight, btnShare;
    Intent intent;
    String id_account;
    String id_compare,
            id_photo_left,
            id_photo_right;
    String compare_status;
    ImageView imagetest,ivLeft,ivRight;

    String strError,strCCError;
    JSONObject jObj = null;
    JSONObject jCCObj = null;
    JSONArray jaAccount = null;
    JSONArray jaError = null;
    JSONArray jaCCError = null;
    JSONArray jaCompare = null;
    JSONArray jaPhoto = null;
    JSONArray jaCCompare = null;
    ViewActivity activity;
    Class<CreateActivity> createActivityClass;
    TextView tvVoiteLeft,tvVoiteRight;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.view);
        activity = this;
        btnOthers = (Button) findViewById(R.id.btnOthers);
        btnOthers.setOnClickListener(this);
        btnCreate = (Button) findViewById(R.id.btnCreate);
        btnCreate.setOnClickListener(this);
        btnMine = (Button) findViewById(R.id.btnMine);
        btnMine.setOnClickListener(this);

        btnVoiteLeft = (Button) findViewById(R.id.btnVoiteLeft);
        btnVoiteLeft.setOnClickListener(this);
        btnVoiteRight = (Button) findViewById(R.id.btnVoiteRight);
        btnVoiteRight.setOnClickListener(this);
        btnShare = (Button) findViewById(R.id.btnShare);
        btnShare.setOnClickListener(this);

        tvVoiteLeft = (TextView) findViewById(R.id.tvVoiteLeft);
        tvVoiteRight = (TextView) findViewById(R.id.tvVoiteRight);
        Intent intent = getIntent();
        id_account = intent.getStringExtra("id_account");
        id_compare = intent.getStringExtra("id_compare");

//        Toast.makeText(getBaseContext(), "id_account = "+id_account+ " id_compare = "+id_compare, Toast.LENGTH_LONG).show();

        ivLeft = (ImageView) findViewById(R.id.ivLeft);
        ivRight = (ImageView) findViewById(R.id.ivRight);
        String url0;
        url0 = "http://95.78.234.20:81/atest/jsonView.php?id_compare="+id_compare+"&id_account="+id_account;
        //   Toast.makeText(getBaseContext(), "url0 "+url0, Toast.LENGTH_LONG).show();
        new ViewHttpAsyncTask().execute(url0);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnMine:
                // кнопка
                intent = new Intent(this, MineViewsActivity.class);
                intent.putExtra("id_account", id_account);
//                intent.putExtra("id_compare", id_compare);
                startActivity(intent);
                break;
            case R.id.btnOthers:
                // кнопка
                intent = new Intent(this, OthersViewsActivity.class);
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
            case R.id.btnVoiteLeft:
                // кнопка
                String urll;
                urll = "http://95.78.234.20:81/atest/jsonVoite.php?id_compare="+id_compare+"&id_account="+id_account+"&id_photo_plus="+id_photo_left+"&id_photo_min="+id_photo_right+"&ph=l";

//                Toast.makeText(getBaseContext(), "urll "+urll, Toast.LENGTH_LONG).show();
                new VoiteHttpAsyncTask().execute(urll);
                break;
            case R.id.btnVoiteRight:
                // кнопка
                String urlr;
                urlr = "http://95.78.234.20:81/atest/jsonVoite.php?id_compare="+id_compare+"&id_account="+id_account+"&id_photo_plus="+id_photo_right+"&id_photo_min="+id_photo_left+"&ph=r";

//                Toast.makeText(getBaseContext(), "urlr "+urlr, Toast.LENGTH_LONG).show();
                new VoiteHttpAsyncTask().execute(urlr);
                break;
            case R.id.btnShare:
                // кнопка
                String url0;
                if (compare_status.equals("0")) {
                    // Отменить Публикацию
                    url0 = "http://95.78.234.20:81/atest/jsonNoShareCompare.php?id_compare="+id_compare;
                }else{
                    // опубликавать
                    url0 = "http://95.78.234.20:81/atest/jsonShareCompare.php?id_compare="+id_compare;
                }
                new ShareHttpAsyncTask().execute(url0);
                break;
        }
    }

//    public static String GET(String url){
//        InputStream inputStream = null;
//        String result = "";
//        try {
//            // create HttpClient
//            HttpClient httpclient = new DefaultHttpClient();
//            // make GET request to the given URL
//            HttpResponse httpResponse = httpclient.execute(new HttpGet(url));
//            // receive response as inputStream
//            inputStream = httpResponse.getEntity().getContent();
//            // convert inputstream to string
//            if(inputStream != null)
//                result = convertInputStreamToString(inputStream);
//            else
//                result = "Did not work!";
//        } catch (Exception e) {
//            Log.d("InputStream", e.getLocalizedMessage());
//        }
//        return result;
//    }
//
//    private static String convertInputStreamToString(InputStream inputStream) throws IOException {
//        BufferedReader bufferedReader = new BufferedReader( new InputStreamReader(inputStream));
//        String line = "";
//        String result = "";
//        while((line = bufferedReader.readLine()) != null)
//            result += line;
//        inputStream.close();
//        return result;
//
//    }
    //----
    private class ViewHttpAsyncTask extends AsyncTask<String, Void, String> {
        public String JsonString_t;
    public ImageLoader ImageLoaderL;
    public ImageLoader ImageLoaderR;
        @Override
        protected String doInBackground(String... urls) {
            GetFromURL oGetURL = new GetFromURL();
            return oGetURL.GET(urls[0]);
        }
        // onPostExecute displays the results of the AsyncTask.
        @Override
        protected void onPostExecute(String result) {
            //   Toast.makeText(getBaseContext(), "Received!", Toast.LENGTH_LONG).show();

            ImageLoaderL=new ImageLoader(getApplicationContext());
            ImageLoaderR=new ImageLoader(getApplicationContext());
            try {

                jObj = new JSONObject(result);
                // Getting JSON Array
                jaError = jObj.getJSONArray("error");
                JSONObject joCCError = jaError.getJSONObject(0);
                strError = joCCError.getString("status");
//                Toast.makeText(getBaseContext(), "Received! "+strError, Toast.LENGTH_LONG).show();

                if (strError.equals("none")) {
                    jaCompare = jObj.getJSONArray("compare");
                    JSONObject joCompare = jaCompare.getJSONObject(0);
                    String id_compare = joCompare.getString("id");
                    String date_crt = joCompare.getString("date_crt");
                    compare_status = joCompare.getString("status");

                    jaPhoto = joCompare.getJSONArray("photo");
                    JSONObject joPhotoLeft = jaPhoto.getJSONObject(0);
                    id_photo_left = joPhotoLeft.getString("id");
                    String pathLeft = joPhotoLeft.getString("path");
                    String voiteLeft = joPhotoLeft.getString("all_voite");
                    String chvoiteLeft = joPhotoLeft.getString("ch_voite");
                    String orientationLeft =  joPhotoLeft.getString("Orientation");
//                        Toast.makeText(getBaseContext(), "pathLeft "+pathLeft, Toast.LENGTH_LONG).show();
                    JSONObject joPhotoRight = jaPhoto.getJSONObject(1);
                    id_photo_right = joPhotoRight.getString("id");
                    String pathRight = joPhotoRight.getString("path");
                    String voiteRight = joPhotoRight.getString("all_voite");
                    String chvoiteRight = joPhotoRight.getString("ch_voite");
                    String orientationRight = joPhotoRight.getString("Orientation");
                    int ivoiteLeft = 0;
                    try {
                        ivoiteLeft = Integer.parseInt(voiteLeft);
                    } catch(NumberFormatException nfe) {
                        System.out.println("Could not parse " + voiteLeft);
                    }
                    int ichvoiteLeft = -1;
                    try {
                        ichvoiteLeft = Integer.parseInt(chvoiteLeft);
                    } catch(NumberFormatException nfe) {
                        System.out.println("Could not parse " + chvoiteLeft);
                    }
                    int ivoiteRight = 0;
                    try {
                        ivoiteRight = Integer.parseInt(voiteRight);
                    } catch(NumberFormatException nfe) {
                        System.out.println("Could not parse " + voiteRight);
                    }
                    int ichvoiteRight = -1;
                    try {
                        ichvoiteRight = Integer.parseInt(chvoiteRight);
                    } catch(NumberFormatException nfe) {
                        System.out.println("Could not parse " + chvoiteRight);
                    }
                    int iall_voite = ivoiteLeft+ ivoiteRight;
                    String all_voite = Integer.toString(iall_voite);

//                    ImageLoader imgLoaderLeft = new ImageLoader(getApplicationContext());
                    int img = R.drawable.abc_ic_clear_mtrl_alpha;
                    ImageLoaderL.DisplayImage("http://95.78.234.20:81/atest/"+pathLeft, img, ivLeft,0);
                    ImageLoaderR.DisplayImage("http://95.78.234.20:81/atest/"+pathRight, img, ivRight,0);
////                    if (pathLeft.equals("empty")) {
////                        pathLeft = "empty.png";
////                    }
//
////            Toast.makeText(getBaseContext(), "imgOrientation_left = "+orientationLeft,
////                    Toast.LENGTH_SHORT).show();
////            Toast.makeText(getBaseContext(), "imgOrientation_right = "+orientationRight,
////                    Toast.LENGTH_SHORT).show();
//                    int imgOrientation_left_i = 1;
//                    try {
//                        imgOrientation_left_i = Integer.parseInt(orientationLeft);
//                    } catch(NumberFormatException nfe) {
//                        System.out.println("Could not parse " + orientationLeft);
//                    }
//                    int imgOrientation_right_i = 1;
//                    try {
//                        imgOrientation_right_i = Integer.parseInt(orientationRight);
//                    } catch(NumberFormatException nfe) {
//                        System.out.println("Could not parse " + orientationRight);
//                    }
//                    Bitmap rotatedBitmap;
//
////                Toast.makeText(getBaseContext(), "Received! "+strError, Toast.LENGTH_LONG).show();
////            Toast.makeText(ViewActivity.this, "pathLeft = "+pathLeft,
////                    Toast.LENGTH_SHORT).show();
//                    Bitmap bitmap = imgLoaderLeft.getBitmap("http://95.78.234.20:81/atest/" + pathLeft);
//                    try {
//                        if(1 !=imgOrientation_left_i){
//                            Matrix matrix = new Matrix();
//                            matrix.postRotate(0);
//                            if(6 ==imgOrientation_left_i){
//                                matrix.postRotate(90);
//                            }
//                            if(3 ==imgOrientation_left_i){
//                                matrix.postRotate(180);
//                            }
//                            if(8 ==imgOrientation_left_i){
//                                matrix.postRotate(270);
//                            }
//                            rotatedBitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
//                            bitmap = rotatedBitmap;
//                        }
//                    }catch (Exception e){
//
////            Toast.makeText(CreateActivity.this, "orientation = "+orientation,
////                    Toast.LENGTH_SHORT).show();
//                    }
//                    ivLeft.setImageBitmap(bitmap);
//
//                    ImageLoader imgLoaderRight = new ImageLoader(getApplicationContext());
////                    if (pathRight.equals("empty")) {
////                        pathRight = "empty.png";
////                    }

//                    bitmap = imgLoaderRight.getBitmap("http://95.78.234.20:81/atest/" + pathRight);
//                    try {
//                        if(1 !=imgOrientation_right_i){
//                            Matrix matrixR = new Matrix();
//                            matrixR.postRotate(0);
//                            if(6 ==imgOrientation_right_i){
//                                matrixR.postRotate(90);
//                            }
//                            if(3 ==imgOrientation_right_i){
//                                matrixR.postRotate(180);
//                            }
//                            if(8 ==imgOrientation_right_i){
//                                matrixR.postRotate(270);
//                            }
//                            rotatedBitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrixR, true);
//                            bitmap = rotatedBitmap;
//                        }
//                    }catch (Exception e){
//
////            Toast.makeText(CreateActivity.this, "orientation = "+orientation,
////                    Toast.LENGTH_SHORT).show();
//                    }
//                    ivRight.setImageBitmap(bitmap);
//
////                    TextView tvCid = (TextView) view.findViewById(R.id.btnVoiteLeft);
////                    String strCid = btnVoiteLeft.getText().toString();
////                    tvVoiteLeft
////                    btnVoiteLeft.setText("Я за эту - ("+voiteLeft+" из "+all_voite+")");
////                    btnVoiteRight.setText("А Я за эту - ("+voiteRight+" из "+all_voite+")");
                    tvVoiteLeft.setText(voiteLeft+" из "+all_voite);
                    tvVoiteRight.setText(voiteRight+" из "+all_voite);

                    if (compare_status.equals("0")) {
                        btnShare.setText("Отменить Публикацию");
                    }else{
                        btnShare.setText("Публикавать");
                    }
                }else{
                    Toast.makeText(getBaseContext(), "простите, не получается прочитать ваши Selfie :(", Toast.LENGTH_LONG).show();
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }

            // Toast.makeText(getBaseContext(), "2222222222 status_user "+status_user, Toast.LENGTH_LONG).show();
            if (strError.equals("none")) {
              //  Toast.makeText(getBaseContext(), "CSelfie загружен", Toast.LENGTH_LONG).show();
            }else{
                Toast.makeText(getBaseContext(), "простите, CSelfie не загружен :(", Toast.LENGTH_LONG).show();
            }
        }

    }
    //---- voite
    private class VoiteHttpAsyncTask extends AsyncTask<String, Void, String> {
        public String JsonString_t;
        @Override
        protected String doInBackground(String... urls) {
            GetFromURL oGetURL = new GetFromURL();
            return oGetURL.GET(urls[0]);
        }
        // onPostExecute displays the results of the AsyncTask.
        @Override
        protected void onPostExecute(String result) {
               Toast.makeText(getBaseContext(), "Received! voite", Toast.LENGTH_LONG).show();
            try {
                jObj = new JSONObject(result);
                // Getting JSON Array
                jaError = jObj.getJSONArray("error");
                JSONObject joCCError = jaError.getJSONObject(0);
                strError = joCCError.getString("status");
                if (strError.equals("none")) {
//                    jaCompare = jObj.getJSONArray("compare");
//                    JSONObject joCompare = jaCompare.getJSONObject(0);
//                    String id_compare = joCompare.getString("id");

//                    intent = new Intent(activity, createActivityClass);
//                    intent.putExtra("id_compare", id_compare);
//                    intent.putExtra("id_account", id_account);
//                    startActivity(intent);

                }else{
                    Toast.makeText(getBaseContext(), "!!!!error json voite", Toast.LENGTH_LONG).show();
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            // Toast.makeText(getBaseContext(), "2222222222 status_user "+status_user, Toast.LENGTH_LONG).show();
            if (strError.equals("none")) {
                Toast.makeText(getBaseContext(), "+++Good voite", Toast.LENGTH_LONG).show();
            }else{
                Toast.makeText(getBaseContext(), "!!!!error voite", Toast.LENGTH_LONG).show();
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

// share
    private class ShareHttpAsyncTask extends AsyncTask<String, Void, String> {
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
                if (strError.equals("share")) {
                    Toast.makeText(getBaseContext(), "Опубликовано", Toast.LENGTH_LONG).show();
                }else{
                    Toast.makeText(getBaseContext(), "!!!!error", Toast.LENGTH_LONG).show();
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }
}
