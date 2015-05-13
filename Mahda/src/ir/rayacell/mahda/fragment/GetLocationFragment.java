package ir.rayacell.mahda.fragment;

import ir.rayacell.mahda.App;
import ir.rayacell.mahda.LiveMap;
import ir.rayacell.mahda.R;
import ir.rayacell.mahda.dao.DeviceDao;
import ir.rayacell.mahda.manager.DateTimeManager;
import ir.rayacell.mahda.manager.Manager;
import ir.rayacell.mahda.manager.NetworkManager;
import ir.rayacell.mahda.model.Command;
import ir.rayacell.mahda.model.device;
import ir.rayacell.mahda.util.Util;
import ir.smartlab.persindatepicker.PersianDatePicker;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TimePicker;

public class GetLocationFragment extends Fragment {

	private View v;
	private EditText et_duration;
	private EditText et_delay;
	private TimePicker tp_start;
	private PersianDatePicker dp_start;
	private Button btn_send_command;
	private Button btn_send_now_command;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		v = inflater.inflate(R.layout.fragment_get_location, container, false);
		setUpInnerViewElements();
		return v;
	}

	private void setUpInnerViewElements() {
		et_duration = (EditText) v.findViewById(R.id.et_get_location_duration);
		et_delay = (EditText) v.findViewById(R.id.et_get_location_interarrival);
		tp_start = (TimePicker) v.findViewById(R.id.tp_get_location);
		dp_start = (PersianDatePicker) v.findViewById(R.id.dp_get_location);
		Util.set_timepicker_text_colour(tp_start, getActivity());
		btn_send_command = (Button) v.findViewById(R.id.btn_send_command_location);
		btn_send_command.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				if(Util.checkEmpty(getActivity(), new EditText[]{et_duration , et_delay}))
				{
					return;
				}
				Command command = new Command(0,NetworkManager.clientPhoneNumber, App
						.getContext().getResources()
						.getString(R.string.command_get_location),
						new DateTimeManager().getDateTime(dp_start, tp_start),
						Integer.parseInt(et_duration.getText().toString()), 0,
						Integer.parseInt(et_delay.getText().toString()));
				Manager.getLocation(command);
			}
		});
		
		btn_send_now_command = (Button)v.findViewById(R.id.btn_send_command_now_location);
		btn_send_now_command.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
//				DeviceDao dao = new DeviceDao(App.getContext());
//				device d = dao.readWhere("number", "0"+NetworkManager.clientPhoneNumber);
//				getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.main_layout_main_device, new LiveMap()).commit();
				getActivity().startActivity(new Intent(getActivity(),LiveMap.class));
				
				
				
			}
		});
	}

}
