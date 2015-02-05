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

/**
 * Created by USRSLM on 02.12.14.
 */
public class SelfieServiceUpVoite extends Service {
    private static String TAG = SelfieServiceUpVoite.class.getSimpleName();
    private MyThread mythread;
    public boolean isRunning = false;

    final String ID_ACCOUNT = "ID_ACCOUNT";
    SharedPreferences sPref;
    JSONArray jaMsg = null;
    String strMsg;


    String strError,strCCError;
    JSONObject jObj = null;
    JSONObject jCCObj = null;
    JSONArray jaAccount = null;
    JSONArray jaError = null;
    JSONArray jaCCError = null;
    JSONArray jaCompare = null;
    JSONArray jaPhoto = null;
    JSONArray jaCCompare = null;
    NotificationManager nm;
    SelfieServiceUpVoite thisServise;
    String id_account;
    public String msgService = "--";
    public int voite;
    public static final String DATA_INSERTED = "WHATEVER_YOU_WANT";
    DB_MineCompare db_MC;
    @Override
    public IBinder onBind(Intent arg0) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
//        Log.d(TAG, "onCreate");
        voite = 0;
        db_MC = new DB_MineCompare(this);
        db_MC.open();
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

//            Message myMessage=new Message();
//            Bundle resBundle = new Bundle();
//            resBundle.putString("status", "SUCCESS");
//            myMessage.obj=resBundle;
//            thisServise.msgService = "---++++---"+id_account;
//            handler.sendMessage(myMessage);
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
        static final long DELAY = 10000;
        @Override
        public void run(){
            while(isRunning){
//                Log.d(TAG,"Running");

                try {
                    String url0;
                    url0 = "http://95.78.234.20:81/atest/jsonMineLoadVoite.php?id_account="+id_account;

//                    Message myMessage1=new Message();
//                    Bundle resBundle1 = new Bundle();
//                    resBundle1.putString("status", "SUCCESS");
//                    myMessage1.obj=resBundle1;
//                    thisServise.msgService = "url0 = "+url0;
//                    handler.sendMessage(myMessage1);
                    voite = voite +1;
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
//        Toast.makeText(this, "Text loaded"+id_account+"-", Toast.LENGTH_SHORT).show();
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
//               Toast.makeText(getBaseContext(), "Received!", Toast.LENGTH_LONG).show();

//            Message myMessage0=new Message();
//            Bundle resBundle0 = new Bundle();
//            resBundle0.putString("status", "SUCCESS");
//            myMessage0.obj=resBundle0;
//            thisServise.msgService = "result = "+result;
//            handler.sendMessage(myMessage0);
            try {
                jObj = new JSONObject(result);
                // Getting JSON Array
                jaMsg = jObj.getJSONArray("error");
                JSONObject joMsg = jaMsg.getJSONObject(0);
                strMsg = joMsg.getString("status");
                if (strMsg.equals("none")) {
//                    sendNotif();
                    jaCompare = jObj.getJSONArray("compare");
                    Integer iCompareLen = jaCompare.length();
//                    JSONObject joCompare0 = jaCompare.getJSONObject(0);
//                    String description0 = joCompare0.getString("description");

                    for (int iC = 0; iC < iCompareLen; iC = iC + 1) {
                        JSONObject joCompare = jaCompare.getJSONObject(iC);
                        int id = joCompare.getInt("id");
//                        String date_crt = joCompare.getString("date_crt");
//
                        jaPhoto = joCompare.getJSONArray("photo");
//
                        JSONObject joPhotoLeft = jaPhoto.getJSONObject(0);
                        int all_voiteLeft = joPhotoLeft.getInt("all_voite");
//
                        JSONObject joPhotoRight = jaPhoto.getJSONObject(1);
                        int all_voiteRight = joPhotoRight.getInt("all_voite");

                        db_MC.updateRightMCVoite(id,all_voiteLeft,all_voiteRight);

                    }
//            db_MC.updateRightMCVoite(331,1,voite);
//            Intent intent = new Intent (DATA_INSERTED);
//            Intent intent = new Intent (MineViewsActivity.BROADCAST_ACTION);
                    Intent intent = new Intent (MineViewsActivity.BROADCAST_ACTION);
                    sendBroadcast(intent);

                }else{

                    Message myMessage2=new Message();
                    Bundle resBundle2 = new Bundle();
                    resBundle2.putString("status", "SUCCESS");
                    myMessage2.obj=resBundle2;
                    thisServise.msgService = "error load voite :( ";
                }
            } catch (JSONException e) {
                e.printStackTrace();

                Message myMessage=new Message();
                Bundle resBundle = new Bundle();
                resBundle.putString("status", "SUCCESS");
                myMessage.obj=resBundle;
                thisServise.msgService = "errrrrrrrrrrrrr ";
            }


        }

    }
}
