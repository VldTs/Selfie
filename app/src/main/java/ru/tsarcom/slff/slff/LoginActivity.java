package ru.tsarcom.slff.slff;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.app.Application;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Parcelable;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import ru.tsarcom.slff.slff.R;


public class LoginActivity extends ActionBarActivity  implements View.OnClickListener{
    private static String url = "http://95.78.234.20:81/phpmyadmin/js/get_image.js.php";
    //JSON Node Names
    private static final String TAG_USER = "user";
    private static final String TAG_ID = "id";
    private static final String TAG_NAME = "name";
    private static final String TAG_EMAIL = "email";

    final String ID_ACCOUNT = "ID_ACCOUNT";
    SharedPreferences sPref;
    EditText etEmail;
    EditText etPass;

    Button btnJson,btnOffLine;
    JSONArray user = null;
    TextView tvIsConnected;

    // String json = "";
    JSONObject json;
    JSONObject jObj = null;
    String country;
    String JsonString;
    String AccountEmail;

    String id_account;
    String status_user;

    protected AccountManager accountManager;
    LoginActivity activity;
    Class<MineViewsActivity> mineViewsActivityClass;
//    private void ShortcutIcon(){
//
//        Intent shortcutIntent = new Intent(getApplicationContext(), LoginActivity.class);
//        shortcutIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//        shortcutIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//
//        Intent addIntent = new Intent();
//        addIntent.putExtra(Intent.EXTRA_SHORTCUT_INTENT, shortcutIntent);
//        addIntent.putExtra(Intent.EXTRA_SHORTCUT_NAME, "Test");
//        addIntent.putExtra(Intent.EXTRA_SHORTCUT_ICON_RESOURCE, Intent.ShortcutIconResource.fromContext(getApplicationContext(), R.drawable.ic_launcher));
//        addIntent.setAction("com.android.launcher.action.INSTALL_SHORTCUT");
//        getApplicationContext().sendBroadcast(addIntent);
//    }
    public void createShortCut(){
        // a Intent to create a shortCut
        Intent shortcutintent = new Intent("com.android.launcher.action.INSTALL_SHORTCUT");
        //repeat to create is forbidden
        shortcutintent.putExtra("duplicate", false);
        //set the name of shortCut
        shortcutintent.putExtra(Intent.EXTRA_SHORTCUT_NAME, "FlyPanda");

        //set icon
        Parcelable icon = Intent.ShortcutIconResource.fromContext(getApplicationContext(), R.drawable.nemo);
        shortcutintent.putExtra(Intent.EXTRA_SHORTCUT_ICON_RESOURCE, icon);
        //set the application to lunch when you click the icon
        shortcutintent.putExtra(Intent.EXTRA_SHORTCUT_INTENT, new Intent(getApplicationContext() , LoginActivity.class));
        //sendBroadcast,done
        sendBroadcast(shortcutintent);
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);

//        try {
        accountManager = AccountManager.get(getApplicationContext());
        Account[] accounts = accountManager.getAccountsByType("com.google");
//        Account[] accounts = accountManager.getAccounts();//.getAccountsByType("com.google");
        if (accounts.length > 0) {
            AccountEmail = accounts[0].name;
            etEmail = (EditText) findViewById(R.id.etEmail);
            etEmail.setText(AccountEmail);
            etPass = (EditText) findViewById(R.id.etPass);
            etPass.setText(AccountEmail);
        }
//        }
//        catch (  Exception ex) {
//            Log.e("People Account creation failed because of exception:\n",ex.toString());
//        }
//            Log.e("People Account creation failed because of exception:\n");
        btnJson = (Button) findViewById(R.id.btnJson);
        btnOffLine = (Button) findViewById(R.id.btnOffLine);
        btnJson.setOnClickListener(this);
        btnOffLine.setOnClickListener(this);
        tvIsConnected = (TextView) findViewById(R.id.tvIsConnected);


        // check if you are connected or not
        activity = this;
        mineViewsActivityClass = MineViewsActivity.class;
        if(isConnected()){
            tvIsConnected.setBackgroundColor(0xFF00CC00);
            tvIsConnected.setText("You are connected");
            loadText();
            if (id_account.equals("")){
                saveText("-1");
                id_account = "-1";
            }else{

                startService(new Intent(this, SelfieServiceF.class).putExtra("id_account", id_account));

                startService(new Intent(this, SelfieServiceUpVoite.class).putExtra("id_account", id_account));

                Intent intent = new Intent(activity, mineViewsActivityClass);
                intent.putExtra("id_account", id_account);
                startActivity(intent);
                finish();
            }

            if (AccountEmail.equals("")) {
            }else {
                if (id_account.equals("-1")) {
                    String login = etEmail.getText().toString();
                    String pass = etPass.getText().toString();
                    String url0;
                    url0 = "http://95.78.234.20:81/atest/jsonLogin.php?login=" + login + "&pass=" + pass;
//                Toast.makeText(getBaseContext(), "url0 "+url0, Toast.LENGTH_LONG).show();
                    new HttpAsyncTask().execute(url0);
                }
            }
        }
        else{
            tvIsConnected.setText("You are NOT connected");
        }
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

    void loadText() {
//        sPref = getPreferences(MODE_PRIVATE);
        sPref = getSharedPreferences("MyPref",MODE_PRIVATE);
        String savedText = sPref.getString(ID_ACCOUNT, "");
        id_account = savedText;
//        Toast.makeText(this, "Text loaded", Toast.LENGTH_SHORT).show();
    }
    @Override
    public void onClick(View v) {
        // по id определеяем кнопку, вызвавшую этот обработчик
        switch (v.getId()) {
            case R.id.btnJson:
                String login = etEmail.getText().toString();
                String pass = etPass.getText().toString();
                String url0;
                url0 = "http://95.78.234.20:81/atest/jsonLogin.php?login="+login+"&pass="+pass;
//                Toast.makeText(getBaseContext(), "url0 "+url0, Toast.LENGTH_LONG).show();
                new HttpAsyncTask().execute(url0);
//                HttpAsyncTask oHttpAsyncTask = new HttpAsyncTask();
//               oHttpAsyncTask.execute("http://95.78.234.20:81/atest/jsonLogin.php?login=999");
                break;
            case R.id.btnOffLine:
                String id_account_t = "5";
                saveText(id_account_t);
                Intent intent = new Intent(activity, mineViewsActivityClass);
                intent.putExtra("id_account", id_account_t);
                startActivity(intent);
                finish();
                break;
        }
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_login, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
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
                user = jObj.getJSONArray(TAG_USER);
                JSONObject c = user.getJSONObject(0);
                // Storing  JSON item in a Variable

                id_account = c.getString("id_account");
                saveText(id_account);
                status_user = c.getString("status");
                //Toast.makeText(getBaseContext(), "111 status_user "+status_user, Toast.LENGTH_LONG).show();
            } catch (JSONException e) {
                e.printStackTrace();
            }
            // Toast.makeText(getBaseContext(), "2222222222 status_user "+status_user, Toast.LENGTH_LONG).show();
            if (status_user.equals("on")) {
                Intent intent = new Intent(activity, mineViewsActivityClass);
                intent.putExtra("id_account", id_account);
                startActivity(intent);
                finish();
            }else{
                Toast.makeText(getBaseContext(), "login or password not correct", Toast.LENGTH_LONG).show();
            }
        }

    }
}
