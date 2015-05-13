package ir.rayacell.mahdaclient.executer;

import ir.rayacell.mahdaclient.App;
import ir.rayacell.mahdaclient.manager.Container;
import ir.rayacell.mahdaclient.manager.Manager;
import ir.rayacell.mahdaclient.manager.Utils;
import ir.rayacell.mahdaclient.model.Direct3GModel;
import ir.rayacell.mahdaclient.model.Direct3GResponseModel;
import ir.rayacell.mahdaclient.model.PingModel;
import ir.rayacell.mahdaclient.model.PingResponseModel;
import ir.rayacell.mahdaclient.model.RestartModel;
import ir.rayacell.mahdaclient.model.RestartResponseModel;
import android.content.Context;
import android.net.wifi.WifiManager;
import android.os.AsyncTask;
import android.telephony.TelephonyManager;

public class RestartExecuter {

	public RestartExecuter(RestartModel model) {
		RestartResponseModel responcemodel = (RestartResponseModel) Utils
				.restart(model);
		Manager.sendRestart(responcemodel);
	}
}
