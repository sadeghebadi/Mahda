package ir.rayacell.mahda.fragment;

import ir.rayacell.mahda.R;
import ir.rayacell.mahda.manager.NetworkManager;
import ir.rayacell.mahda.model.device;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.widget.Button;

public class DeviceMainFragment extends Fragment implements Disabler {
	private View v;
	private device mdevice;
	private Button btn_orders;
	private Button btn_history;

	// edited by hadi
	private Button ActiveButton = null;

	// edited by hadi

	// public DeviceMainFragment(){}
	//
	// public DeviceMainFragment(device mdevice){
	// NetworkManager.clientPhoneNumber = mdevice.number;
	// }

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		v = inflater.inflate(R.layout.fragment_devices_main, container, false);
		// DeviceDao dao = new DeviceDao(getActivity());
		// Bundle bundle = this.getArguments();

		getActivity().getActionBar().getCustomView().findViewById(R.id.toggle)
				.setVisibility(View.INVISIBLE);
		// mdevice = dao.loadDeviceFromDB(bundle.getInt("position", 1));
//		NetworkManager.clientPhoneNumber = /* mdevice.number */"9302964951";
//		NetworkManager.clientPhoneNumber = 
		NetworkManager.dstAddress = "";
		getActivity()
				.getSupportFragmentManager()
				.beginTransaction()
				.replace(R.id.fl_action_device_fragments,
						new DeviceInfoFragment(this)).commit();
		setUpInnerViewElements();
		return v;
	}

	// edited by hadi
	public void setActiveButton(Button btn) {
		if (ActiveButton != null) {
			ActiveButton.setBackground(getResources().getDrawable(
					R.drawable.button_normal));
			btn.setBackground(getResources().getDrawable(
					R.drawable.button_hover));
			ActiveButton = btn;
		} else {
			btn.setBackground(getResources().getDrawable(
					R.drawable.button_hover));
			ActiveButton = btn;

		}

	}
	
	

	public void setDisbaleOrderButton(Button btn) {
		btn.setBackground(getResources().getDrawable(
				R.drawable.button_disable));
	}

	private void touchSetter(Button b) {
		b.setOnTouchListener(new OnTouchListener() {

			@Override
			public boolean onTouch(View arg0, MotionEvent arg1) {
				// TODO Auto-generated method stub
				return false;
			}
		});
	}

	public void setUpInnerViewElements() {

		// device info section

		Button btn_deviceinfor = (Button) v.findViewById(R.id.btn_device_info);
		setActiveButton(btn_deviceinfor);
	
		btn_deviceinfor.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// edited by hadi
				setActiveButton((Button) arg0);
				// edited by hadi
				getActivity()
						.getSupportFragmentManager()
						.beginTransaction()
						.replace(R.id.fl_action_device_fragments,
								new DeviceInfoFragment(DeviceMainFragment.this))
						.commit();
			}
		});
		touchSetter(btn_deviceinfor);
		
		
		
		// orders section
		btn_orders = (Button) v.findViewById(R.id.btn_orders);
		setDisbaleOrderButton(btn_orders);
		touchSetter(btn_orders);
		
		
		// file manager section
		Button btn_filemanager = (Button) v.findViewById(R.id.btn_file_manager);
		touchSetter(btn_filemanager);
		btn_filemanager.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// edited by hadi
				setActiveButton((Button) arg0);
				// edited by hadi
				getActivity()
						.getSupportFragmentManager()
						.beginTransaction()
						.replace(R.id.fl_action_device_fragments,
								new FileManagerFragment()).commit();
			}
		});

		// history section
		btn_history = (Button) v.findViewById(R.id.btn_history);
		setDisbaleOrderButton(btn_history);
		touchSetter(btn_history);
//		btn_history.setOnClickListener(new OnClickListener() {
//
//			@Override
//			public void onClick(View arg0) {
//				// edited by hadi
//				setActiveButton((Button) arg0);
//				// edited by hadi
//				getActivity()
//						.getSupportFragmentManager()
//						.beginTransaction()
//						.replace(R.id.fl_action_device_fragments,
//								new HistoryFragment()).commit();
//			}
//		});
	}

	@Override
	public void enableOrderButton() {
		btn_orders.setBackground(getResources().getDrawable(
				R.drawable.button_normal));
		btn_orders.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// edited by hadi
				setActiveButton((Button) arg0);
				// edited by hadi
				getActivity()
						.getSupportFragmentManager()
						.beginTransaction()
						.replace(R.id.fl_action_device_fragments,
								new OrderMainFragment()).commit();
			}
		});
	}
	
	@Override
	public void enableHistoryButton() {
		btn_history.setBackground(getResources().getDrawable(
				R.drawable.button_normal));
		btn_history.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// edited by hadi
				setActiveButton((Button) arg0);
				// edited by hadi
				getActivity()
						.getSupportFragmentManager()
						.beginTransaction()
						.replace(R.id.fl_action_device_fragments,
								new HistoryFragment()).commit();
			}
		});
	}

	@Override
	public void onDestroy() {
		DeviceInfoFragment.load = false;
		super.onDestroy();
	}

}
