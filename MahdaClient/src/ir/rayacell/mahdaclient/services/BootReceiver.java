package ir.rayacell.mahdaclient.services;

import ir.rayacell.mahdaclient.MainActivity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class BootReceiver extends BroadcastReceiver {
	@Override
	public void onReceive(Context context, Intent intent) {
		Intent intenta = new Intent(context, MainActivity.class);
		intenta.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		context.startActivity(intenta);
	}
}