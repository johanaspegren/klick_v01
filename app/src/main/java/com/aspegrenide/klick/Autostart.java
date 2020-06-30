package com.aspegrenide.klick;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.util.Log;
import android.widget.Toast;

public class Autostart extends BroadcastReceiver
{
    @Override
    public void onReceive(Context context, Intent intent) {
        Log.i("Broadcast Listened", "device rebooted");

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            context.startForegroundService(new Intent(context, BluetoothConnectionService.class));
        } else {
            context.startService(new Intent(context, BluetoothConnectionService.class));
        }
    }
}