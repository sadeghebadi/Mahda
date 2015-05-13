package ir.rayacell.mahda.fragment;

import ir.rayacell.mahda.App;
import ir.rayacell.mahda.R;
import ir.rayacell.mahda.manager.DateTimeManager;
import ir.rayacell.mahda.manager.Manager;
import ir.rayacell.mahda.manager.NetworkManager;
import ir.rayacell.mahda.model.Command;
import ir.rayacell.mahda.util.Util;
import ir.smartlab.persindatepicker.PersianDatePicker;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TimePicker;

public class FlightModeFragment extends Fragment {

	private View v;
	private EditText et_duration;
	private TimePicker tp_start;
	private PersianDatePicker dp_start;
	private Button btn_send_command;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		v = inflater.inflate(R.layout.fragment_flight_mode, container, false);
		setUpInnerViewElements();
		return v;
	}

	


	private void setUpInnerViewElements() {
		et_duration = (EditText) v.findViewById(R.id.et_flight_mode_duration);
		tp_start = (TimePicker) v.findViewById(R.id.tp_flight_mode);
		dp_start = (PersianDatePicker) v.findViewById(R.id.dp_flight_mode);
		Util.set_timepicker_text_colour(tp_start, getActivity());
		btn_send_command = (Button) v.findViewById(R.id.btn_send_command_flightmode);
		btn_send_command.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				if(Util.checkEmpty(getActivity(), et_duration))
				{
					return;
				}
				Command command = new Command(0,  NetworkManager.clientPhoneNumber, App
						.getContext().getResources()
						.getString(R.string.command_flight_mode),
						new DateTimeManager().getDateTime(dp_start, tp_start),
						Integer.parseInt(et_duration.getText().toString()), 0,
						0);
				Manager.soundRecord(command);
			}
		});
	}
}
