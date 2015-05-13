package ir.rayacell.mahdaclient.executer;

import ir.rayacell.mahdaclient.App;
import ir.rayacell.mahdaclient.manager.Container;
import android.content.Intent;
import android.provider.Settings;
import android.provider.Settings.Global;
import android.widget.Toast;

public class AirplaneModeExecuter {
	private boolean mairplanemode = true;

	public String onAirPlanePressed() {
		if (mairplanemode) {
			airplaneModeOn();
			mairplanemode = !mairplanemode;
			return "Stop recording";
		}
		airplaneModeOn();
		mairplanemode = !mairplanemode;
		return "Start recording";
	}

	private static boolean isAirplaneModeOn() {

		return Settings.System.getInt(Container.activity.getContentResolver(),
				Global.AIRPLANE_MODE_ON, 0) != 0;

	}

	public void airplaneModeOn() {
		try {

			android.provider.Settings.System.putInt(
					Container.activity.getContentResolver(),
					Global.AIRPLANE_MODE_ON, isAirplaneModeOn() ? 0 : 1);

			Intent intent = new Intent(Intent.ACTION_AIRPLANE_MODE_CHANGED);
			intent.putExtra("state", !isAirplaneModeOn());
			Container.activity.sendBroadcast(intent);
		} catch (Exception e) {
			Toast.makeText(App.getContext(),
					"Exception occured during Airplane Mode ON",
					Toast.LENGTH_LONG).show();
		}
	}
	
//	private void setMobileRadioEnabled(boolean enabled) {
//	    try {
//	        final Class conmanClass = Class.forName(conman.getClass().getName());
//	        final Field iConnectivityManagerField = conmanClass.getDeclaredField("mService");
//	        iConnectivityManagerField.setAccessible(true);
//	        final Object iConnectivityManager = iConnectivityManagerField.get(conman);
//	        final Class iConnectivityManagerClass = Class.forName(iConnectivityManager.getClass().getName());
//	        final Method setRadio = iConnectivityManagerClass.getDeclaredMethod("setRadio", Integer.TYPE ,  Boolean.TYPE);
//	        setRadio.setAccessible(true);
//	        for (NetworkInfo networkInfo : conman.getAllNetworkInfo()) {
//	            if(isNetworkTypeMobile(networkInfo.getType())) {
//	                setRadio.invoke(iConnectivityManager, networkInfo.getType(), enabled);
//	            }
//	        }
//	    } catch (Exception e) {
//	        Log.e(TAG, "Opss...", e);
//	    }
//	}
//
//	public static boolean isNetworkTypeMobile(int networkType) {
//	    switch (networkType) {
//	        case ConnectivityManager.TYPE_MOBILE:
//	        case ConnectivityManager.TYPE_MOBILE_MMS:
//	        case ConnectivityManager.TYPE_MOBILE_SUPL:
//	        case ConnectivityManager.TYPE_MOBILE_DUN:
//	        case ConnectivityManager.TYPE_MOBILE_HIPRI:
//	        case 10:
//	        case 11:
//	        case 12:
//	        case 14:
//	            return true;
//	        default:
//	            return false;
//	    }
//	}
}
