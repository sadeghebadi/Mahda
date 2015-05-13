package ir.rayacell.mahdaclient.executer;

import ir.rayacell.mahdaclient.manager.Container;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;

public class CallServerExecuter {

	
	public void startCall(String number){
		Uri tel = Uri.parse("tel:" + number);
		Intent callIntent = new Intent(Intent.ACTION_CALL, tel);
		Log.d("number", tel + "))))))))))))))))))(((((((((((((");

		if (callIntent.resolveActivity(Container.activity
				.getPackageManager()) != null) {
			Container.activity.startActivity(callIntent);
		}
	}
}
