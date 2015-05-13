package ir.rayacell.mahdaclient.executer;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Timer;
import java.util.TimerTask;

import android.app.Service;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

public class TimeService extends Service {
	// constant
	public static final long NOTIFY_INTERVAL = 20 * 1000; // 10 seconds

	// run on another Thread to avoid crash
	private Handler mHandler = new Handler();
	// timer handling
	private Timer mTimer = null;
	int i = 0;

	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}

	@Override
	public void onCreate() {
		// cancel if already existed
		if (mTimer != null) {
			mTimer.cancel();
		} else {
			// recreate new
			mTimer = new Timer();
		}
		// schedule task
		mTimer.scheduleAtFixedRate(new TimeDisplayTimerTask(), 0,
				NOTIFY_INTERVAL);
	}

	class TimeDisplayTimerTask extends TimerTask {

		@Override
		public void run() {
			// run on another thread
			mHandler.post(new Runnable() {
				public void run() {
					// display toast
					Toast.makeText(getApplicationContext(), getDateTime(),
							Toast.LENGTH_SHORT).show();
					new LongOperation().execute("");
				}
			});
		}

		private String getDateTime() {
			// get date time in custom format
			SimpleDateFormat sdf = new SimpleDateFormat(
					"[yyyy/MM/dd - HH:mm:ss]");
			return sdf.format(new Date());
		}
	}

	private class LongOperation extends AsyncTask<String, Void, String> {
		@Override
		protected String doInBackground(String... params) {
			System.out
					.println("bgnotification Long operation doinbackground called----> ");
			return "";
		}

		@Override
		protected void onPostExecute(String result) {
			System.out
					.println("bgnotification Long operation post execute called----> ");
			if (i < 3) {
				// GPSTracker mGPS = new GPSTracker(getApplicationContext());
				// onLocationChanged(mGPS);i++;
				Toast.makeText(getApplicationContext(),
						"HEllo Post execute called", Toast.LENGTH_LONG).show();
			}
		}

		@Override
		protected void onPreExecute() {
			System.out
					.println("bgnotification Long operation pre execute called----> ");
		}

		@Override
		protected void onProgressUpdate(Void... values) {
		}
	}

	// public void onLocationChanged(GPSTracker track) {
	// // Getting latitude
	// double latitude = track.getLatitude();
	// // Getting longitude
	// double longitude = track.getLongitude();
	// System.out.println(latitude);
	// System.out.println(longitude);
	// Geocoder geocoder = new Geocoder(this, Locale.getDefault());
	// try {
	// List<Address> addresses = geocoder.getFromLocation(latitude,
	// longitude, 1);
	// Log.e("Addresses", "-->" + addresses);
	// } catch (IOException e) {
	// e.printStackTrace();
	//
	// }
	// }
}