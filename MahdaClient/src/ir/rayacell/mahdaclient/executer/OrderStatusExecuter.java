package ir.rayacell.mahdaclient.executer;

import ir.rayacell.mahdaclient.manager.Manager;
import ir.rayacell.mahdaclient.manager.Utils;
import ir.rayacell.mahdaclient.model.OrderStatusModel;
import ir.rayacell.mahdaclient.model.OrderStatusResponseModel;
import android.os.AsyncTask;

public class OrderStatusExecuter {

	public OrderStatusExecuter(OrderStatusModel model) {
		OrderStatusResponseModel responcemodel = (OrderStatusResponseModel) Utils
				.getOrderStatus(model);
		Manager.sendOrderStatus(responcemodel);

//		setDirectConnectionAsync async = new setDirectConnectionAsync(
//				responcemodel);
//		async.execute();
	}

//	public class setDirectConnectionAsync extends AsyncTask<Void, Void, Void> {
//
//		OrderStatusResponseModel mModel;
//
//		setDirectConnectionAsync(OrderStatusResponseModel Model) {
//			mModel = Model;
//		}
//
//		@Override
//		protected Void doInBackground(Void... arg0) {
//			Manager.sendOrderStatus(mModel);
//			return null;
//		}
//
//		@Override
//		protected void onPostExecute(Void result) {
//			super.onPostExecute(result);
//
//		}
//
//	}
}
