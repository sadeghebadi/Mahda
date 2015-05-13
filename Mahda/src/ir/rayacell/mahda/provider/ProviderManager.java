package ir.rayacell.mahda.provider;

import java.util.Queue;

import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import ir.rayacell.mahda.App;
import ir.rayacell.mahda.MainActivity;
import ir.rayacell.mahda.manager.Container;
import ir.rayacell.mahda.manager.Manager;
import ir.rayacell.mahda.manager.NetworkManager;
import ir.rayacell.mahda.param.BaseParam;

public class ProviderManager {
	private static BaseProvider mProvider;
//	private static SMSProvider msmsprovider;
	private Queue<BaseParam> mQueue;
	private MainActivity activity;

	public BaseProvider getProvider() {
		if(mProvider == null){
			setSmsProvider();
		}
		return mProvider;
	}

//	public SMSProvider getSmsProvider() {
//		if (msmsprovider == null) {
//			msmsprovider = new SMSProvider(this, activity);
//			return msmsprovider;
//		}
//		return msmsprovider;
//	}

	public ProviderManager(MainActivity activity) {
		this.activity = activity;
	}
	public synchronized void setXmpp3GProvider(){
		mProvider = new Xmpp3GProvider(this , activity);
		mProvider.connect(new BaseParam(0, null, null));
	}
	public synchronized void setInternetProvider(/*InternetProvider provider*/) {
		mProvider = null;
		
		mProvider = new InternetProvider(this, activity);
		mProvider.connect(new BaseParam(0, null, null));
//		mProvider = new Xmpp3GProvider(this , activity);
//		mProvider.connect(new BaseParam(0, null, null));
	}

	public void setSmsProvider(/*SMSProvider provider*/) {
		mProvider = null;
		mProvider = new SMSProvider(this, activity);
		mProvider.connect(new BaseParam(0, null, null));
	}

	public void setConnection(/* BaseParam param */) {
		mProvider = null;
		mProvider = new InternetProvider(this, activity);
		mProvider.connect(new BaseParam(0, null, null));
	}

	public synchronized boolean send(final BaseParam param) {
//		Container.activity.runOnUiThread(new Runnable() {
//			
//			@Override
//			public void run() {
//				Toast.makeText(App.getContext(), param.getCommand()+"   "+ NetworkManager.dstAddress, 4000).show();
//			}
//		});
		new SenderAsyncTask().execute(param);
		return false;
	}

	public boolean recieve(BaseParam param) {
		Log.d("in receive", "333333333333333333333333333333" + param.getCommand() );
		Manager.controll(param);
		return true;
	}

	private class SenderAsyncTask extends AsyncTask<BaseParam, Void, Void> {

		@Override
		protected  synchronized Void doInBackground(BaseParam... param) {
			mProvider.send(param[0]);
			return null;
		}

	}

}
