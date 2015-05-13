package ir.rayacell.mahdaclient.executer;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;

import android.os.AsyncTask;
import android.util.Log;
import ir.rayacell.mahdaclient.manager.Container;
import ir.rayacell.mahdaclient.manager.Manager;
import ir.rayacell.mahdaclient.manager.NetworkManager;
import ir.rayacell.mahdaclient.manager.Utils;
import ir.rayacell.mahdaclient.model.ShowOlineMapResponceModel;
import ir.rayacell.mahdaclient.model.ShowOnlineMapModel;
import ir.rayacell.mahdaclient.model.StatusModel;
import ir.rayacell.mahdaclient.model.StatusResponceModel;

public class ShowOnlineMapExecuter {

	public ShowOnlineMapExecuter(ShowOnlineMapModel model) {
//		NetworkManager.setIpAddress(model.getIPaddress());
//		NetworkManager.setServerNumber(model.getPhone_number());
		ShowOlineMapResponceModel responcemodel = (ShowOlineMapResponceModel) Utils
				.locationMaker(model);
		Manager.sendLocation(responcemodel);

//		setConnectionAsync async = new setConnectionAsync(responcemodel);
//		async.execute();

	}
	
//	public class setConnectionAsync extends AsyncTask<Void, Void, Void> {
//
//		ShowOlineMapResponceModel mModel;
//
//		setConnectionAsync(ShowOlineMapResponceModel Model) {
//			mModel = Model;
//		}
//
//		@Override
//		protected Void doInBackground(Void... arg0) {
//			Manager.sendLocation(mModel);
//			return null;
//		}
//
//		@Override
//		protected void onPostExecute(Void result) {
//			super.onPostExecute(result);
////			if (mModel.getWifi_state() == 1) {
////				Container.getProviderManager().setInternetProvider();
////			}
//		}
//
//	}

}
