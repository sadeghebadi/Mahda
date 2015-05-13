package ir.rayacell.mahdaclient.executer;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import ir.rayacell.mahdaclient.App;
import ir.rayacell.mahdaclient.manager.Container;
import ir.rayacell.mahdaclient.manager.DateTimeManager;
import ir.rayacell.mahdaclient.param.MotionVideoRecordParam;
import ir.rayacell.mahdaclient.param.VideoRecordParam;
import ir.rayacell.mahdaclient.param.VoiceRecordParam;
import ir.rayacell.mahdaclient.services.MotionVideoRecordStarter;
import ir.rayacell.mahdaclient.services.VideoRecordStarter;
import ir.rayacell.mahdaclient.services.VoiceRecordStarter;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.hardware.Camera;
import android.os.SystemClock;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;

public class MotionVideoExecuter implements SurfaceHolder.Callback {

	public static Camera mCamera;
	public static SurfaceView mSurfaceView;
	public static SurfaceHolder mSurfaceHolder;
	private int duration;
	private int quality;
	private String orderTime;

	@SuppressWarnings("deprecation")
	public MotionVideoExecuter(MotionVideoRecordParam param ,String orderId) {
		duration = param.getDuration();
		quality = param.getQuality();
		orderTime = param.getOrderTime();
		Log.d("in executer", param.getDate_and_time() + " &&&&&&*****&&&&& "
				+ param.getDate_and_time());

		Map<String, Integer> mdatetime = new HashMap<String, Integer>();
		mdatetime = new DateTimeManager().parseDateTime(param
				.getDate_and_time());
		@SuppressWarnings("deprecation")
		Date date = new Date(mdatetime.get("year") - 1900,
				mdatetime.get("month") - 1, mdatetime.get("day"),
				mdatetime.get("hour"), mdatetime.get("minute"));
		final int _id = (int) System.currentTimeMillis();

		Intent intent = new Intent(App.getContext(), MotionVideoRecordStarter.class);
		intent.putExtra("duration", duration);
		intent.putExtra("quality", quality);
		intent.putExtra("orderId",orderId);
		intent.putExtra("orderTime", orderTime);
		PendingIntent pending_intent = PendingIntent.getBroadcast(
				App.getContext(), _id, intent, PendingIntent.FLAG_ONE_SHOT);
		AlarmManager alarm = (AlarmManager) App.getContext().getSystemService(
				Context.ALARM_SERVICE);

		Log.d("calendar", date.getTime() + "#$$#$#$#$#$#$#$#$#$#$#$#$#$#");
		Log.d("curent", System.currentTimeMillis() + "  %%%%% current");
		Log.d("interval", date.getTime() - System.currentTimeMillis()
				+ " %%%%%%%% interval");

		alarm.set(
				AlarmManager.ELAPSED_REALTIME_WAKEUP,
				SystemClock.elapsedRealtime() + date.getTime()
						- System.currentTimeMillis(), pending_intent);
	}

	public void surfaceChanged(SurfaceHolder arg0, int arg1, int arg2, int arg3) {
		// TODO Auto-generated method stub

	}

	public void surfaceCreated(SurfaceHolder arg0) {
		// TODO Auto-generated method stub

	}

	public void surfaceDestroyed(SurfaceHolder arg0) {
		// TODO Auto-generated method stub

	}
}
