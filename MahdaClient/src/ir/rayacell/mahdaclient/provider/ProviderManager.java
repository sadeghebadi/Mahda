package ir.rayacell.mahdaclient.provider;

import java.util.ArrayList;
import java.util.Queue;

import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.preference.PreferenceManager;

import ir.rayacell.mahdaclient.App;
import ir.rayacell.mahdaclient.MainActivity;
import ir.rayacell.mahdaclient.ServerInit;
import ir.rayacell.mahdaclient.manager.Container;
import ir.rayacell.mahdaclient.manager.Manager;
import ir.rayacell.mahdaclient.manager.NetworkManager;
import ir.rayacell.mahdaclient.model.BaseModel;
import ir.rayacell.mahdaclient.model.Command;
import ir.rayacell.mahdaclient.param.BaseParam;

public class ProviderManager {
	private/* public */static BaseProvider mProvider;
	private Queue<BaseParam> mQueue;
	private MainActivity activity;

	public BaseProvider getProvider() {
		return mProvider;
	}

	public ProviderManager(MainActivity activity) {
		this.activity = activity;
	}

	public synchronized void setXmpp3GProvider() {
		SharedPreferences prefs = PreferenceManager
				.getDefaultSharedPreferences(activity);

		prefs.edit().putString("provider", "3");
		prefs.edit().commit();
		mProvider = null;
		mProvider = new Xmpp3GProvider(this, activity);
		mProvider.connect(new BaseParam(0, null, null));
	}

	public synchronized void setInternetProvider() {
		SharedPreferences prefs = PreferenceManager
				.getDefaultSharedPreferences(activity);

		prefs.edit().putString("provider", "2");
		prefs.edit().commit();
		mProvider = null;

		mProvider = new InternetProvider(this, activity);
		mProvider.connect(new BaseParam(0, null, null));

		// mProvider = new Xmpp3GProvider(this , activity);
		// mProvider.connect(new BaseParam(0, null, null));
	}

	public void setSmsProvider() {
		// SharedPreferences prefs = PreferenceManager
		// .getDefaultSharedPreferences(activity);

		// prefs.edit().putString("provider", "1");
		// prefs.edit().commit();
		mProvider = null;
		mProvider = new SMSProvider(this, activity);
		mProvider.connect(new BaseParam(0, null, null));
	}

	// public synchronized void setConnection(/*BaseParam param*/) {

	// NetworkManager.setServerNumber(param.getPhone_number());
	// BaseModel model = stringParser(param.getCommand_type());
	// if (model.getPhone_number() == null) {
	// mProvider = new SMSProvider(this, activity);
	// return;
	// }
	// NetworkManager.setIpAddress(model.getPhone_number());
	// mProvider = new InternetProvider(this, activity);
	// mProvider.connect(new BaseParam(0, null, null));
	// }
	public synchronized boolean send(final BaseParam param) {
		new Thread(new Runnable() {

			@Override
			public void run() {

				// new SenderAsyncTask().execute(param);
				// if(mProvider != null)
				mProvider.send(param);
				// else {
				// mProvider = new SMSProvider(this, activity);
				// mProvider.send(param);
				// }
			}
		}).start();
		return false;

	}

	/**
	 * implement a call to database and manager. every command must first be
	 * written in database then forwarded to manager in order to start a
	 * service.
	 * 
	 * @param model
	 */
	public boolean recieve(BaseParam param) {
		// call to database

		// call to manager
		// Container.getProviderManager().setInternetProvider();
		Manager.controll(param);
		return false;

	}

	// private static BaseModel stringParser(String received_command) {
	// ArrayList<Integer> star_index = new ArrayList<Integer>();
	// for (int i = 0; i < received_command.length(); i++) {
	// if (received_command.charAt(i) == '*') {
	// star_index.add(i);
	// }
	// }
	// String commandType = received_command.substring(star_index.get(0) + 1,
	// star_index.get(1));
	// String ip = received_command.substring(star_index.get(1) + 1,
	// star_index.get(2));
	//
	// BaseModel model = new BaseModel(0, ip, commandType);
	//
	// return model;
	// }

	// private class SenderAsyncTask extends AsyncTask<BaseParam, Void, Void> {
	//
	// public SenderAsyncTask(){
	// System.out.println("in sender");
	// }
	// @Override
	// protected Void doInBackground(BaseParam... param) {
	// mProvider.send(param[0]);
	// return null;
	// }
	//
	// }

	// public void sendSMS(String message){
	// SMSProvider tempprovider = new SMSProvider(this, Container.activity);
	// tempprovider.sendSMS(App.getContext(), message);
	// }

}
