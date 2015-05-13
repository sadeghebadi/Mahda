package ir.rayacell.mahdaclient;

import ir.rayacell.mahdaclient.dao.OrderDao;
import ir.rayacell.mahdaclient.model.Command;
import ir.rayacell.mahdaclient.security.EncryptDecrypt;
import ir.rayacell.mahdaclient.services.ResponseReceiver;

import java.io.File;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.location.Location;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import com.google.android.gms.maps.model.LatLng;
import com.koushikdutta.async.http.server.AsyncHttpServer;
import com.koushikdutta.async.http.server.AsyncHttpServerRequest;
import com.koushikdutta.async.http.server.AsyncHttpServerResponse;
import com.koushikdutta.async.http.server.HttpServerRequestCallback;

public class ServerInit {
	final static int PORT = 5001;
	public static final String DEFAULT_STORAGE_LOCATION = App.getContext()
			.getResources().getString(R.string.default_location);

	public void init() {
		AsyncHttpServer server = new AsyncHttpServer();
		server.get("/order", new HttpServerRequestCallback() {
			@Override
			public void onRequest(AsyncHttpServerRequest request,
					AsyncHttpServerResponse response) {
				String orderId = request.getHeaders().get("orderId");

				OrderDao dao = new OrderDao(App.getContext());
				dao.readAllAsc().get(2);
				String status = dao.read(Long.parseLong(orderId)).status;
				response.send(status);
			}
		});
		server.get("/geo", new HttpServerRequestCallback() {
			private LocationManager locationManager;
			private Location getLastLocation;
			private double currentLongitude;
			private double currentLatitude;

			@Override
			public void onRequest(AsyncHttpServerRequest request,
					AsyncHttpServerResponse response) {
				locationManager = (LocationManager) App.getContext()
						.getSystemService(Context.LOCATION_SERVICE);
				getLastLocation = locationManager
						.getLastKnownLocation(LocationManager.PASSIVE_PROVIDER);
				currentLongitude = getLastLocation.getLongitude();
				currentLatitude = getLastLocation.getLatitude();
				response.send(currentLatitude + " " + currentLongitude);
			}
		});
		server.get("/", new HttpServerRequestCallback() {
			@Override
			public void onRequest(AsyncHttpServerRequest request,
					AsyncHttpServerResponse response) {
				response.sendFile(new File(DEFAULT_STORAGE_LOCATION+ "/"
						+ request.getHeaders().get("file")));
			}
		});
		server.get("/execute", new HttpServerRequestCallback() {
			@Override
			public void onRequest(AsyncHttpServerRequest request,
					AsyncHttpServerResponse response) {
				String orderStr = request.getHeaders().get("orderStr");
				try {
					orderStr = EncryptDecrypt.decrypt(orderStr);
				} catch (Exception e) {
				}
				if (orderStr.equals("busy")) {
					return;
				}
				String msgReply = orderStr.split("\\*")[orderStr.split("\\*").length - 1];
				try {
					msgReply = EncryptDecrypt.encrypt(msgReply);
				} catch (Exception e) {
				}
				response.send(msgReply);
				// update sen
				 publishResults(orderStr);
			}
		});
		server.listen(PORT);
	}

	private void publishResults(String result) {
		Intent intent = new Intent(Constants.BROADCAST_ACTION);
		intent.putExtra(Constants.RESULT_KEY, result);
		ResponseReceiver mResponseReceiver = new ResponseReceiver();
		IntentFilter mActionIntentFilter = new IntentFilter(
				Constants.BROADCAST_ACTION);
		LocalBroadcastManager.getInstance(App.getContext())
				.registerReceiver(mResponseReceiver, mActionIntentFilter);
		LocalBroadcastManager.getInstance(App.getContext()).sendBroadcast(intent);
	}
}
