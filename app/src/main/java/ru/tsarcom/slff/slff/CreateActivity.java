package ru.tsarcom.slff.slff;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import ru.tsarcom.slff.slff.R;

public class CreateActivity extends Activity  implements View.OnClickListener {
    Button btnOthers,btnMine, btnShare;
    Intent intent;
    String id_account;
    String id_compare;
    String strError;
    JSONObject jObj = null;
    JSONArray jaAccount = null;
    JSONArray jaError = null;
    JSONArray jaCompare = null;
    private static final int SELECT_PICTURE = 1;
    private String selectedImagePath;

    TextView messageText;
    Button uploadButton;
    int serverResponseCode = 0;
    ProgressDialog dialog = null;

    String upLoadServerUri = null;
    String dtnow;
    /**********  File Path *************/
    final String uploadFilePath = "/mnt/sdcard/";
    final String uploadFileName = "upldtst.png";
    int imageOrient;
    String imageLR;
    ImageView img,ivLeft,ivRight;
    ImageButton imageButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.create);

        Intent intent = getIntent();
        id_account = intent.getStringExtra("id_account");
        id_compare = intent.getStringExtra("id_compare");

        TextView tvCreateID = (TextView)findViewById(R.id.textView);
        tvCreateID.setText(tvCreateID.getText()+"  # "+id_compare);
//      textView

        btnOthers = (Button) findViewById(R.id.btnOthers);
        btnOthers.setOnClickListener(this);
        btnMine = (Button) findViewById(R.id.btnMine);
        btnMine.setOnClickListener(this);
        btnShare = (Button) findViewById(R.id.btnShare);
        btnShare.setOnClickListener(this);

        ivLeft= (ImageView)findViewById(R.id.ivLeft);
        ivRight= (ImageView)findViewById(R.id.ivRight);


        messageText  = (TextView)findViewById(R.id.messageText);
        upLoadServerUri = "http://95.78.234.20:81/atest/upload.php?id_account="+id_account+"&id_compare="+id_compare;
        // load from galary for left image
        ((ImageView) findViewById(R.id.ivLeft))
                .setOnClickListener(new View.OnClickListener() {

                    public void onClick(View arg0) {

                        Intent intent = new Intent();
                        intent.setType("image/*");
                        intent.setAction(Intent.ACTION_GET_CONTENT);
                        startActivityForResult(Intent.createChooser(intent,
                                "Select Picture"), SELECT_PICTURE);
                        img = ivLeft;
                        imageLR = "Left";

                    }
                });
        // load from galary for right image
        ((ImageView) findViewById(R.id.ivRight))
                .setOnClickListener(new View.OnClickListener() {

                    public void onClick(View arg0) {
                        Intent intent = new Intent();
                        intent.setType("image/*");
                        intent.setAction(Intent.ACTION_GET_CONTENT);
                        startActivityForResult(Intent.createChooser(intent,
                                "Select Picture"), SELECT_PICTURE);
                        img = ivRight;
                        imageLR = "Right";
                    }
                });
    }
    //decodes image and scales it to reduce memory consumption
    private Bitmap decodeFile(File f){
        try {
            //Decode image size
            BitmapFactory.Options o = new BitmapFactory.Options();
            o.inJustDecodeBounds = true;
            BitmapFactory.decodeStream(new FileInputStream(f),null,o);

            //The new size we want to scale to
            final int REQUIRED_SIZE=340;

            //Find the correct scale value. It should be the power of 2.
            int scale=1;
            while(o.outWidth/scale/2>=REQUIRED_SIZE && o.outHeight/scale/2>=REQUIRED_SIZE)
                scale*=2;

            //Decode with inSampleSize
            BitmapFactory.Options o2 = new BitmapFactory.Options();
            o2.inSampleSize=scale;
            return BitmapFactory.decodeStream(new FileInputStream(f), null, o2);
        } catch (FileNotFoundException e) {}
        return null;
    }
    public void setImageForMI(){

//        Bitmap bm0 = BitmapFactory.decodeFile(selectedImagePath);

//        Bitmap bm = decodeFile(bm0);
        // сжимание
//        ByteArrayOutputStream stream = new ByteArrayOutputStream();
//        bm.compress(Bitmap.CompressFormat.JPEG, 20, stream);

        File file=new File(selectedImagePath);

        Bitmap bm = decodeFile(file);
        try{
            ExifInterface exif = new ExifInterface(selectedImagePath);
            int orientation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, 1);
            if(1 !=orientation){
                Matrix matrix = new Matrix();
                matrix.postRotate(0);
                if(6 ==orientation){
                    matrix.postRotate(90);
                }
                if(3 ==orientation){
                    matrix.postRotate(180);
                }
                if(8 ==orientation){
                    matrix.postRotate(270);
                }
                imageOrient =orientation;
                Bitmap rotatedBitmap = Bitmap.createBitmap(bm, 0, 0, bm.getWidth(), bm.getHeight(), matrix, true);
                bm = rotatedBitmap;
            }
//            Toast.makeText(CreateActivity.this, "orientation = "+orientation,
//                    Toast.LENGTH_SHORT).show();
        }catch(Exception e){
            Toast.makeText(CreateActivity.this, "не получисось считать orientation",
                    Toast.LENGTH_SHORT).show();
        }
        img.setImageBitmap(bm);
        dialog = ProgressDialog.show(CreateActivity.this, "", "Загрузка файла...", true);
        new Thread(new Runnable() {
            public void run() {
                runOnUiThread(new Runnable() {
                    public void run() {
                        messageText.setText("Загрузка начата.....");
                    }
                });
                uploadFile(selectedImagePath,imageOrient, imageLR);
            }
        }).start();
    }
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            if (requestCode == SELECT_PICTURE) {
                Uri selectedImageUri = data.getData();
                selectedImagePath = getPath(selectedImageUri);
                setImageForMI();
            }
        }
    }
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

//            Log.e("uploadFile", "Source File not exist :"
//                    + uploadFilePath + "" + uploadFileName);

            runOnUiThread(new Runnable() {
                public void run() {
                    Toast.makeText(CreateActivity.this, "Такого файла для Selfie не существует",
                            Toast.LENGTH_SHORT).show();
//                    messageText.setText("Source File not exist :"
//                            +uploadFilePath + "" + uploadFileName);
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

                    dos.write(buffer, 0, bufferSize);
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
                String jsonReply;
//                String convertStreamToString(InputStream is) {

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

                final String serverJSON;
                serverJSON = jsonReply;
                runOnUiThread(new Runnable() {
                    public void run() {
                        Toast.makeText(CreateActivity.this,"serverJSON-" + serverJSON + "-",
                                Toast.LENGTH_SHORT).show();
                            messageText.setText(serverJSON);
                    }
                });

                Log.i("uploadFile", "HTTP Response is : "
                        + serverResponseMessage + ": " + serverResponseCode);

                if(serverResponseCode == 200){

                    runOnUiThread(new Runnable() {
                        public void run() {

//                            String msg = "File Upload Completed.\n\n See uploaded file here : \n\n"
//                                    +" http://www.androidexample.com/media/uploads/"
//                                    +uploadFileName;
//
//                            messageText.setText(msg);
                            Toast.makeText(CreateActivity.this,"-" + serverResponseMessage + "- Ваша Selfie загружена",
                                    Toast.LENGTH_SHORT).show();
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
//                        messageText.setText("MalformedURLException Exception : check script url.");
                        Toast.makeText(CreateActivity.this, "MalformedURLException Exception : check script url.",
                                Toast.LENGTH_SHORT).show();
                    }
                });

                Log.e("Upload file to server", "error: " + ex.getMessage(), ex);
            } catch (Exception e) {

                dialog.dismiss();
                e.printStackTrace();

                runOnUiThread(new Runnable() {
                    public void run() {
//                        messageText.setText("Got Exception : see logcat ");
                        Toast.makeText(CreateActivity.this, "Got Exception : see logcat ",
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
    // ---------------
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
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
//                intent.putExtra("fname", etEmail.getText().toString());
//                intent.putExtra("lname", etPass.getText().toString());
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

    // json



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
