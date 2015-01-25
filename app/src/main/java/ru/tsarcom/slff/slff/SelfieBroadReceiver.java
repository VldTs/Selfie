package ru.tsarcom.slff.slff;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public  class SelfieBroadReceiver extends BroadcastReceiver {
//    public SelfieBroadReceiver() {
//    }
//
//    @Override
//    public void onReceive(Context context, Intent intent) {
//        // TODO: This method is called when the BroadcastReceiver is receiving
//        // an Intent broadcast.
//        context.startService(new Intent(context, SelfieServiceF.class));
//        throw new UnsupportedOperationException("Not yet implemented");
//    }

//    final String LOG_TAG = "myLogs";

    public void onReceive(Context context, Intent intent) {
//        Log.d(LOG_TAG, "onReceive " + intent.getAction());
        context.startService(new Intent(context, SelfieServiceF.class));
    }
}
