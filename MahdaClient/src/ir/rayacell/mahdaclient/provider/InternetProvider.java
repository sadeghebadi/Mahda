//package ir.rayacell.mahdaclient.provider;
//
//import ir.rayacell.mahdaclient.App;
//import ir.rayacell.mahdaclient.Constants;
//import ir.rayacell.mahdaclient.MainActivity;
//import ir.rayacell.mahdaclient.manager.Container;
//import ir.rayacell.mahdaclient.manager.NetworkManager;
//import ir.rayacell.mahdaclient.model.BaseModel;
//import ir.rayacell.mahdaclient.model.Command;
//import ir.rayacell.mahdaclient.param.*;
//import ir.rayacell.mahdaclient.security.EncryptDecrypt;
//import ir.rayacell.mahdaclient.services.ServerService;
//
//import java.io.BufferedInputStream;
//import java.io.DataInputStream;
//import java.io.DataOutputStream;
//import java.io.File;
//import java.io.FileInputStream;
//import java.io.IOException;
//import java.io.OutputStream;
//import java.net.InetAddress;
//import java.net.NetworkInterface;
//import java.net.ServerSocket;
//import java.net.Socket;
//import java.net.SocketException;
//import java.net.UnknownHostException;
//import java.util.Enumeration;
//
//import android.app.ActivityManager;
//import android.app.IntentService;
//import android.app.ActivityManager.RunningServiceInfo;
//import android.content.BroadcastReceiver;
//import android.content.Context;
//import android.content.Intent;
//import android.content.IntentFilter;
//import android.os.AsyncTask;
//import android.os.Build;
//import android.os.Bundle;
//import android.support.v4.content.LocalBroadcastManager;
//import android.util.Log;
//import android.widget.Toast;
//
//public class InternetProvider extends BaseProvider {
//
//	public InternetProvider(ProviderManager providerManager,
//			MainActivity activity) {
//		super(providerManager, activity);
//
//		
//		
//	}
//
//	private boolean isMyServiceRunning(Class<?> serviceClass) {
//	    ActivityManager manager = (ActivityManager) App.getContext().getSystemService(Context.ACTIVITY_SERVICE);
//	    for (RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
//	        if (serviceClass.getName().equals(service.service.getClassName())) {
//	            return true;
//	        }
//	    }
//	    return false;
//	}
//	@Override
//	public boolean connect(BaseParam param) {
////		if(isMyServiceRunning(ir.rayacell.mahdaclient.services.ServerService.class)){
////			Intent server_intent = new Intent(activity,
////					ir.rayacell.mahdaclient.services.ServerService.class);
////			activity.stopService(server_intent);
////		}
//		System.out.println("ip:" + NetworkManager.getIpAddress());
//		Intent server_intent = new Intent(activity,
//				ir.rayacell.mahdaclient.services.ServerService.class);
//		server_intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//		activity.startService(server_intent);
//		return false;
//	}
//	@Override
//	public boolean send(BaseParam param) {
//		System.out.println(param.getCommand_type() + "  wifi  "
//				+ "==============================");
//		MyClientTask myClientTask = new MyClientTask(NetworkManager.dstAddress,
//				NetworkManager.dstPort,
//				/*
//				 * param.getCommand_type() + "     " + param.getPhone_number() +
//				 * "     " +
//				 *//*
//					 * ((VoiceRecordParam) param).getDate_and_time() +
//					 * " $$$$$$$$$$$$$$$$$$$$$$$$$$"
//					 */
//				param.getCommand());
//		if (Build.VERSION.SDK_INT>=Build.VERSION_CODES.HONEYCOMB)
//			myClientTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
//		else
//			myClientTask.execute();
//		return false;
//	}
//
//	@Override
//	public synchronized void recieve(BaseParam param) {
//		// test//
//
//		mProviderManager.recieve(param);
//		// connect();
//	}
//
//	
//
//	public class MyClientTask extends AsyncTask<Void, Void, Void> {
//		String dstAddress;
//		int dstPort;
//		String response = "";
//		String msgToServer;
//
//		MyClientTask(String addr, int port, String msgTo) {
//			dstAddress = addr;
//			dstPort = port;
//			msgToServer = msgTo;
//		}
//		@Override
//		protected synchronized Void doInBackground(Void... arg0) {
//
//			Socket socket = null;
//			DataOutputStream dataOutputStream = null;
//			DataInputStream dataInputStream = null;
//
//			try {
//				Log.d("ipport", dstAddress + "   HHHHHHHHHHHHHHHHHH " + dstPort
//						+ "     JJJJJJJJJJJJJJ");
//				socket = new Socket(dstAddress, dstPort);
//				dataOutputStream = new DataOutputStream(
//						socket.getOutputStream());
//				dataInputStream = new DataInputStream(socket.getInputStream());
//
//				if (msgToServer != null) {
//					msgToServer = EncryptDecrypt.encrypt(msgToServer);
//					dataOutputStream.writeUTF(msgToServer);
//				
//				}
//				response = dataInputStream.readUTF();
//				response = EncryptDecrypt.decrypt(response);
////				dataOutputStream.writeUTF(msgToServer);
//				
//			} catch (Exception e) {
//				e.printStackTrace();
////				response = "UnknownHostException: " + e.toString();
////				e.printStackTrace();
////				response = "IOException: " + e.toString();
//			} finally {
//				if (socket != null) {
//					try {
//						socket.close();
//					} catch (IOException e) {
//						e.printStackTrace();
//					}
//				}
//				if (dataOutputStream != null) {
//					try {
//						dataOutputStream.close();
//					} catch (IOException e) {
//						e.printStackTrace();
//					}
//				}
//				if (dataInputStream != null) {
//					try {
//						dataInputStream.close();
//					} catch (IOException e) {
//						e.printStackTrace();
//					}
//				}
//			}
//			return null;
//		}
//
//		@Override
//		protected void onPostExecute(Void result) {
//			Log.d("tag", response + "((((((((((((((((((((((((((((((((((((((");
//			activity.updateView(response);
//			super.onPostExecute(result);
//		}
//	}
//	
//}


package ir.rayacell.mahdaclient.provider;

import ir.rayacell.mahdaclient.App;
import ir.rayacell.mahdaclient.Constants;
import ir.rayacell.mahdaclient.MainActivity;
import ir.rayacell.mahdaclient.ServerInit;
import ir.rayacell.mahdaclient.manager.Container;
import ir.rayacell.mahdaclient.manager.NetworkManager;
import ir.rayacell.mahdaclient.model.BaseModel;
import ir.rayacell.mahdaclient.model.Command;
import ir.rayacell.mahdaclient.param.*;
import ir.rayacell.mahdaclient.security.EncryptDecrypt;

import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.Enumeration;

import com.koushikdutta.async.http.AsyncHttpClient;
import com.koushikdutta.async.http.AsyncHttpGet;
import com.koushikdutta.async.http.AsyncHttpResponse;
import com.koushikdutta.async.http.AsyncHttpClient.StringCallback;

import android.app.ActivityManager;
import android.app.IntentService;
import android.app.ActivityManager.RunningServiceInfo;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import android.widget.Toast;

public class InternetProvider extends BaseProvider {

	public InternetProvider(ProviderManager providerManager,
			MainActivity activity) {
		super(providerManager, activity);
	}
	private boolean isMyServiceRunning(Class<?> serviceClass) {
	    ActivityManager manager = (ActivityManager) App.getContext().getSystemService(Context.ACTIVITY_SERVICE);
	    for (RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
	        if (serviceClass.getName().equals(service.service.getClassName())) {
	            return true;
	        }
	    }
	    return false;
	}
	@Override
	public boolean connect(BaseParam param) {
//		if(isMyServiceRunning(ir.rayacell.mahdaclient.services.ServerService.class)){
//			Intent server_intent = new Intent(activity,
//					ir.rayacell.mahdaclient.services.ServerService.class);
//			activity.stopService(server_intent);
//		}

//		System.out.println("ip:" + NetworkManager.getIpAddress());
//		Intent server_intent = new Intent(activity,
//				ir.rayacell.mahdaclient.services.ServerService.class);
//		server_intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//		activity.startService(server_intent);
		return false;
	}
	@Override
	public boolean send(BaseParam param) {
		System.out.println(param.getCommand_type() + "  wifi  "
				+ "==============================");
		MyClientTask myClientTask = new MyClientTask(NetworkManager.dstAddress,
				NetworkManager.dstPort,
				/*
				 * param.getCommand_type() + "     " + param.getPhone_number() +
				 * "     " +
				 *//*
					 * ((VoiceRecordParam) param).getDate_and_time() +
					 * " $$$$$$$$$$$$$$$$$$$$$$$$$$"
					 */
				param.getCommand());
		if (Build.VERSION.SDK_INT>=Build.VERSION_CODES.HONEYCOMB)
			myClientTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
		else
			myClientTask.execute();
		return false;
	}

	@Override
	public synchronized void recieve(BaseParam param) {
		// test//

		mProviderManager.recieve(param);
		// connect();
	}

	

	public class MyClientTask extends AsyncTask<Void, Void, Void> {
		String dstAddress;
		int dstPort;
		String response = "";
		String msgToServer;

		MyClientTask(String addr, int port, String msgTo) {
			dstAddress = addr;
			dstPort = port;
			msgToServer = msgTo;
		}
		@Override
		protected synchronized Void doInBackground(Void... arg0) {

//			Socket socket = null;
//			DataOutputStream dataOutputStream = null;
//			DataInputStream dataInputStream = null;

			try {
				Log.d("ipport", dstAddress + "   HHHHHHHHHHHHHHHHHH " + dstPort
						+ "     JJJJJJJJJJJJJJ");
//				socket = new Socket(dstAddress, dstPort);
//				dataOutputStream = new DataOutputStream(
//						socket.getOutputStream());
//				dataInputStream = new DataInputStream(socket.getInputStream());

				if (msgToServer != null) {
					msgToServer = EncryptDecrypt.encrypt(msgToServer);
//					dataOutputStream.writeUTF(msgToServer);
					AsyncHttpGet ahg = new AsyncHttpGet("http://" + dstAddress + ":" + 5001 + "/execute");
					ahg.addHeader(
							"orderStr",msgToServer);
					AsyncHttpClient.getDefaultInstance().executeString(ahg, new StringCallback() {
						
						@Override
						public void onCompleted(Exception e, AsyncHttpResponse source, String result) {
							try {
								response = EncryptDecrypt.decrypt(result);
							} catch (Exception e1) {
							}
						}
					});
				}
//				response = dataInputStream.readUTF();
//				response = EncryptDecrypt.decrypt(response);
//				dataOutputStream.writeUTF(msgToServer);
				
			} catch (Exception e) {
				e.printStackTrace();
//				response = "UnknownHostException: " + e.toString();
//				e.printStackTrace();
//				response = "IOException: " + e.toString();
			} finally {
//				if (socket != null) {
//					try {
//						socket.close();
//					} catch (IOException e) {
//						e.printStackTrace();
//					}
//				}
//				if (dataOutputStream != null) {
//					try {
//						dataOutputStream.close();
//					} catch (IOException e) {
//						e.printStackTrace();
//					}
//				}
//				if (dataInputStream != null) {
//					try {
//						dataInputStream.close();
//					} catch (IOException e) {
//						e.printStackTrace();
//					}
//				}
			}
			return null;
		}

		@Override
		protected void onPostExecute(Void result) {
			Log.d("tag", response + "((((((((((((((((((((((((((((((((((((((");
			activity.updateView(response);
			super.onPostExecute(result);
		}
	}
	
}

