package ru.tsarcom.vs.uselfie;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.provider.MediaStore;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.ContextMenu;
import android.view.Display;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;


public class Create2Activity extends Activity {

    String strError;
    JSONObject jObj = null;
    JSONArray jaAccount = null;
    JSONArray jaError = null;
    JSONArray jaCompare = null;
    final String TAG = "logCreate2Activity";
    ImageView ivTop, ivBotton;
    int serverResponseCode = 0;
    String upLoadServerUri = null;

    Intent intent;
    ProgressDialog dialog = null;
    String id_account;
    String id_compare;
    Create2Activity activity;
    ImageView img;
    int cnt = 0;
    LinearLayout view,llBase;
    int DisplayWidth, DisplayHeight;
    AlertDialog alert;
    TextView tvCount;
    AlertDialog.Builder adb;
    String imageLR;
    DB_MineCompare db_MC;
    int id_compare_int;
    String data[] = { "camera", "gallery" };
    Uri selectedImageUriCamera;
    private String selectedImagePath;
     String savedImagePath;
    final int DIALOG_IMAGE = 1;

    private static final int SELECT_PICTURE_GALLERY = 301;
    int REQUEST_CODE_PHOTO = 111;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create2);

        Intent intent = getIntent();
        id_account = intent.getStringExtra("id_account");
        id_compare = intent.getStringExtra("id_compare");
        try {
            id_compare_int = Integer.parseInt(id_compare);
        } catch(NumberFormatException nfe) {
            System.out.println("Could not parse " + id_compare);
        }
        activity = this;

        llBase = (LinearLayout) findViewById(R.id.llBase);
        ivTop = (ImageView) findViewById(R.id.ivTop);
        ivBotton = (ImageView) findViewById(R.id.ivBotton);

        Display display = getWindowManager().getDefaultDisplay();
        DisplayWidth = display.getWidth();
        DisplayHeight = (display.getHeight()-140)/2;
        LinearLayout.LayoutParams parms = new LinearLayout.LayoutParams(DisplayHeight,DisplayHeight);
        ivTop.setLayoutParams(parms);
        ivBotton.setLayoutParams(parms);
        upLoadServerUri = "http://95.78.234.20:81/atest/upload.php?id_account="+id_account+"&id_compare="+id_compare;


        TextView tvCreateID = (TextView)findViewById(R.id.tvCreateID);
        tvCreateID.setText(id_compare);

    }

    public void onclick(View v) {
        switch (v.getId()) {
            case R.id.ivTop:
                img = ivTop;
                imageLR = "Left";
//                REQUEST_CODE_PHOTO = 111;
                showDialog(DIALOG_IMAGE);
                break;
            case R.id.ivBotton:
                img = ivBotton;
                imageLR = "Right";
//                REQUEST_CODE_PHOTO = 222;
                showDialog(DIALOG_IMAGE);
                break;
            case R.id.btnMine:
                // кнопка
                intent = new Intent(this, MineViewsActivity.class);
                intent.putExtra("id_account", id_account);
                startActivity(intent);
                break;
            case R.id.btnOthers:
                // кнопка
                intent = new Intent(this, OthersViewsActivity.class);
                intent.putExtra("id_account", id_account);
                startActivity(intent);
                break;
            case R.id.btnShare:
                // кнопка
                String url0;
                url0 = "http://95.78.234.20:81/atest/jsonShareCompare.php?id_compare="+id_compare;
                new ShareHttpAsyncTask().execute(url0);
                break;
        }
    }

    public void onFromCamera(View v) {

        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
//        intent.putExtra(MediaStore.EXTRA_OUTPUT, generateFileUri(TYPE_PHOTO));
        startActivityForResult(intent, REQUEST_CODE_PHOTO);
        alert.cancel();
    }
    public void onFromGallery(View v) {

//        Toast.makeText(getBaseContext(), "onFromGallery", Toast.LENGTH_LONG).show();

        Intent i = new Intent(
                Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);

        startActivityForResult(i, SELECT_PICTURE_GALLERY);
        alert.cancel();
    }

    protected Dialog onCreateDialog(int id) {
        adb = new AlertDialog.Builder(this);
//        adb.setTitle("Image From");
        // создаем view из dialog.xml
        view = (LinearLayout) getLayoutInflater()
                .inflate(R.layout.dialog_create, null);
        // устанавливаем ее, как содержимое тела диалога
        adb.setView(view);
        // находим TexView для отображения кол-ва
//        tvCount = (TextView) view.findViewById(R.id.tvCount);
        alert = adb.create();
        return alert;
    }


    // обработчик нажатия на пункт списка диалога
    OnClickListener myClickListener = new OnClickListener() {
        public void onClick(DialogInterface dialog, int which) {
            // выводим в лог позицию нажатого элемента
//            Log.d(LOG_TAG, "which = " + which);
//            Toast.makeText(getBaseContext(), "cliiiik" + which, Toast.LENGTH_LONG).show();
        }
    };

    public String getPath(Uri uri) {

        if( uri == null ) {
            return null;
        }

        // this will only work for images selected from gallery
        String[] projection = { MediaStore.Images.Media.DATA };
        Cursor cursor = managedQuery(uri, projection, null, null, null);
        if( cursor != null ){
            int column_index = cursor
                    .getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            cursor.moveToFirst();
            return cursor.getString(column_index);
        }

        return uri.getPath();
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode,  Intent intent) {
//    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == SELECT_PICTURE_GALLERY) {
            if (resultCode == RESULT_OK) {
                if (intent == null) {
                    Log.d(TAG, "Intent is null");
                } else {
                    Uri selectedImageUri = intent.getData();
                    selectedImagePath = getPath(selectedImageUri);
                    savedImagePath = selectedImagePath;
//                    setImageForMIPic();
                    runOnUiThread(new Runnable() {
                        public void run() {
                            Picasso.with(activity)
                                    .load("file:///"+selectedImagePath)
                                    .resize(1024, 0)
                                    .into(img);
                        }
                    });
                    runOnUiThread(new Runnable() {
                        public void run() {
                            Picasso.with(activity)
                                    .load("file:///" + selectedImagePath)
                                    .resize(1024, 0)
                                    .into(target);
//                            Toast.makeText(activity, "savedImagePath = "+savedImagePath,
//                                    Toast.LENGTH_LONG).show();
                        }
                    });
                }
            }
        }

        if (requestCode == REQUEST_CODE_PHOTO) {
            if (resultCode == RESULT_OK) {
                if (intent == null) {
                    Log.d(TAG, "Intent is null");
                } else {
                    selectedImageUriCamera = intent.getData();
                    imageLR = "Left";
                    selectedImagePath = getPath(selectedImageUriCamera);

                    runOnUiThread(new Runnable() {
                        public void run() {
                            Picasso.with(activity)
                                    .load("file:///" + selectedImagePath)
                                    .resize(1024, 0)
                                    .into(img);
                        }
                    });
                    runOnUiThread(new Runnable() {
                        public void run() {
                            Picasso.with(activity)
                            .load("file:///" + selectedImagePath)
                                    .resize(1024, 0)
                                    .into(target);
                        }
                    });

                }
            } else if (resultCode == RESULT_CANCELED) {
                Log.d(TAG, "Canceled");
            }
        }


    }

    private Target target = new Target() {
        @Override
        public void onBitmapLoaded(final Bitmap bitmap, Picasso.LoadedFrom from) {
            new Thread(new Runnable() {
                @Override
                public void run() {

                    savedImagePath = android.os.Environment.getExternalStorageDirectory()
                            + "/saved.jpg";
                    File file = new File(savedImagePath );
                    try {
                        file.createNewFile();
                        FileOutputStream ostream = new FileOutputStream(file);
                        bitmap.compress(Bitmap.CompressFormat.JPEG, 80, ostream);
                        ostream.close();

                        runOnUiThread(new Runnable() {
                            public void run() {
                                setImageForMIPic();

                            }
                        });
                    }
                    catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }).start();
        }

        @Override
        public void onBitmapFailed(Drawable errorDrawable) {

//            Toast.makeText(getBaseContext(), "errorDrawable ", Toast.LENGTH_LONG).show();
        }

        @Override
        public void onPrepareLoad(Drawable placeHolderDrawable) {

//            Toast.makeText(getBaseContext(), "placeHolderDrawable", Toast.LENGTH_LONG).show();
        }
    };
    public void setImageForMIPic(){

//        File file=new File(selectedImagePath);
//
//        Bitmap bm = decodeFile(file);
//        try{
//            ExifInterface exif = new ExifInterface(selectedImagePath);
//            int orientation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, 1);
//            if(1 !=orientation){
//                Matrix matrix = new Matrix();           matrix.postRotate(0);
//                if(6 ==orientation){                    matrix.postRotate(90);                }
//                if(3 ==orientation){                    matrix.postRotate(180);                }
//                if(8 ==orientation){                    matrix.postRotate(270);                }
//                imageOrient =orientation;
//                Bitmap rotatedBitmap = Bitmap.createBitmap(bm, 0, 0, bm.getWidth(), bm.getHeight(), matrix, true);
//                bm = rotatedBitmap;
//            }
////            Toast.makeText(CreateActivity.this, "orientation = "+orientation,
////                    Toast.LENGTH_SHORT).show();
//        }catch(Exception e){
//            Toast.makeText(CreateActivity.this, "не получисось считать orientation",
//                    Toast.LENGTH_SHORT).show();
//        }
//
//        final Bitmap finalBm = bm;
//        new Thread(new Runnable() {
//            public void run() {
//                runOnUiThread(new Runnable() {
//                    public void run() {
//                        img.setImageBitmap(finalBm);
//                    }
//                });
//            }
//        }).start();

//        Picasso.with(activity)
//                .load(selectedImagePath)
////        .placeholder(R.drawable.ic_launcher)
//                .into(ivLeft);
        dialog = ProgressDialog.show(Create2Activity.this, "", "Загрузка файла...", true);
        new Thread(new Runnable() {
            public void run() {
                runOnUiThread(new Runnable() {
                    public void run() {
//                        messageText.setText("Загрузка начата.....");
                    }
                });
                uploadFile(savedImagePath, 0, imageLR);
//                uploadFile(savedImagePath,imageOrient, imageLR);
            }
        }).start();
    }
    public int uploadFile(String sourceFileUri, int imageOrientation, String imageLR) {


        String fileName = sourceFileUri;

        HttpURLConnection conn = null;
        DataOutputStream dos = null;
        String lineEnd = "\r\n";
        String twoHyphens = "--";
        String boundary = "*****";
        int bytesRead, bytesAvailable, bufferSize;
        byte[] buffer;
        int maxBufferSize = 1 * 1024 * 1024;
        File sourceFile = new File(sourceFileUri);

        if (!sourceFile.isFile()) {
            dialog.dismiss();
            runOnUiThread(new Runnable() {
                public void run() {
                    Toast.makeText(Create2Activity.this, "Такого файла для Selfie не существует",
                            Toast.LENGTH_SHORT).show();
                }
            });
            return 0;
        }
        else
        {
            try {
                // open a URL connection to the Servlet
                FileInputStream fileInputStream = new FileInputStream(sourceFile);
                URL url = new URL(upLoadServerUri+"&imageLR="+imageLR+"&imageOrientation="+imageOrientation);
                // Open a HTTP  connection to  the URL
                conn = (HttpURLConnection) url.openConnection();
                conn.setDoInput(true); // Allow Inputs
                conn.setDoOutput(true); // Allow Outputs
                conn.setUseCaches(false); // Don't use a Cached Copy
                conn.setRequestMethod("POST");
                conn.setRequestProperty("Connection", "Keep-Alive");
                conn.setRequestProperty("ENCTYPE", "multipart/form-data");
                conn.setRequestProperty("Content-Type", "multipart/form-data;boundary=" + boundary);
                conn.setRequestProperty("uploaded_file", fileName);

                dos = new DataOutputStream(conn.getOutputStream());

                dos.writeBytes(twoHyphens + boundary + lineEnd);
                dos.writeBytes("Content-Disposition: form-data; name='uploaded_file';filename='"+ fileName + "'" + lineEnd);

                dos.writeBytes(lineEnd);

                // create a buffer of  maximum size
                bytesAvailable = fileInputStream.available();

                bufferSize = Math.min(bytesAvailable, maxBufferSize);
                buffer = new byte[bufferSize];

                // read file and write it into form...
                bytesRead = fileInputStream.read(buffer, 0, bufferSize);

                while (bytesRead > 0) {

                    dos.write(buffer,0,bufferSize);
                    bytesAvailable = fileInputStream.available();
                    bufferSize = Math.min(bytesAvailable, maxBufferSize);
                    bytesRead = fileInputStream.read(buffer, 0, bufferSize);

                }

                // send multipart form data necesssary after file data...
                dos.writeBytes(lineEnd);
                dos.writeBytes(twoHyphens + boundary + twoHyphens + lineEnd);

                // Responses from the server (code and message)
                serverResponseCode = conn.getResponseCode();

                final String serverResponseMessage = conn.getResponseMessage();

                // Convert the InputStream into a string

                InputStream response = conn.getInputStream();
                final String jsonReply;

                BufferedReader reader = new BufferedReader(new InputStreamReader(response));
                StringBuilder sb = new StringBuilder();

                String line = null;
                try {
                    while ((line = reader.readLine()) != null) {
                        sb.append(line + "\n");
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                } finally {
                    try {
                        response.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                jsonReply = sb.toString();

                final String serverContent;
                serverContent = jsonReply;
//                runOnUiThread(new Runnable() {
//                    public void run() {
//                        Toast.makeText(Create2Activity.this,"serverContent-" + serverContent + "-",
//                                Toast.LENGTH_SHORT).show();
//                    }
//                });

                if(serverResponseCode == 200){

                    runOnUiThread(new Runnable() {
                        public void run() {

//                            Toast.makeText(Create2Activity.this,"-" + serverResponseMessage + "-готово",
//                                    Toast.LENGTH_SHORT).show();
                            try {
                                JSONObject jCCObj = null;
                                JSONArray jaCCError = null;
                                JSONArray jaCPhoto = null;
                                String strCCError;
                                jCCObj = new JSONObject(serverContent);
                                // Getting JSON Array
                                jaCCError = jCCObj.getJSONArray("error");
                                JSONObject joCCError = jaCCError.getJSONObject(0);
                                strCCError = joCCError.getString("status");
                                if (strCCError.equals("none")) {
                                    jaCPhoto = jCCObj.getJSONArray("photo");
                                    JSONObject joCCompare = jaCPhoto.getJSONObject(0);
                                    int id_photo = joCCompare.getInt("id");
                                    int Orientation = joCCompare.getInt("Orientation");
                                    String LeftOrRight = joCCompare.getString("LeftOrRight");

                                    if (LeftOrRight.equals("Left")) {
//        // открываем подключение к БД
                                        db_MC = new DB_MineCompare(activity);
                                        db_MC.open();
                                        db_MC.updateLeftMC(id_compare_int, 0,
                                                id_photo,  Orientation, "uploads/"+id_account+"/"+id_compare+"/"+id_photo+"/img.jpg", 0);
                                        db_MC.close();

                                    }
                                    if (LeftOrRight.equals("Right")) {
//        // открываем подключение к БД
                                        db_MC = new DB_MineCompare(activity);
                                        db_MC.open();
                                        db_MC.updateRightMC(id_compare_int, 0,
                                                id_photo,  Orientation, "uploads/"+id_account+"/"+id_compare+"/"+id_photo+"/img.jpg", 0);
                                        db_MC.close();

                                    }

//                                    Toast.makeText(Create2Activity.this,"-" + serverResponseMessage + "- Ваша Selfie загружена",
//                                            Toast.LENGTH_SHORT).show();
                                }else{
                                    Toast.makeText(Create2Activity.this,"-" + serverResponseMessage + "- Ошибки при изменении данных загружки",
                                            Toast.LENGTH_SHORT).show();  }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    });
                }

                //close the streams //
                fileInputStream.close();
                dos.flush();
                dos.close();

            } catch (MalformedURLException ex) {

                dialog.dismiss();
                ex.printStackTrace();

                runOnUiThread(new Runnable() {
                    public void run() {

                        Toast.makeText(Create2Activity.this, "MalformedURLException Exception : check script url.",
                                Toast.LENGTH_SHORT).show();
                    }
                });

                Log.e("Upload file to server", "error: " + ex.getMessage(), ex);
            } catch (Exception e) {

                dialog.dismiss();
                e.printStackTrace();

                runOnUiThread(new Runnable() {
                    public void run() {

                        Toast.makeText(Create2Activity.this, "Got Exception : see logcat ",
                                Toast.LENGTH_SHORT).show();
                    }
                });
                Log.e("Upload file to server Exception", "Exception : "
                        + e.getMessage(), e);
            }
            dialog.dismiss();
            return serverResponseCode;

        } // End else block
    }

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

                    intent = new Intent(activity, MineViewsActivity.class);
                    intent.putExtra("id_account", id_account);
                    startActivity(intent);
                }else{
                    Toast.makeText(getBaseContext(), "!!!!error", Toast.LENGTH_LONG).show();
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }
}
