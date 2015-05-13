package ir.rayacell.mahda.services;

import ir.rayacell.mahda.App;
import ir.rayacell.mahda.Constants;
import ir.rayacell.mahda.manager.Container;
import ir.rayacell.mahda.param.BaseParam;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

public class ResponseReceiver extends BroadcastReceiver {
	@Override
	public void onReceive(Context context, Intent intent) {
		Bundle bundle = intent.getExtras();
		if (bundle != null) {
			String response = bundle.getString(Constants.RESULT_KEY);
			// Toast.makeText(App.getContext(), response + "!!!!!!!!!!!!!!",
			// Toast.LENGTH_LONG).show();
			Log.d("receiver", response + "!!!!!!!!!!!!!!");
			// Container.getTestFragment().updateView(response);
			BaseParam param = new BaseParam(0, null, null);
			param.mCommand = response;
			Container.getProviderManager().recieve(param);
			IntentFilter mActionIntentFilter = new IntentFilter(
					Constants.BROADCAST_ACTION);
			LocalBroadcastManager.getInstance(App.getContext()).unregisterReceiver(this);
		}
	}

}