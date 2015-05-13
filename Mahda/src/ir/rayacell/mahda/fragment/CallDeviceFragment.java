package ir.rayacell.mahda.fragment;

import ir.rayacell.mahda.App;
import ir.rayacell.mahda.R;
import ir.rayacell.mahda.manager.Manager;
import ir.rayacell.mahda.manager.NetworkManager;
import ir.rayacell.mahda.model.BaseModel;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageButton;

public class CallDeviceFragment extends Fragment {

	private View v;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		v = inflater.inflate(R.layout.fragment_call_device, container, false);
		setUpInnerViewElements();
		return v;

	}

	public void setUpInnerViewElements() {
		ImageButton btn_call_device = (ImageButton) v.findViewById(R.id.btn_call_device);
		btn_call_device.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				BaseModel model = new BaseModel(0,
						NetworkManager.clientPhoneNumber, App.getContext()
								.getResources()
								.getString(R.string.command_call_device));
				Manager.callDevice(model);
			}
		});
	}

}
