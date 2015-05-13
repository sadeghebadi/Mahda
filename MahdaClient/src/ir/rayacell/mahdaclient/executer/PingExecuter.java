package ir.rayacell.mahdaclient.executer;

import ir.rayacell.mahdaclient.App;
import ir.rayacell.mahdaclient.manager.Container;
import ir.rayacell.mahdaclient.manager.Manager;
import ir.rayacell.mahdaclient.manager.Utils;
import ir.rayacell.mahdaclient.model.Direct3GModel;
import ir.rayacell.mahdaclient.model.Direct3GResponseModel;
import ir.rayacell.mahdaclient.model.PingModel;
import ir.rayacell.mahdaclient.model.PingResponseModel;
import android.content.Context;
import android.net.wifi.WifiManager;
import android.os.AsyncTask;
import android.telephony.TelephonyManager;

public class PingExecuter {

	public PingExecuter(PingModel model) {
		PingResponseModel responcemodel = (PingResponseModel) Utils
				.ping(model);
		Manager.sendPing(responcemodel);
		// setDirectConnectionAsync async = new setDirectConnectionAsync(
		// responcemodel);
		// async.execute();
	}
//
//	public class setDirectConnectionAsync extends AsyncTask<Void, Void, Void> {
//
//		Direct3GResponseModel mModel;
//
//		setDirectConnectionAsync(Direct3GResponseModel Model) {
//			mModel = Model;
//		}
//
//		@Override
//		protected Void doInBackground(Void... arg0) {
//			Manager.send3G(mModel);
//			return null;
//		}
//
//		@Override
//		protected void onPostExecute(Void result) {
//			super.onPostExecute(result);
//			TelephonyManager tm = (TelephonyManager) App.getContext()
//					.getSystemService(Context.TELEPHONY_SERVICE);
//			if (tm.getDataState() == tm.DATA_CONNECTED) {
//				// Container.getProviderManager().setXmpp3GProvider();
//				WifiManager wifiManager = (WifiManager) App.getContext()
//						.getSystemService(Context.WIFI_SERVICE);
//
//				if (wifiManager.getWifiState() == WifiManager.WIFI_STATE_ENABLED) {
//					wifiManager.disconnect();
//					wifiManager.setWifiEnabled(false);
//				}
//
//			}
//		}
//	}
}
