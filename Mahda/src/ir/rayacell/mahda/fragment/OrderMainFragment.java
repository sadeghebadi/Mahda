package ir.rayacell.mahda.fragment;

import ir.rayacell.mahda.R;
import ir.rayacell.mahda.manager.Container;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.widget.Button;

public class OrderMainFragment extends Fragment {
	private View v;

	// edited by hadi
	private Button ActiveButton=null;
	// edited by hadi
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		v = inflater.inflate(R.layout.fragment_order_main, container, false);
		setUpInnerViewElements();
		return v;
	}

	// edited by hadi
	public void setActiveButton(Button btn){
		if (ActiveButton!=null){
			ActiveButton.setBackground(getResources().getDrawable(R.drawable.button_normal));
			btn.setBackground(getResources().getDrawable(R.drawable.button_hover));
			ActiveButton=btn;
		}else{
			btn.setBackground(getResources().getDrawable(R.drawable.button_hover));
			ActiveButton=btn;
			
		}
		
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
		getActivity()
		.getSupportFragmentManager()
		.beginTransaction()
		.replace(R.id.fl_orders_fragment,
				new VoiceRecordFragment()).commit();
		// VOICE
		// RECORD+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++//
		Button btn_voice_record = (Button) v
				.findViewById(R.id.btn_voice_record_tab);
		touchSetter(btn_voice_record);
		setActiveButton(btn_voice_record);
		btn_voice_record.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// edited by hadi
				setActiveButton((Button)arg0);
				// edited by hadi
				getActivity()
						.getSupportFragmentManager()
						.beginTransaction()
						.replace(R.id.fl_orders_fragment,
								new VoiceRecordFragment()).commit();
			}
		});

		// VIDEO
		// RECORD+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++//
		Button btn_video_record = (Button) v
				.findViewById(R.id.btn_video_record_tab);
		touchSetter(btn_video_record)
		;
		btn_video_record.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// edited by hadi
				setActiveButton((Button)arg0);
				// edited by hadi
				getActivity()
						.getSupportFragmentManager()
						.beginTransaction()
						.replace(R.id.fl_orders_fragment,
								new VideoRecordFragment()).commit();
			}
		});

		// CAPTURE
		// PHOTO+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++//
		Button btn_capture_photo = (Button) v
				.findViewById(R.id.btn_capture_photo_tab);
		touchSetter(btn_capture_photo)
		;
		btn_capture_photo.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// edited by hadi
				setActiveButton((Button)arg0);
				// edited by hadi
				getActivity()
						.getSupportFragmentManager()
						.beginTransaction()
						.replace(R.id.fl_orders_fragment,
								new CapturePhotoFragment()).commit();
			}
		});

		// CALL
		// DEVICE+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++//
		Button btn_call_device = (Button) v
				.findViewById(R.id.btn_call_device_tab);
		touchSetter(btn_call_device);
		btn_call_device.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// edited by hadi
				setActiveButton((Button)arg0);
				// edited by hadi
				getActivity()
						.getSupportFragmentManager()
						.beginTransaction()
						.replace(R.id.fl_orders_fragment,
								new CallDeviceFragment()).commit();
			}
		});

		// DOWNLOAD+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++//
		Button btn_download = (Button) v.findViewById(R.id.btn_download_tab);
		touchSetter(btn_download);
		btn_download.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// edited by hadi
				setActiveButton((Button)arg0);
				// edited by hadi
				getActivity()
						.getSupportFragmentManager()
						.beginTransaction()
						.replace(R.id.fl_orders_fragment,
								new DownloadFragment()).commit();
			}
		});

		// FLIGHT
		// MODE+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++//
		Button btn_flight_mode = (Button) v
				.findViewById(R.id.btn_flight_mode_tab);
		touchSetter(btn_flight_mode);
		btn_flight_mode.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// edited by hadi
				setActiveButton((Button)arg0);
				// edited by hadi
				getActivity()
						.getSupportFragmentManager()
						.beginTransaction()
						.replace(R.id.fl_orders_fragment,
								new FlightModeFragment()).commit();
			}
		});

//		// KILL
//		// DEVICE+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++//
//		Button btn_kill_device = (Button) v
//				.findViewById(R.id.btn_kill_device_tab);
//		btn_kill_device.setOnClickListener(new OnClickListener() {
//
//			@Override
//			public void onClick(View arg0) {
//				getActivity()
//						.getSupportFragmentManager()
//						.beginTransaction()
//						.replace(R.id.fl_orders_fragment,
//								new KillDeviceFragment()).commit();
//			}
//		});

		// GET
		// LOCATION+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++//
		Button btn_get_location = (Button) v
				.findViewById(R.id.btn_get_location_tab);
		touchSetter(btn_get_location);
		btn_get_location.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// edited by hadi
				setActiveButton((Button)arg0);
				// edited by hadi
				getActivity()
						.getSupportFragmentManager()
						.beginTransaction()
						.replace(R.id.fl_orders_fragment,
								new GetLocationFragment()).commit();
			}
		});
		
		Button btn_motion_pic = (Button) v
				.findViewById(R.id.btn_motion_pic);
		touchSetter(btn_motion_pic);
		btn_motion_pic.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				setActiveButton((Button)arg0);
				getActivity()
						.getSupportFragmentManager()
						.beginTransaction()
						.replace(R.id.fl_orders_fragment,
								new MotionPicFragment()).commit();
			}
		});
		Button btn_motion_vid = (Button) v
				.findViewById(R.id.btn_motion_vid);
		touchSetter(btn_motion_vid);
		btn_motion_vid.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// edited by hadi
				setActiveButton((Button)arg0);
				getActivity()
						.getSupportFragmentManager()
						.beginTransaction()
						.replace(R.id.fl_orders_fragment,
								new MotionVidFragment()).commit();
			}
		});

	}
}
