package ir.rayacell.mahda.services;

import ir.rayacell.mahda.App;
import ir.rayacell.mahda.Constants;
import ir.rayacell.mahda.security.EncryptDecrypt;

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

	public void init() {
		AsyncHttpServer server = new AsyncHttpServer();
	
		server.get("/execute", new HttpServerRequestCallback() {
			@Override
			public void onRequest(AsyncHttpServerRequest request,
					AsyncHttpServerResponse response) {
			try {
				String	messageFromClient = EncryptDecrypt.decrypt(request.getHeaders().get("orderStr"));
				String msgReply = "Hello from Android, you are #" ;
				msgReply = EncryptDecrypt.encrypt(msgReply);

				publishResults(messageFromClient);
			} catch (Exception e) {
			}
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
