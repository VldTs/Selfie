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

            Message myMessage=new Message();
            Bundle resBundle = new Bundle();
            resBundle.putString("status", "SUCCESS");
            myMessage.obj=resBundle;
            thisServise.msgService = "---++++---"+id_account;
            handler.sendMessage(myMessage);
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
//                    Runnable run0 = new Runnable() {
//                        @Override
//                        public void run() {
//                            MineViewsActivity.update();
//                        }
//                    };
                    voite = voite +1;
//            Message myMessage=new Message();
//            Bundle resBundle = new Bundle();
//            resBundle.putString("status", "SUCCESS");
//            myMessage.obj=resBundle;
//            thisServise.msgService = "++++++++++++++="+id_account;
//            handler.sendMessage(myMessage);
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

            Message myMessage=new Message();
            Bundle resBundle = new Bundle();
            resBundle.putString("status", "SUCCESS");
            myMessage.obj=resBundle;
            thisServise.msgService = "!!!!!="+voite;
            handler.sendMessage(myMessage);
            db_MC.updateRightMCVoite(331,1,voite);
//            Intent intent = new Intent (DATA_INSERTED);
            Intent intent = new Intent (MineViewsActivity.BROADCAST_ACTION);
            sendBroadcast(intent);
//            Intent intent = new Intent(MainActivity.BROADCAST_ACTION);
//            Log.d(LOG_TAG, "MyRun#" + startId + " start, time = " + time);
//            try {
//                // сообщаем об старте задачи
//                intent.putExtra(MainActivity.PARAM_TASK, task);
//                intent.putExtra(MainActivity.PARAM_STATUS, MainActivity.STATUS_START);
//                sendBroadcast(intent);
//
//                // начинаем выполнение задачи
//                TimeUnit.SECONDS.sleep(time);
//
//                // сообщаем об окончании задачи
//                intent.putExtra(MainActivity.PARAM_STATUS, MainActivity.STATUS_FINISH);
//                intent.putExtra(MainActivity.PARAM_RESULT, time * 100);
//                sendBroadcast(intent);
//
//            } catch (InterruptedException e) {
//                e.printStackTrace();
//            }

//            Runnable run0 = new Runnable() {
//                @Override
//                public void run() {
//                    MineViewsActivity.update();
//                }
//            };
//            new Thread(new Runnable() {
//                public void run() {
//                    runOnUiThread(new Runnable() {
//                        public void run() {
////                            img.setImageBitmap(finalBm);
//                        }
//                    });
//                }
//            }).start();
//            MineViewsActivity.runOnUiThread(run0);
//            try {
//                jObj = new JSONObject(result);
//
//                // Getting JSON Array
////                jaError = jObj.getJSONArray("error");
////                JSONObject joError = jaError.getJSONObject(0);
////                strError = joError.getString("status");
////                if (strError.equals("none")) {
////
////
////                    Message myMessage=new Message();
////                    Bundle resBundle = new Bundle();
////                    resBundle.putString("status", "SUCCESS");
////                    myMessage.obj=resBundle;
////                    thisServise.msgService = "обновили ";
////                    handler.sendMessage(myMessage);
////
//////                    jaCompare = jObj.getJSONArray("compare");
//////                    Integer iCompareLen = jaCompare.length();
////////                    JSONObject joCompare0 = jaCompare.getJSONObject(0);
//////
//////                        String strCompareLen = iCompareLen.toString();
//////                        // массивы данных
//////                        for (int iC = 0; iC < iCompareLen; iC = iC + 1) {
//////                            JSONObject joCompare = jaCompare.getJSONObject(iC);
//////                            int id = joCompare.getInt("id");
//////                            String date_crt = joCompare.getString("date_crt");
//////
//////                            jaPhoto = joCompare.getJSONArray("photo");
//////
//////                            JSONObject joPhotoLeft = jaPhoto.getJSONObject(0);
//////                            String pathLeft = joPhotoLeft.getString("path");
//////                            int idLeft =  joPhotoLeft.getInt("id");
//////                            int orientationLeft =  joPhotoLeft.getInt("Orientation");
//////                            int all_voiteLeft = joPhotoLeft.getInt("all_voite");
//////
//////                            JSONObject joPhotoRight = jaPhoto.getJSONObject(1);
//////                            String pathRight = joPhotoRight.getString("path");
//////                            int idRight =  joPhotoRight.getInt("id");
//////                            int orientationRight = joPhotoRight.getInt("Orientation");
//////                            int all_voiteRight = joPhotoRight.getInt("all_voite");
//////                            //                        Toast.makeText(getBaseContext(), "pathRight "+pathRight, Toast.LENGTH_LONG).show();
//////
//////                            db_MC.addRecMC(id, date_crt, all_voiteLeft ,all_voiteRight ,
//////                                    idLeft, idRight, orientationLeft, orientationRight, pathLeft, pathRight, 0);
//////
//////                        }
////
////                }else{
//////                    Toast.makeText(getBaseContext(), "простите, не получается прочитать ваши Selfie :(", Toast.LENGTH_LONG).show();
////                }
//
//            } catch (JSONException e) {
//                e.printStackTrace();
//            }
//            if (strError.equals("none")) {
//                Toast.makeText(getBaseContext(), "ваших Selfie загружены", Toast.LENGTH_LONG).show();
//            }else{
//                Toast.makeText(getBaseContext(), "простите, не могу загрузить список ваших Selfie :(", Toast.LENGTH_LONG).show();
//            }


        }

    }
}
