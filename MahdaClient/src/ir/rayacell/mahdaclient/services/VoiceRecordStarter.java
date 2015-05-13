package ir.rayacell.mahdaclient.services;

import ir.rayacell.mahdaclient.App;
import ir.rayacell.mahdaclient.R;
import ir.rayacell.mahdaclient.R.string;
import ir.rayacell.mahdaclient.manager.Container;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

public class VoiceRecordStarter extends BroadcastReceiver {

	@Override
	public void onReceive(Context context, Intent intent) {
		Log.d("in receiver", "in receiver ))))))))))))))))))))");
		Intent service = new Intent(Container.activity,
				VoiceRecordService.class);
		service.putExtra(
				App.getContext().getResources().getString(R.string.duration),
				intent.getExtras().getInt("duration"));
		service.putExtra(
				"orderId",
				intent.getExtras().getString("orderId"));
		Container.activity.startService(service);
	}
}