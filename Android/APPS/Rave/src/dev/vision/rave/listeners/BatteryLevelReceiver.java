package dev.vision.rave.listeners;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

public class BatteryLevelReceiver extends BroadcastReceiver {

	public BatteryLevelReceiver() {}

	@Override
	public void onReceive(Context c, Intent arg1) {
		Toast.makeText(c, "Going Into PowerSaver Mode", Toast.LENGTH_LONG).show();
	}

}
