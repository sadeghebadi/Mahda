package ir.rayacell.mahdaclient.executer;

import android.os.AsyncTask;
import ir.rayacell.mahdaclient.manager.Container;
import ir.rayacell.mahdaclient.manager.Manager;
import ir.rayacell.mahdaclient.manager.NetworkManager;
import ir.rayacell.mahdaclient.manager.Utils;
import ir.rayacell.mahdaclient.model.DirectWIFIModel;
import ir.rayacell.mahdaclient.model.DirectWIFIResponseModel;
import ir.rayacell.mahdaclient.model.StatusModel;
import ir.rayacell.mahdaclient.model.StatusResponceModel;

public class DirectWIFIExecuter {

	public DirectWIFIExecuter(DirectWIFIModel model) {
		DirectWIFIResponseModel responcemodel = (DirectWIFIResponseModel) Utils
				.wifidirect(model);
		Manager.sendWifi(responcemodel);
//		setDirectConnectionAsync async = new setDirectConnectionAsync(
//				responcemodel);
//		async.execute();
	}
//
//	public class setDirectConnectionAsync extends AsyncTask<Void, Void, Void> {
//
//		DirectWIFIResponseModel mModel;
//
//		setDirectConnectionAsync(DirectWIFIResponseModel Model) {
//			mModel = Model;
//		}
//
//		@Override
//		protected Void doInBackground(Void... arg0) {
//			Manager.sendWifi(mModel);
//			return null;
//		}
//
//		@Override
//		protected void onPostExecute(Void result) {
//			super.onPostExecute(result);
////			if (Container.getWifiApManager().isWifiApEnabled())
////				Container.getProviderManager().setInternetProvider();
//		}
//
//	}
}
