package ir.rayacell.mahda.fragment;

import ir.rayacell.mahda.App;
import ir.rayacell.mahda.R;
import ir.rayacell.mahda.manager.DateTimeManager;
import ir.rayacell.mahda.manager.Manager;
import ir.rayacell.mahda.manager.NetworkManager;
import ir.rayacell.mahda.model.Command;
import ir.rayacell.mahda.util.Util;
import ir.smartlab.persindatepicker.PersianDatePicker;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;

public class VideoRecordFragment extends Fragment {

	private View v;
	private EditText et_duration;
	private TimePicker tp_start;
	private PersianDatePicker dp_start;
	private Button btn_send_command;
	private Spinner sp_quality;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		v = inflater.inflate(R.layout.fragment_video_record, container, false);
		setUpInnerViewElements();
		return v;
	}
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
	}
	private void setUpInnerViewElements() {
		sp_quality = (Spinner) v.findViewById(R.id.sp_video_record_quality);

		et_duration = (EditText) v.findViewById(R.id.et_video_record_duration);
		tp_start = (TimePicker) v.findViewById(R.id.tp_video_record);
		dp_start = (PersianDatePicker) v.findViewById(R.id.dp_video_record);
		Util.set_timepicker_text_colour(tp_start, getActivity());
		btn_send_command = (Button) v.findViewById(R.id.btn_send_command_video);
		btn_send_command.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				if(Util.checkEmpty(getActivity(), et_duration))
				{
					return;
				}
				Command command = new Command(0,  NetworkManager.clientPhoneNumber, App
						.getContext().getResources()
						.getString(R.string.command_video_record),
						new DateTimeManager().getDateTime(dp_start, tp_start),
						Integer.parseInt(et_duration.getText().toString()), 0,
						0 , (int)sp_quality.getSelectedItemId());
				Manager.videoRecord(command);
			}
		});
	}
}
