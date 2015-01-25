package ru.tsarcom.slff.slff;

import android.app.IntentService;
import android.content.Intent;
import android.widget.Toast;

public class SelfieServiceZ extends IntentService {

    public SelfieServiceZ() {
        super("myname");
    }
    public void onCreate() {
        super.onCreate();

        Toast.makeText(getBaseContext(), "ZZZZZZ333  onCreate", Toast.LENGTH_LONG).show();
//        es = Executors.newFixedThreadPool(3);
//        someRes = new Object();
//        Log.d(LOG_TAG, "onCreate");
    }
//    public int onStartCommand(Intent intent, int flags, int startId) {
//
//        Toast.makeText(getBaseContext(), "ZZZZZZ333 onStartCommand", Toast.LENGTH_LONG).show();
//
////            return START_STICKY;
//            return super.onStartCommand(intent, flags, startId);
//
//    }
//    @Override
//    public IBinder onBind(Intent intent) {
//        // TODO: Return the communication channel to the service.
////        throw new UnsupportedOperationException("Not yet implemented");
//
//        return null;
//    }




        @Override
        protected void onHandleIntent(Intent intent) {
//            int tm = intent.getIntExtra("time", 0);
//            String label = intent.getStringExtra("label");
            Toast.makeText(getBaseContext(), "ZZZZZZ333  onHandleIntent", Toast.LENGTH_LONG).show();
        }

        public void onDestroy() {
            super.onDestroy();
            Toast.makeText(getBaseContext(), "ZZZZZZ333  onDestroy", Toast.LENGTH_LONG).show();
        }

}
