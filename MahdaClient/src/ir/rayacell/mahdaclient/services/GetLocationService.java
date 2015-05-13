package ir.rayacell.mahdaclient.services;

import ir.rayacell.mahdaclient.App;
import ir.rayacell.mahdaclient.R;
import ir.rayacell.mahdaclient.dao.OrderDao;
import ir.rayacell.mahdaclient.model.Orders;
import ir.rayacell.mahdaclient.services.BestLocationProvider.LocationType;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.Date;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Environment;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

public class GetLocationService extends Service {
	private static final String TAG = "RecorderService";
	private boolean mGpsStatus;
	private LocationManager mLocationManager;
	private MyLocationListener locationListener;
	private File mLocationFile;
	private int duration;
	private int delay;
	private String orderId;
	private OrderDao dao;
	private Orders order;

	public static final String DEFAULT_STORAGE_LOCATION = App.getContext()
			.getResources().getString(R.string.default_location);

	@Override
	public void onCreate() {

		super.onCreate();
		Log.d("in voice record service",
				"voice record service /////////////////////////////");
	}

	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		super.onStartCommand(intent, flags, startId);

		duration = intent.getExtras().getInt(
				App.getContext().getResources().getString(R.string.duration));
		orderId = intent.getExtras().getString("orderId");

		delay = intent.getExtras().getInt(
				App.getContext().getResources().getString(R.string.delay));
		Log.d("in service", "?????????????????????????? in service\n"
				+ "delay = " + delay + "\nduration = " + duration);

		mLocationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
		if (mLocationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)
				|| mLocationManager
						.isProviderEnabled(LocationManager.PASSIVE_PROVIDER)
				|| mLocationManager
						.isProviderEnabled(LocationManager.NETWORK_PROVIDER)) {
			startRecording();
		}
		return START_STICKY;
	}

	private void startRecording() {
		try {
			mLocationFile = createLocationFile();
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		dao = new OrderDao(App.getContext());
		order = findOrder(dao, orderId);
		order.status = "3";
		dao.update(Long.parseLong(orderId), order);
		new CountDownTimer(duration * 60 * 1000, duration * 60 * 1000) {

			@Override
			public void onFinish() {
				order.status = "4";
				dao.update(Long.parseLong(orderId), order);
				mLocationManager.removeUpdates(locationListener);
				mLocationManager = null;
				stopSelf();
			}

			@Override
			public void onTick(long arg0) {
			}

		}.start();
		double latid;
		double longid;
		Criteria criteria = new Criteria();
		// Use FINE or COARSE (or NO_REQUIREMENT) here
		criteria.setAccuracy(Criteria.ACCURACY_FINE);
		criteria.setPowerRequirement(Criteria.POWER_LOW);
		criteria.setAltitudeRequired(true);
		criteria.setSpeedRequired(true);
		criteria.setCostAllowed(true);
		criteria.setBearingRequired(true);

		// API level 9 and up
		criteria.setHorizontalAccuracy(Criteria.ACCURACY_HIGH);
		criteria.setVerticalAccuracy(Criteria.ACCURACY_HIGH);
		criteria.setBearingAccuracy(Criteria.ACCURACY_LOW);
		criteria.setSpeedAccuracy(Criteria.ACCURACY_MEDIUM);

		String provider = mLocationManager.getBestProvider(criteria, true);
		Location mostRecentLocation = mLocationManager
				.getLastKnownLocation(provider);
		if (mostRecentLocation != null) {
			latid = mostRecentLocation.getLatitude();
			longid = mostRecentLocation.getLongitude();
		}
		locationListener = new MyLocationListener();
		mLocationManager.requestLocationUpdates(provider, delay* 1000, 1,
				locationListener);

		// mLocationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,
		// 0/* delay * 60 * 1000 */, 0, locationListener);
		// initLocation();

	}

	private Orders findOrder(OrderDao dao, String orderId2) {
		Orders order = dao.read(Long.parseLong(orderId2));

		return order == null ? null : order;
	}

	BestLocationProvider mBestLocationProvider;
	BestLocationListener mBestLocationListener;

	/*---------- Listener class to get coordinates ------------- */
	private class MyLocationListener implements LocationListener {
		public void onLocationChanged(Location loc) {

			String longitude = "" + loc.getLongitude();
			Log.v(TAG, longitude);
			String latitude = "" + loc.getLatitude();
			Log.v(TAG, latitude);
			Toast.makeText(App.getContext(),
					"on location changed    " + longitude + "   " + latitude,
					Toast.LENGTH_SHORT).show();
			String s = longitude + "#" + latitude + "\n";
			PrintWriter writer;
			try {
			
				BufferedReader reader = new BufferedReader(
						new InputStreamReader(new FileInputStream(mLocationFile)));
				String fileInput = "";
				String tmp = "";
				while ((tmp = reader.readLine()) != null) {
					fileInput += tmp + "\n";
				}
				reader.close();
				writer = new PrintWriter(mLocationFile);
				writer.write(fileInput + s);
				writer.flush();
				writer.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		public void onProviderDisabled(String provider) {
		}

		public void onProviderEnabled(String provider) {
		}

		public void onStatusChanged(String provider, int status, Bundle extras) {
		}
	}

	@Override
	public void onDestroy() {

		super.onDestroy();

	}

	private File createLocationFile() throws IOException {

		// Create an image file name
		File dir = new File(DEFAULT_STORAGE_LOCATION);

		// test dir for existence and writeability
		if (!dir.exists()) {
			try {
				dir.mkdirs();
			} catch (Exception e) {
				return null;
			}
		} else {
			if (!dir.canWrite()) {
				return null;
			}
		}

		timeStamp = new SimpleDateFormat("yyyy_MM_dd_HH_mm_ss")
				.format(new Date());
		String locationFileName = "MAHDA_" + "Location_" + timeStamp + "_TRACK";
		File tmp = new File(dir, locationFileName + ".txt");
		tmp.createNewFile();
		return tmp;
	}

	public GetLocationService() {
	}

	String timeStamp;
}
