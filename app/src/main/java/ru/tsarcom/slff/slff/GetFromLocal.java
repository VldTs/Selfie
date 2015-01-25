package ru.tsarcom.slff.slff;

import android.content.SharedPreferences;
import android.util.Log;
import android.widget.Toast;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * Created by USRSLM on 29.11.14.
 */
public class GetFromLocal {

    final String ID_ACCOUNT = "ID_ACCOUNT";
    SharedPreferences sPref;
    // constructor
    public GetFromLocal() {
    }
//    void saveText() {
//        sPref = getPreferences(MODE_PRIVATE);
//        SharedPreferences.Editor ed = sPref.edit();
//        ed.putString(ID_ACCOUNT, etText.getText().toString());
//        ed.commit();
//        Toast.makeText(this, "Text saved", Toast.LENGTH_SHORT).show();
//    }
//
//    void loadText() {
//        sPref = getPreferences(MODE_PRIVATE);
//        String savedText = sPref.getString(ID_ACCOUNT, "");
//        etText.setText(savedText);
//        Toast.makeText(this, "Text loaded", Toast.LENGTH_SHORT).show();
//    }
    public static String GET(String url){
        InputStream inputStream = null;
        String result = "";
        try {
            // create HttpClient
            HttpClient httpclient = new DefaultHttpClient();
            // make GET request to the given URL
            HttpResponse httpResponse = httpclient.execute(new HttpGet(url));
            // receive response as inputStream
            inputStream = httpResponse.getEntity().getContent();
            // convert inputstream to string
            if(inputStream != null)
                result = convertInputStreamToString(inputStream);
            else
                result = "Did not work!";
        } catch (Exception e) {
            Log.d("InputStream", e.getLocalizedMessage());
        }
        return result;
    }

    private static String convertInputStreamToString(InputStream inputStream) throws IOException {
        BufferedReader bufferedReader = new BufferedReader( new InputStreamReader(inputStream));
        String line = "";
        String result = "";
        while((line = bufferedReader.readLine()) != null)
            result += line;
        inputStream.close();
        return result;

    }
}
