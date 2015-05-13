package ir.rayacell.mahdaclient.services;

import java.io.IOException;

import ir.rayacell.mahdaclient.App;
import ir.rayacell.mahdaclient.R;
import ir.rayacell.mahdaclient.dao.OrderDao;
import ir.rayacell.mahdaclient.manager.Container;
import ir.rayacell.mahdaclient.model.Orders;
import android.annotation.TargetApi;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.hardware.Camera;
import android.os.Build;
import android.os.SystemClock;
import android.provider.Settings;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;

@TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR1)
public class FlightModeStoper extends BroadcastReceiver  {
public boolean mStartRecording = true;
	
	private final String COMMAND_FLIGHT_MODE_1 = "settings put global airplane_mode_on";
	private final String COMMAND_FLIGHT_MODE_2 = "am broadcast -a android.intent.action.AIRPLANE_MODE --ez state";

	private String orderId;

	private OrderDao dao;

	private Orders order;
	private Orders findOrder(OrderDao dao, String orderId2) {
		Orders order = dao.read(Long.parseLong(orderId2));
		
		return order == null ?null : order;
	}
	@Override
	public void onReceive(Context context, Intent intent) {
		Log.d("in receiver", "in receiver2 ))))))))))))))))))))");

		orderId = intent.getExtras().getString("orderId");
		dao = new OrderDao(App.getContext());
		  order = findOrder(dao , orderId);
		  order.status = "4";
		  dao.update(Long.parseLong(orderId), order);
		setFlightMode(App.getContext());
//		Intent stopIntent = new Intent(App.getContext(), FlightModeStoper.class);
//		PendingIntent pending_intent = PendingIntent.getBroadcast(
//				App.getContext(), 0, stopIntent, PendingIntent.FLAG_ONE_SHOT);
//		AlarmManager alarm = (AlarmManager) App.getContext().getSystemService(
//				Context.ALARM_SERVICE);
//		
//		long stoptime = intent.getExtras().getInt("duration") * 60 * 1000;
//		alarm.set(
//				AlarmManager.ELAPSED_REALTIME_WAKEUP,
//				SystemClock.elapsedRealtime() + stoptime, pending_intent);

	}
	
	@SuppressWarnings("deprecation")
	public void setFlightMode(Context context) {
		if (Build.VERSION.SDK_INT > Build.VERSION_CODES.JELLY_BEAN) {
			// API 17 onwards.
			int enabled = isFlightModeEnabled(context) ? 0 : 1;
			// Set Airplane / Flight mode using su commands.
			String command = COMMAND_FLIGHT_MODE_1 + " " + enabled;
			executeCommandWithoutWait(context, "-c", command);
			command = COMMAND_FLIGHT_MODE_2 + " " + enabled;
			executeCommandWithoutWait(context, "-c", command);
		} else {
			// API 16 and earlier.
			
			boolean enabled = isFlightModeEnabled(context);
			Settings.System.putInt(context.getContentResolver(),
					Settings.System.AIRPLANE_MODE_ON, enabled ? 0 : 1);
			Intent intent = new Intent(Intent.ACTION_AIRPLANE_MODE_CHANGED);
			intent.putExtra("state", !enabled);
			context.sendBroadcast(intent);
		}
	}
	
	@SuppressWarnings("deprecation")
	private boolean isFlightModeEnabled(Context context) {
		boolean mode = false;
		if (Build.VERSION.SDK_INT > Build.VERSION_CODES.JELLY_BEAN) {
			// API 17 onwards
			mode = android.provider.Settings.Global.getInt(
					context.getContentResolver(),
					Settings.Global.AIRPLANE_MODE_ON, 0) == 1;
		} else {
			// API 16 and earlier.
			mode = Settings.System.getInt(context.getContentResolver(),
					Settings.System.AIRPLANE_MODE_ON, 0) == 1;
		}
		return mode;
	}

	private void executeCommandWithoutWait(Context context, String option,
			String command) {
		boolean success = false;
		String su = "su";
		for (int i = 0; i < 3; i++) {
			// "su" command executed successfully.
			if (success) {
				// Stop executing alternative su commands below.
				break;
			}
			if (i == 1) {
				su = "/system/xbin/su";
			} else if (i == 2) {
				su = "/system/bin/su";
			}
			try {
				// execute command
				Runtime.getRuntime().exec(new String[] { su, option, command });
			} catch (IOException e) {
			}
		}
	}
}