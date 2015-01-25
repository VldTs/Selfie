package ru.tsarcom.slff.slff;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import ru.tsarcom.slff.slff.R;

/**
 * Created by USRSLM on 02.12.14.
 */
public class SelfieServiceF extends Service {
    private static String TAG = SelfieServiceF.class.getSimpleName();
    private MyThread mythread;
    public boolean isRunning = false;

    final String ID_ACCOUNT = "ID_ACCOUNT";
    SharedPreferences sPref;
    JSONArray jaMsg = null;
    JSONObject jObj = null;
    String strMsg,strCCError;
    JSONArray jaCompare = null;
    NotificationManager nm;
    SelfieServiceF thisServise;
    String id_account;
    public String msgService = "--";
    @Override
    public IBinder onBind(Intent arg0) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
//        Log.d(TAG, "onCreate");
        thisServise = this;
        loadText();
        nm = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
//        Toast.makeText(getBaseContext(), "Service onCreate "+id_account, Toast.LENGTH_LONG).show();
        mythread  = new MyThread();
    }

//    @Override
//    public int onStartCommand(Intent intent, int flags, int startId) {
////        Log.d(LOG_TAG, "onStartCommand");
//
//        sendNotif();
//        return super.onStartCommand(intent, flags, startId);
//    }
    @Override
    public synchronized void onDestroy() {
        super.onDestroy();
//        Log.d(TAG, "onDestroy");
        Toast.makeText(getBaseContext(), "onDestroy 002 ", Toast.LENGTH_LONG).show();
        if(!isRunning){
            mythread.interrupt();
            mythread.stop();
        }
    }

    @Override
    public synchronized void onStart(Intent intent, int startId) {
        super.onStart(intent, startId);
//        try {
          //  id_account = intent.getStringExtra("id_account");
//        }
//        catch(Exception e){
//            Message myMessage=new Message();
//            Bundle resBundle = new Bundle();
//            resBundle.putString("status", "SUCCESS");
//            myMessage.obj=resBundle;
//            thisServise.msgService = "intent.getStringExtra"+id_account;
//            handler.sendMessage(myMessage);
//        }
//        Log.d(TAG, "onStart");
//        sendNotif();
//        Toast.makeText(getBaseContext(), "onStart", Toast.LENGTH_LONG).show();
        if(!isRunning){
            mythread.start();
            isRunning = true;
        }
    }
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            Toast.makeText(getApplicationContext(), "msg : "+thisServise.msgService, Toast.LENGTH_LONG).show();
        }
    };

    void sendNotif() {
        // 1-я часть
        Notification notif = new Notification(R.drawable.ic_launcher, "Новые Selfie",
                System.currentTimeMillis());
        notif.defaults = Notification.DEFAULT_SOUND;
        // 3-я часть
        Intent intent = new Intent(this, OthersViewsActivity.class);
//        String id_account = "3";
//        Toast.makeText(getBaseContext(), "1 sendNotif id_account = "+id_account, Toast.LENGTH_LONG).show();
//        intent.putExtra("id_account",id_account);
        intent.putExtra("id_account", id_account);
//        intent.putExtra(MineViewsActivity.FILE_NAME, "somefile++++++++++++++");
        PendingIntent pIntent = PendingIntent.getActivity(this, 0, intent, 0);

        // 2-я часть
        notif.setLatestEventInfo(this, "Новые Селфи", "от пользователя", pIntent);

        // ставим флаг, чтобы уведомление пропало после нажатия
        notif.flags |= Notification.FLAG_AUTO_CANCEL;

        // отправляем
        nm.notify(1, notif);

//        Message myMessage=new Message();
//        Bundle resBundle = new Bundle();
//        resBundle.putString("status", "SUCCESS");
//        myMessage.obj=resBundle;
//        thisServise.msgService = "открыть others";
//        handler.sendMessage(myMessage);
    }

    class MyThread extends Thread{
        static final long DELAY = 20000;
        @Override
        public void run(){
            while(isRunning){
//                Log.d(TAG,"Running");

                try {
                    String url0;
                    url0 = "http://95.78.234.20:81/atest/jsonCheck.php?id_account="+id_account;
                    new ChHttpAsyncTask().execute(url0);
                    Thread.sleep(DELAY);
                } catch (InterruptedException e) {
                    isRunning = false;
                    e.printStackTrace();
                }
            }
        }

    }

    void loadText() {
//        sPref = getPreferences(MODE_PRIVATE);
        sPref = getSharedPreferences("MyPref",MODE_PRIVATE);
        String savedText = sPref.getString(ID_ACCOUNT, "");
        id_account = savedText;
//        Toast.makeText(this, "Text loaded", Toast.LENGTH_SHORT).show();
    }
    //----
    private class ChHttpAsyncTask extends AsyncTask<String, Void, String> {
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
                jaMsg = jObj.getJSONArray("msg");
                JSONObject joMsg = jaMsg.getJSONObject(0);
                strMsg = joMsg.getString("status");

                if (strMsg.equals("new")) {
                    sendNotif();
                    Message myMessage=new Message();
                    Bundle resBundle = new Bundle();
                    resBundle.putString("status", "SUCCESS");
                    myMessage.obj=resBundle;
                    thisServise.msgService = "Есть новые Селфи";
                    handler.sendMessage(myMessage);
                }else{

                }

            } catch (JSONException e) {
                e.printStackTrace();
            }


        }

    }
}
