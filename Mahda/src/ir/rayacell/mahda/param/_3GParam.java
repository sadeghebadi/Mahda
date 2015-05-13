package ir.rayacell.mahda.param;

import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import ir.rayacell.mahda.App;
import ir.rayacell.mahda.manager.NetworkManager;
import ir.rayacell.mahda.model.BaseModel;

public class _3GParam extends BaseParam {

	public _3GParam(BaseModel model) {
		super(model.getCommand_id(), model.getPhone_number(), model
				.getCommand_type());
		mCommand = new String();
		SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(App.getContext());
		String LanSettining = sharedPrefs.getString("ip", "127.0.0.1");
		mCommand = "*" + model.getCommand_id() + "*" + model.getCommand_type()
				+ "*" + model.getPhone_number() + "*"
				+ NetworkManager.getIpAddress() + "*"+LanSettining+"*";
	}
}
