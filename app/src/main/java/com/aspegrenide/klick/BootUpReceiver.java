package com.aspegrenide.klick;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

public class BootUpReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        /****** For Start Activity *****/
        Log.d("GOGOGOG", "HIHII");
        Intent i = new Intent(context, MainActivity.class);
        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(i);

        /***** For start Service  ****/
        Intent myIntent = new Intent(context, BluetoothConnectionService.class);
        context.startService(myIntent);
    }

}